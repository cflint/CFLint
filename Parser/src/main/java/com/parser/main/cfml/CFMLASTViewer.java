package com.parser.main.cfml;

/*
 Copyright (c) 2007 Mark Mandel, Mark Drew

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.	
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.Tree;

import com.parser.main.cfml.antlr.CFMLParser.scriptBlock_return;
import com.parser.main.cfscript.ANTLRNoCaseStringStream;

public class CFMLASTViewer {
	private JFrame window;
	private JTextArea text;
	private JTextArea log;
	private HashMap<Integer, String> tokenTypes;
	private ICFMLDictionary dictionary;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CFMLASTViewer ast = new CFMLASTViewer();
	}
	
	public CFMLASTViewer() {
		this(new DefaultCFMLDictionary());
	}
	
	public CFMLASTViewer(ICFMLDictionary dictionary) {
		setTokenTypes(new HashMap<Integer, String>());
		
		initCFMLTokenTypes();
		initCFScriptTokenTypes();
		System.out.println(getTokenTypes().toString());
		
		setDictionary(dictionary);
		
		window = new JFrame("AST Viewer");
		JFrame logger = new JFrame("Log");
		
		JButton button = new JButton("Parse >>");
		
		window.addWindowListener(new WindowListener() {
			public void windowClosed(WindowEvent e) {
			}
			
			public void windowActivated(WindowEvent e) {
			}
			
			public void windowClosing(WindowEvent e) {
				System.out.println("closing");
				System.exit(0);
			}
			
			public void windowDeactivated(WindowEvent e) {
			}
			
			public void windowDeiconified(WindowEvent e) {
			}
			
			public void windowOpened(WindowEvent e) {
			}
			
			public void windowIconified(WindowEvent e) {
			}
		});
		window.setSize(300, 300);
		
		window.setLayout(new BorderLayout());
		
		text = new JTextArea();
		try {
			File bar = new File("./src/cfml/parsing/cfml/test1.cfm");
			String path = bar.getAbsolutePath();
			text.setText(readFileAsString(path));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		window.add(new JScrollPane(text), BorderLayout.CENTER);
		
		window.add(button, BorderLayout.SOUTH);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				parseCFML(text.getText());
			}
		});
		
		window.setVisible(true);
		
		// do the logger
		
		logger.setSize(300, 600);
		logger.setLocation(320, 0);
		
		log = new JTextArea();
		
		logger.setLayout(new BorderLayout());
		
		logger.add(new JScrollPane(log), BorderLayout.CENTER);
		
		logger.setVisible(true);
	}
	
	private void parseCFML(String cfml) {
		try {
			// this is pretty quick n' dirty... oh well.
			IErrorObserver observer = new IErrorObserver() {
				public void actionCFMLParserError(ErrorEvent event) {
					log.append("Error at: " + event.getException().line + ":");
					log.append(event.getException().charPositionInLine + "\n");
					log.append(event.getErrorMsg() + "\n");
				}
			};
			
			log.append(cfml + "\n");
			CharStream input = new ANTLRNoCaseStringStream(cfml);
			CFMLLexer lexer = new CFMLLexer(input);
			
			lexer.addObserver(observer);
			
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			CFMLParser parser = new CFMLParser(tokens, getDictionary());
			
			parser.addObserver(observer);
			
			// CFMLParser.script_return root = parser.script();
			scriptBlock_return root = parser.scriptBlock();
			
			Tree ast = (Tree) root.getTree();
			
			JFrame astWindow = new JFrame("Tree...");
			
			astWindow.setLayout(new BorderLayout());
			
			JTree tree = new JTree(buildTree(ast));
			
			astWindow.add(new JScrollPane(tree), BorderLayout.CENTER);
			
			astWindow.setSize(300, 300);
			astWindow.setLocation(640, 0);
			
			astWindow.setVisible(true);
			
			// cleanup afterwoulds
			lexer.removeObserver(observer);
			parser.removeObserver(observer);
		} catch (Throwable t) {
			System.out.println("Exception: " + t.getMessage());
			t.printStackTrace();
		}
	}
	
	private DefaultMutableTreeNode buildTree(Tree tree) {
		DefaultMutableTreeNode node = displayNode(tree);
		buildTree(tree, node);
		
		return node;
	}
	
	private void buildTree(Tree tree, DefaultMutableTreeNode node) {
		for (int counter = 0; counter < tree.getChildCount(); counter++) {
			Tree child = tree.getChild(counter);
			DefaultMutableTreeNode childNode = displayNode(child);
			node.add(childNode);
			buildTree(child, childNode);
		}
	}
	
	private DefaultMutableTreeNode displayNode(Tree t) {
		String tokenType = getTokenType(t.getType());
		
		String str = "[" + tokenType + "] ";
		str += t.getText();
		str += " (line:" + t.getLine() + ", pos: " + t.getCharPositionInLine() + ")";
		return new DefaultMutableTreeNode(str);
	}
	
	private void initCFMLTokenTypes() {
		CFMLParser parser = new CFMLParser(null, null);
		try {
			Class clazz = Thread.currentThread().getContextClassLoader()
					.loadClass("com.parser.main.cfml.antlr.CFMLParser");
			Field[] fields = clazz.getDeclaredFields();
			
			for (Field f : fields) {
				if (f.getType().getName().equals("int")) {
					getTokenTypes().put(f.getInt(parser), f.getName());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void initCFScriptTokenTypes() {
		CFScriptParser parser = new CFScriptParser(null);
		try {
			Class clazz = Thread.currentThread().getContextClassLoader()
					.loadClass("com.parser.main.cfml.antlr.CFScriptParser");
			Field[] fields = clazz.getDeclaredFields();
			
			for (Field f : fields) {
				if (f.getType().getName().equals("int")) {
					getTokenTypes().put(f.getInt(parser), f.getName());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private String getTokenType(int type) {
		if (getTokenTypes().containsKey(type)) {
			return getTokenTypes().get(type);
		}
		return Integer.toString(type);
	}
	
	private HashMap<Integer, String> getTokenTypes() {
		return tokenTypes;
	}
	
	private void setTokenTypes(HashMap<Integer, String> tokenTypes) {
		this.tokenTypes = tokenTypes;
	}
	
	private ICFMLDictionary getDictionary() {
		return dictionary;
	}
	
	private void setDictionary(ICFMLDictionary dictionary) {
		this.dictionary = dictionary;
	}
	
	private static String readFileAsString(String filePath) throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}
}
