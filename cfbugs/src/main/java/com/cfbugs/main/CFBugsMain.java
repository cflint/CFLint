package com.cfbugs.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.cfbugs.CFBugs;
import com.cfbugs.HTMLOutput;
import com.cfbugs.TextOutput;
import com.cfbugs.XMLOutput;

public class CFBugsMain {

	List<String> includeRule = new ArrayList<String>();
	List<String> excludeRule = new ArrayList<String>();
	List<String> folder = new ArrayList<String>();
	String filterFile = null;
	boolean verbose = false;
	boolean xmlOutput = false;
	boolean htmlOutput = true;
	boolean textOutput = false;
	String xmlOutFile = "cfbugs-result.xml";
	String xmlstyle = "cfbugs";
	String htmlOutFile = "cfbugs-result.html";
	String htmlStyle = "plain.xsl";
	String textOutFile = "cfbugs-result.txt";

	public static void main(final String[] args) throws ParseException, IOException, TransformerException {
		final Options options = new Options();
		// add t option
		options.addOption("includeRule", true, "specify rules to include");
		options.addOption("excludeRule", true, "specify rules to exclude");
		options.addOption("folder", true, "folder(s) to scan");
		options.addOption("filterFile", true, "filter file");
		options.addOption("v", false, "verbose");
		options.addOption("verbose", false, "verbose");
		options.addOption("h", false, "display this help");
		options.addOption("help", false, "display this help");
		options.addOption("xml", false, "output in xml format");
		options.addOption("xmlfile", false, "specify the output xml file (default: cfbugs-results.xml)");
		options.addOption("xmlstyle", true, "cfbugs,findbugs");
		options.addOption("html", false, "output in html format (default)");
		options.addOption("htmlfile", false, "specify the output html file (default: cfbugs-results.html)");
		options.addOption("htmlstyle", true, "default,plain");//fancy,fancy-hist,summary
		options.addOption("text", false, "output in plain text");
		options.addOption("textfile", false, "specify the output text file (default: cfbugs-results.txt)");

		final CommandLineParser parser = new GnuParser();
		final CommandLine cmd = parser.parse(options, args);
		if (cmd.hasOption('h') || cmd.hasOption("help")) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("cfbugs", options);
			return;
		}

		final CFBugsMain main = new CFBugsMain();
		main.verbose = (cmd.hasOption('v') || cmd.hasOption("verbose"));
		main.xmlOutput = cmd.hasOption("xml");
		main.textOutput = cmd.hasOption("text");
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
		if (cmd.hasOption("xmlOutFile")) {
			main.xmlOutFile = cmd.getOptionValue("xmlOutFile");
		}
		if (cmd.hasOption("htmlOutFile")) {
			main.htmlOutFile = cmd.getOptionValue("htmlOutFile");
		}
		if (cmd.hasOption("textOutFile")) {
			main.textOutFile = cmd.getOptionValue("textOutFile");
		}
		for (final Option option : cmd.getOptions()) {
			System.out.println("Option " + option.getOpt() + " => " + option.getValue());
		}
		if (main.isValid()) {
			main.execute();
		}else{
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("cfbugs", options);
		}
	}

	private void execute() throws IOException, TransformerException {
		final CFBugs cfbugs = new CFBugs();
		for (final String scanfolder : folder) {
			cfbugs.scan(scanfolder);
			// for(BugInfo bi: cfbugs.getBugs()){
			// System.out.println(bi);
			// }
		}
		if (xmlOutput) {
			System.out.println("Style:" + xmlstyle);
			if("findbugs".equalsIgnoreCase(xmlstyle)){
				System.out.println("Writing findbugs style to " + xmlOutFile);
				new XMLOutput().outputFindBugs(cfbugs.getBugs(), new FileWriter(xmlOutFile));
			}else{
				System.out.println("Writing " + xmlOutFile);
				new XMLOutput().output(cfbugs.getBugs(), new FileWriter(xmlOutFile));
			}
		}
		if (textOutput) {
			System.out.println("Writing " + textOutFile);
			new TextOutput().output(cfbugs.getBugs(), new FileWriter(textOutFile));
		}
		if (htmlOutput) {
			try {
				System.out.println("Writing " + htmlOutFile);
				new HTMLOutput(htmlStyle).output(cfbugs.getBugs(), new FileWriter(htmlOutFile));
			} catch (final TransformerException e) {
				throw new IOException(e);
			}
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
