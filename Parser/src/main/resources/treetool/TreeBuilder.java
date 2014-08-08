/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package treetool;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenRewriteStream;

import cfml.parsing.cfml.antlr.XMLLexer;
import cfml.parsing.cfml.antlr.XMLParser;

/**
 * Helper class to build trees representing the parsing process of a grammar.
 * 
 * @author Yang Jiang (yang.jiang.z@gmail.com)
 * 
 */
public class TreeBuilder extends JPanel implements MouseListener, ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JTree tree;
	private JPopupMenu popup;
	private JTextPane editor;
	private JMenuBar menuBar;
	private JFileChooser fileChooser;
	
	Stack<TreeNode> stacks = new Stack<TreeNode>();
	String sourceFile;
	String fileCotent;
	TreeNode currentParent;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Source File Tree Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		TreeBuilder treeBuilder = new TreeBuilder();
		// Add content to the window.
		frame.add(treeBuilder);
		frame.setJMenuBar(treeBuilder.menuBar);
		if (args.length != 0) {
			treeBuilder.showFile(args[0]);
		} else {
			File bar = new File("./src/cfml/parsing/cfml/antlr/input3");
			// File bar = new File("./src/cfml/parsing/cfml/test1.cfm");
			String path = bar.getAbsolutePath();
			treeBuilder.showFile(path);
		}
		
		// Display the window.
		frame.pack();
		frame.setSize(800, 500);
		frame.setVisible(true);
		// tree.addTreeSelectionListener(this);
		
	}
	
	public TreeBuilder() {
		super(new GridLayout(1, 0));
		
		// left, tree
		createTree();
		
		JScrollPane treeView = new JScrollPane(tree);
		treeView.setMinimumSize(new Dimension(400, 200));
		
		// right, editor
		editor = new JTextPane();
		editor.setEditable(false);
		JScrollPane editorView = new JScrollPane(editor);
		editorView.setMinimumSize(new Dimension(350, 200));
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(treeView);
		splitPane.setRightComponent(editorView);
		add(splitPane);
		createMenu();
		fileChooser = new JFileChooser();
	}
	
	private void createTree() {
		tree = new JTree(new DefaultMutableTreeNode("Choose a java source file."));
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		popup = new JPopupMenu();
		JMenuItem mi = new JMenuItem("Expand all children");
		mi.addActionListener(this);
		mi.setActionCommand("expand");
		popup.add(mi);
		
		mi = new JMenuItem("Collapse all children");
		mi.addActionListener(this);
		mi.setActionCommand("collapse");
		popup.add(mi);
		popup.setOpaque(true);
		popup.setLightWeightPopupEnabled(true);
		tree.addMouseListener(this);
	}
	
	private void createMenu() {
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		JMenuItem menuItem = new JMenuItem("Open File", KeyEvent.VK_O);
		menuItem.setActionCommand("openfile");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		menu.add(menuItem);
	}
	
	/**
	 * Set the current parent of the node. Any node added after this call we be counted as children of the newParent.
	 * 
	 * @param newParent
	 * @return
	 */
	public TreeNode setCurrentParent(TreeNode newParent) {
		TreeNode oldParent = currentParent;
		currentParent = newParent;
		return oldParent;
	}
	
	public TreeNode getCurrentParent() {
		return currentParent;
	}
	
	/**
	 * Add a child to the currentParent set by calling setCurrentParent().
	 * 
	 * @param name
	 * @return
	 */
	public TreeNode addNode(String name, Token start, Token stop) {
		int startIndex = -1, stopIndex = -1;
		if (start != null) {
			startIndex = ((CommonToken) start).getStartIndex();
		}
		if (stop != null) {
			stopIndex = ((CommonToken) stop).getStopIndex();
		}
		
		TreeNode node = new TreeNode(name, new TextRange(startIndex, stopIndex));
		if (currentParent != null)
			currentParent.add(node);
		node.setLeaf(false);
		return node;
	}
	
	public TreeNode addNode(String name) {
		TreeNode node = new TreeNode(name);
		node.setLeaf(false);
		if (currentParent != null)
			currentParent.add(node);
		
		return node;
	}
	
	/**
	 * Same as addNode(), except that a leaf should contain no child.
	 * 
	 * @param name
	 * @return
	 */
	public TreeNode addLeaf(String name, Token token) {
		TreeNode node = new TreeNode(name, new TextRange(((CommonToken) token).getStartIndex(),
				((CommonToken) token).getStopIndex()));
		node.setLeaf(true);
		if (currentParent != null)
			currentParent.add(node);
		return node;
	}
	
	/**
	 * Stores the current parent in the stack, this is usually called before setting a new parent.
	 */
	public void pushTop() {
		stacks.push(currentParent);
	}
	
	/**
	 * Restores the previous parent.
	 */
	public TreeNode popTop() {
		TreeNode ret = currentParent;
		currentParent = stacks.pop();
		return ret;
	}
	
	public void showFile(String file) {
		loadFile(file);
		editor.setText(fileCotent);
		buildTree();
		this.revalidate();
	}
	
	private void loadFile(String file) {
		FileInputStream in = null;
		this.sourceFile = file;
		fileCotent = null;
		try {
			in = new FileInputStream(file);
			byte[] buf = new byte[10240];
			StringBuffer sbuf = new StringBuffer(buf.length);
			int len = in.read(buf);
			while (len > 0) {
				sbuf.append(new String(buf, 0, len));
				len = in.read(buf);
			}
			fileCotent = sbuf.toString();
		} catch (Exception e) {
			fileCotent = null;
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
		
	}
	
	private void buildTree() {
		TreeNode root = new TreeNode("compilationUnit [" + sourceFile + "]", new TextRange(0, fileCotent.length() - 1));
		try {
			XMLLexer lex = new XMLLexer(new ANTLRStringStream(fileCotent));
			TokenRewriteStream tokens = new TokenRewriteStream(lex);
			XMLParser g = new XMLParser(tokens);
			// g.setTreeBuilder(this);
			this.setCurrentParent(root);
			g.compilationUnit();
			// if (g.getNumberOfSyntaxErrors() != 0) {
			// root = new TreeNode("There are syntax error in input file. [source:" + sourceFile + "]", new TextRange(
			// -1, -1));
			// }
		} catch (Exception e) {
			e.printStackTrace();
			root = new TreeNode("There are syntax error in input file. [source:" + sourceFile + "]", new TextRange(-1,
					-1));
		}
		
		((DefaultTreeModel) tree.getModel()).setRoot(root);
		tree.expandRow(0);
	}
	
	public void mouseClicked(MouseEvent e) {
		if (tree.getLastSelectedPathComponent() instanceof TreeNode) {
			TreeNode node = (TreeNode) tree.getLastSelectedPathComponent();
			if (node.getTextRange().start != -1 && node.getTextRange().end != -1) {
				// select the corresponding range in the editor
				editor.select(node.getTextRange().start, node.getTextRange().end + 1);
				editor.grabFocus();
			}
		}
	}
	
	public void mouseEntered(MouseEvent e) {
	}
	
	public void mouseExited(MouseEvent e) {
	}
	
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger() && tree.getLastSelectedPathComponent() != null) {
			popup.show((JComponent) e.getSource(), e.getX(), e.getY());
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger() && tree.getLastSelectedPathComponent() != null) {
			popup.show((JComponent) e.getSource(), e.getX(), e.getY());
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("expand".equals(cmd) || "collapse".equals(cmd)) {
			TreeNode node = (TreeNode) tree.getLastSelectedPathComponent();
			if (node == null) {
				return;
			}
			int row = -1;
			if (tree.getSelectionRows() != null) {
				row = tree.getSelectionRows()[0];
			}
			if (row == -1) {
				return;
			}
			
			if ("expand".equals(cmd)) {
				for (int i = row; i < tree.getRowCount(); i++) {
					tree.expandRow(i);
				}
			} else if ("collapse".equals(cmd)) {
				for (int i = tree.getRowCount() - 1; i >= row; i--) {
					tree.collapseRow(i);
				}
			}
		} else if ("openfile".equals(cmd)) {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				this.showFile(file.getAbsolutePath());
			}
		}
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
