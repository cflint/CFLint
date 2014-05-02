package com.cflint.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

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
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import com.cflint.CFLint;
import com.cflint.HTMLOutput;
import com.cflint.TextOutput;
import com.cflint.Version;
import com.cflint.XMLOutput;
import com.cflint.tools.CFLintFilter;

public class CFLintMain {

	List<String> includeRule = new ArrayList<String>();
	List<String> excludeRule = new ArrayList<String>();
	List<String> folder = new ArrayList<String>();
	String filterFile = null;
	boolean verbose = false;
	boolean quiet = false;
	boolean xmlOutput = false;
	boolean htmlOutput = true;
	boolean textOutput = false;
	String xmlOutFile = "cflint-result.xml";
	String xmlstyle = "cflint";
	String htmlOutFile = "cflint-result.html";
	String htmlStyle = "plain.xsl";
	String textOutFile = null;
	String[] includeCodes = null;
	String[] excludeCodes = null;
	private String extensions;

	public static void main(final String[] args) throws ParseException, IOException, TransformerException {
		//PropertyConfigurator.configure("/log4j.properties");
		DOMConfigurator.configure(CFLintFilter.class.getResource("/log4j.xml").getFile());
		Logger.getLogger("net.htmlparser.jericho");
		
		final Options options = new Options();
		// add t option
		options.addOption("includeRule", true, "specify rules to include");
		options.addOption("excludeRule", true, "specify rules to exclude");
		options.addOption("folder", true, "folder(s) to scan");
		options.addOption("file", true, "file(s) to scan");
		options.addOption("filterFile", true, "filter file");
		options.addOption("v", false, "verbose");
		options.addOption("version", false, "show the version number");
		options.addOption("ui", false, "show UI");
		options.addOption("verbose", false, "verbose");
		options.addOption("q", false, "quiet");
		options.addOption("quiet", false, "quiet");
		options.addOption("h", false, "display this help");
		options.addOption("help", false, "display this help");
		options.addOption("xml", false, "output in xml format");
		options.addOption("xmlfile", true, "specify the output xml file (default: cflint-results.xml)");
		options.addOption("xmlstyle", true, "cflint,findbugs");
		options.addOption("html", false, "output in html format (default)");
		options.addOption("htmlfile", true, "specify the output html file (default: cflint-results.html)");
		options.addOption("htmlstyle", true, "default,plain");// fancy,fancy-hist,summary
		options.addOption("text", false, "output in plain text");
		options.addOption("textfile", true, "specify the output text file (default: cflint-results.txt)");
		options.addOption("extensions", true, "specify the extensions of the CF source files (default: .cfm,.cfc)");

		final CommandLineParser parser = new GnuParser();
		final CommandLine cmd = parser.parse(options, args);
		if (cmd.hasOption('h') || cmd.hasOption("help")) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("cflint", options);
			return;
		}
		if(cmd.hasOption("version")){
			System.out.println("CFLint " + Version.getVersion());
			return;
		}
		final CFLintMain main = new CFLintMain();
		main.verbose = (cmd.hasOption('v') || cmd.hasOption("verbose"));
		main.quiet = (cmd.hasOption('q') || cmd.hasOption("quiet"));
		main.xmlOutput = cmd.hasOption("xml") || cmd.hasOption("xmlstyle") || cmd.hasOption("xmlfile");
		main.textOutput = cmd.hasOption("text") || cmd.hasOption("textfile");
		if (cmd.hasOption("ui")) {
			main.ui();
		}
		// If an output is specified, htmlOutput is not defaulted to true.
		if (main.xmlOutput || main.textOutput) {
			main.htmlOutput = cmd.hasOption("html") || cmd.hasOption("htmlstyle") || cmd.hasOption("htmlfile");
		}

		if (cmd.hasOption("folder")) {
			main.folder.addAll(Arrays.asList(cmd.getOptionValue("folder").split(",")));
		}
		if (cmd.hasOption("file")) {
			main.folder.addAll(Arrays.asList(cmd.getOptionValue("file").split(",")));
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
		if (cmd.hasOption("extensions")) {
			main.extensions = cmd.getOptionValue("extensions");
		}
		
		if (cmd.hasOption("includeRule")) {
			main.includeRule = Arrays.asList(cmd.getOptionValue("includeRule").split(","));
		}
		if (cmd.hasOption("excludeRule")) {
			main.excludeRule = Arrays.asList(cmd.getOptionValue("excludeRule").split(","));
		}
//		for (final Option option : cmd.getOptions()) {
//			if(main.verbose){
//				System.out.println("Option " + option.getOpt() + " => " + option.getValue());
//			}
//		}
		if (main.isValid()) {
			main.execute();
			if (cmd.hasOption("ui")) {
				main.open();
			}
		} else {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("cflint", options);
		}
	}

