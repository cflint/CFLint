package com.parser.main.cfml.antlr;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.parser.main.cfml.DefaultCFMLDictionary;
import com.parser.main.cfml.ICFMLDictionary;
import com.parser.main.cfscript.ANTLRNoCaseStringStream;

public class XMLASTViewer extends JPanel implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame window;
	private JTextArea text;
	private JTextArea log;
	private HashMap<Integer, String> tokenTypes;
	private ICFMLDictionary dictionary;
	private JTree tree;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XMLASTViewer ast = new XMLASTViewer();
	}
	
	public XMLASTViewer() {
		this(new DefaultCFMLDictionary());
	}
	
	public XMLASTViewer(ICFMLDictionary dictionary) {
		setTokenTypes(new HashMap<Integer, String>());
		
		initCFMLTokenTypes();
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
			File bar = new File("./src/cfml/parsing/cfml/antlr/input2");
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
			
			log.append(cfml + "\n");
			CharStream input = new ANTLRNoCaseStringStream(cfml);
			// /CharStream input = new
			// ANTLRFileStream("/Users/valliant/programs/eclipse-inst/workspaces/pde/fart/src/input");
			XMLLexer lexer = new XMLLexer(input);
			
			// CommonTokenStream tokens = new CommonTokenStream(lexer);
			TokenStream tokens = new CommonTokenStream(lexer);
			XMLParser parser = new XMLParser(tokens);
			
			// XMLParser.script_return root = parser.script();
			
			XMLParser.compilationUnit_return root = parser.compilationUnit();
			
			CommonTree ast = (CommonTree) root.tree;
			
			JFrame astWindow = new JFrame("Tree...");
			
			astWindow.setLayout(new BorderLayout());
			
			tree = new JTree(buildTree(ast));
			tree.addMouseListener(this);
			
			astWindow.add(new JScrollPane(tree), BorderLayout.CENTER);
			
			astWindow.setSize(500, 300);
			astWindow.setLocation(640, 0);
			
			astWindow.setVisible(true);
			
		} catch (Throwable t) {
			System.out.println("Exception: " + t.getMessage());
			t.printStackTrace();
		}
	}
	
	private TreeNode buildTree(CommonTree tree) {
		TreeNode node = displayNode(tree);
		buildTree(tree, node);
		return node;
	}
	
	private void buildTree(CommonTree tree, TreeNode node) {
		for (int counter = 0; counter < tree.getChildCount(); counter++) {
			CommonTree child = (CommonTree) tree.getChild(counter);
			TreeNode childNode = displayNode(child);
			node.add(childNode);
			buildTree(child, childNode);
		}
	}
	
	private TreeNode displayNode(CommonTree t) {
		int startIndex = -1, stopIndex = -1;
		String str = "Nada!";
		if (t == null) {
			System.err.println("There ain't a damn thing there dude: noTokenException");
		} else {
			
			String tokenType = getTokenType(t.getType());
			
			str = "[" + tokenType + "] ";
			str += t.getText();
			str += " (line:" + t.getLine() + ", pos: " + t.getCharPositionInLine() + " startind:"
					+ t.getTokenStartIndex() + " stopind:" + t.getTokenStopIndex() + " stopline:" + t.getChildCount()
					+ ")";
			CommonToken tokenStart = (CommonToken) t.getToken();
			CommonToken tokenEnd = tokenStart;
			if (t.getChildCount() != 0) {
				CommonTree lastChild = (CommonTree) t.getChild(t.getChildCount() - 1);
				tokenEnd = (CommonToken) lastChild.getToken();
			}
			if (tokenStart == null) {
				System.err.println(str);
				str += "err: ";
			} else {
				startIndex = tokenStart.getStartIndex();
				stopIndex = tokenEnd.getStopIndex();
			}
			// return new DefaultMutableTreeNode(str);
		}
		return new TreeNode(str, new TextRange(startIndex, stopIndex));
	}
	
	private void initCFMLTokenTypes() {
		XMLParser parser = new XMLParser(null, null);
		try {
			Class clazz = Thread.currentThread().getContextClassLoader().loadClass("com.parser.main.cfml.antlr.XMLParser");
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
		XMLParser parser = new XMLParser(null);
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
	
	public void mouseClicked(MouseEvent e) {
		if (tree.getLastSelectedPathComponent() instanceof TreeNode) {
			TreeNode node = (TreeNode) tree.getLastSelectedPathComponent();
			if (node.getTextRange().start != -1 && node.getTextRange().end != -1) {
				// select the corresponding range in the editor
				text.select(node.getTextRange().start, node.getTextRange().end + 1);
				// text.grabFocus();
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}

class TreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;
	TextRange range;
	private boolean isLeaf = false;
	
	public TreeNode(Object obj, TextRange range) {
		super(obj);
		this.range = range;
	}
	
	public TreeNode(Object obj) {
		super(obj);
	}
	
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public boolean isLeaf() {
		return isLeaf;
	}
	
	/**
	 * TODO: change this
	 * 
	 * @param start
	 * @param stop
	 */
	public void setTextRange(Token start, Token stop) {
		int startIndex = -1, stopIndex = -1;
		if (start != null) {
			startIndex = ((CommonToken) start).getStartIndex();
		}
		if (stop != null) {
			stopIndex = ((CommonToken) stop).getStopIndex();
		}
		
		this.range = new TextRange(startIndex, stopIndex);
	}
	
	public TextRange getTextRange() {
		return range;
	}
}

class TextRange {
	public int start;
	public int end;
	
	public TextRange(int start, int end) {
		this.start = start;
		this.end = end;
	}
}