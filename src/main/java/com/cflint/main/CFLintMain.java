package com.cflint.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.xml.transform.TransformerException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.cflint.CFLint;
import com.cflint.HTMLOutput;
import com.cflint.TextOutput;
import com.cflint.XMLOutput;

public class CFLintMain {

	List<String> includeRule = new ArrayList<String>();
	List<String> excludeRule = new ArrayList<String>();
	List<String> folder = new ArrayList<String>();
	String filterFile = null;
	boolean verbose = false;
	boolean xmlOutput = false;
	boolean htmlOutput = true;
	boolean textOutput = false;
	String xmlOutFile = "cflint-result.xml";
	String xmlstyle = "cflint";
	String htmlOutFile = "cflint-result.html";
	String htmlStyle = "plain.xsl";
	String textOutFile = "cflint-result.txt";
	String [] includeCodes = null;
	String [] excludeCodes = null;
	
	public static void main(final String[] args) throws ParseException, IOException, TransformerException {
		final Options options = new Options();
		// add t option
		options.addOption("includeRule", true, "specify rules to include");
		options.addOption("excludeRule", true, "specify rules to exclude");
		options.addOption("folder", true, "folder(s) to scan");
		options.addOption("filterFile", true, "filter file");
		options.addOption("v", false, "verbose");
		options.addOption("ui", false, "show UI");
		options.addOption("verbose", false, "verbose");
		options.addOption("h", false, "display this help");
		options.addOption("help", false, "display this help");
		options.addOption("xml", false, "output in xml format");
		options.addOption("xmlfile", true, "specify the output xml file (default: cflint-results.xml)");
		options.addOption("xmlstyle", true, "cflint,findbugs");
		options.addOption("html", false, "output in html format (default)");
		options.addOption("htmlfile", true, "specify the output html file (default: cflint-results.html)");
		options.addOption("htmlstyle", true, "default,plain");//fancy,fancy-hist,summary
		options.addOption("text", false, "output in plain text");
		options.addOption("textfile", true, "specify the output text file (default: cflint-results.txt)");

		final CommandLineParser parser = new GnuParser();
		final CommandLine cmd = parser.parse(options, args);
		if (cmd.hasOption('h') || cmd.hasOption("help")) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("cflint", options);
			return;
		}

		final CFLintMain main = new CFLintMain();
		main.verbose = (cmd.hasOption('v') || cmd.hasOption("verbose"));
		main.xmlOutput = cmd.hasOption("xml");
		main.textOutput = cmd.hasOption("text");
		if(cmd.hasOption("ui")){
			main.ui();
		}
		// If an output is specified, htmlOutput is not defaulted to true.
		if (main.xmlOutput || main.textOutput) {
			main.htmlOutput = cmd.hasOption("html");
		}

		if (cmd.hasOption("folder")) {
			main.folder.addAll(Arrays.asList(cmd.getOptionValue("folder").split(",")));
		}
		if (cmd.hasOption("htmlstyle")) {
			main.htmlStyle = cmd.getOptionValue("htmlstyle");
			if (!main.htmlStyle.endsWith(".xsl") && !main.htmlStyle.endsWith(".xslt")) {
				main.htmlStyle = main.htmlStyle + ".xsl";
			}
		}
		if (cmd.hasOption("xmlstyle")) {
			main.xmlstyle = cmd.getOptionValue("xmlstyle");
		}
		if (cmd.hasOption("filterFile")) {
			main.filterFile = cmd.getOptionValue("filterFile");
		}
		if (cmd.hasOption("xmlfile")) {
			main.xmlOutFile = cmd.getOptionValue("xmlfile");
		}
		if (cmd.hasOption("htmlfile")) {
			main.htmlOutFile = cmd.getOptionValue("htmlfile");
		}
		if (cmd.hasOption("textfile")) {
			main.textOutFile = cmd.getOptionValue("textfile");
		}
		if (cmd.hasOption("includeRule")) {
			main.includeRule = Arrays.asList(cmd.getOptionValue("includeRule").split(","));
		}
		if (cmd.hasOption("excludeRule")) {
			main.excludeRule = Arrays.asList(cmd.getOptionValue("excludeRule").split(","));
		}
		for (final Option option : cmd.getOptions()) {
			System.out.println("Option " + option.getOpt() + " => " + option.getValue());
		}
		if (main.isValid()) {
			System.out.println("htmloutput?"+main.htmlOutput);
			System.out.println("xmlOutput?"+main.xmlOutput);
			main.execute();
			if(cmd.hasOption("ui")){
				main.open();
			}
		}else{
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("cflint", options);
		}
	}

	private void open() throws IOException {
		if (xmlOutput) {
			Desktop.getDesktop().open(new File(xmlOutFile));
			return;
		}
		if (textOutput) {
			Desktop.getDesktop().open(new File(textOutFile));
			return;
		}
		if (htmlOutput) {
			Desktop.getDesktop().open(new File(htmlOutFile));
			return;
		}
	}

	private void ui() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select target directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			folder.add(chooser.getSelectedFile().getAbsolutePath());
		}else{
			return;
		}
		
		String[] slist = new String[] {"xml", "html", "text"};
		JList list = new JList(slist);
		JOptionPane.showMessageDialog(
		  null, list, "Output Type", JOptionPane.PLAIN_MESSAGE);
		
		int[] indxs = list.getSelectedIndices();
		//If selected set htmlOutput to false
		for(int i=0; i<indxs.length; i++){
			if(indxs[i]==0)
				xmlOutput=true;
			if(indxs[i]==1)
				htmlOutput=true;
			if(indxs[i]==2)
				textOutput=true;
		}
	}

	private void execute() throws IOException, TransformerException {
		final CFLint cflint = new CFLint();
		for (final String scanfolder : folder) {
			cflint.scan(scanfolder);
			// for(BugInfo bi: cflint.getBugs()){
			// System.out.println(bi);
			// }
		}
		if (xmlOutput) {
			System.out.println("Style:" + xmlstyle);
			if("findbugs".equalsIgnoreCase(xmlstyle)){
				System.out.println("Writing findbugs style to " + xmlOutFile);
				new XMLOutput().outputFindBugs(cflint.getBugs(), new FileWriter(xmlOutFile));
			}else{
				System.out.println("Writing " + xmlOutFile);
				new XMLOutput().output(cflint.getBugs(), new FileWriter(xmlOutFile));
			}
		}
		if (textOutput) {
			System.out.println("Writing " + textOutFile);
			new TextOutput().output(cflint.getBugs(), new FileWriter(textOutFile));
		}
		if (htmlOutput) {
			try {
				System.out.println("Writing " + htmlOutFile);
				new HTMLOutput(htmlStyle).output(cflint.getBugs(), new FileWriter(htmlOutFile));
			} catch (final TransformerException e) {
				throw new IOException(e);
			}
		}
		if(includeCodes != null){
			cflint.getBugs().getFilter().includeCode(includeCodes);
		}
		if(excludeCodes != null){
			cflint.getBugs().getFilter().excludeCode(excludeCodes);
		}
	}

	private boolean isValid() {
		if (folder.isEmpty()) {
			System.err.println("Set -scanFolder");
			return false;
		}
		return true;
	}
}