	private void open() throws IOException {
		if (xmlOutput) {
			Desktop.getDesktop().open(new File(xmlOutFile));
			return;
		}
		if (textOutput && textOutFile != null) {
			Desktop.getDesktop().open(new File(textOutFile));
			return;
		}
		if (htmlOutput) {
			Desktop.getDesktop().open(new File(htmlOutFile));
			return;
		}
	}

	private void ui() {
		final JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select target directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			folder.add(chooser.getSelectedFile().getAbsolutePath());
		} else {
			return;
		}

		final String[] slist = new String[] { "xml", "html", "text" };
		final JList list = new JList(slist);
		JOptionPane.showMessageDialog(null, list, "Output Type", JOptionPane.PLAIN_MESSAGE);

		final int[] indxs = list.getSelectedIndices();
		// If selected set htmlOutput to false
		for (final int indx : indxs) {
			if (indx == 0) {
				xmlOutput = true;
			}
			if (indx == 1) {
				htmlOutput = true;
			}
			if (indx == 2) {
				textOutput = true;
			}
		}
	}

	private void execute() throws IOException, TransformerException {
		final CFLint cflint = new CFLint();
		cflint.setVerbose(verbose);
		cflint.setQuiet(quiet);
		if(extensions != null && extensions.trim().length() > 0){
			try{
				cflint.setAllowedExtensions(Arrays.asList(extensions.trim().split(",")));
			}catch(Exception e){
				System.out.println("Unable to use extensions (" + extensions + ") using default instead. " + e.getMessage());
			}
		}
		CFLintFilter filter = CFLintFilter.createFilter();
		if(filterFile != null){
			File ffile = new File(filterFile);
			if(ffile.exists()){
				FileInputStream fis = new FileInputStream(ffile);
				byte b[] = new byte[fis.available()];
				fis.read(b);
				filter = CFLintFilter.createFilter(new String(b));
			}
		}
				
		if (excludeCodes != null && excludeCodes.length > 0) {
			filter.excludeCode(excludeCodes);
		}
		if (includeCodes != null && includeCodes.length > 0) {
			filter.includeCode(includeCodes);
		}
		cflint.getBugs().setFilter(filter);
		for (final String scanfolder : folder) {
			cflint.scan(scanfolder);
			// for(BugInfo bi: cflint.getBugs()){
			// System.out.println(bi);
			// }
		}
		if (xmlOutput) {
			if(verbose){
				System.out.println("Style:" + xmlstyle);
			}
			if ("findbugs".equalsIgnoreCase(xmlstyle)) {
				if(verbose){
					System.out.println("Writing findbugs style to " + xmlOutFile);
				}
				new XMLOutput().outputFindBugs(cflint.getBugs(), new FileWriter(xmlOutFile));
			} else {
				if(verbose){
					System.out.println("Writing " + xmlOutFile);
				}
				new XMLOutput().output(cflint.getBugs(), new FileWriter(xmlOutFile));
			}
		}
		if (textOutput) {
			if(textOutFile != null){
				if(verbose){
					System.out.println("Writing " + textOutFile);
				}
			}
			Writer textwriter = textOutFile != null?new FileWriter(textOutFile):new OutputStreamWriter(System.out);
			new TextOutput().output(cflint.getBugs(), textwriter);
			
		}
		if (htmlOutput) {
			try {
				if(verbose){
					System.out.println("Writing " + htmlOutFile);
				}
				new HTMLOutput(htmlStyle).output(cflint.getBugs(), new FileWriter(htmlOutFile));
			} catch (final TransformerException e) {
				throw new IOException(e);
			}
		}
		if (includeCodes != null) {
			cflint.getBugs().getFilter().includeCode(includeCodes);
		}
		if (excludeCodes != null) {
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
