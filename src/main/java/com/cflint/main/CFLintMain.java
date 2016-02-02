package com.cflint.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.cflint.CFLint;
import com.cflint.HTMLOutput;
import com.cflint.JSONOutput;
import com.cflint.TextOutput;
import com.cflint.Version;
import com.cflint.XMLOutput;
import com.cflint.config.CFLintConfig;
import com.cflint.config.ConfigUtils;
import com.cflint.tools.CFLintFilter;

public class CFLintMain {

	List<String> folder = new ArrayList<String>();
	String filterFile = null;
	boolean verbose = false;
	boolean logerror = false;
	boolean quiet = false;
	boolean xmlOutput = false;
	boolean jsonOutput = false;
	boolean htmlOutput = true;
	boolean textOutput = false;
	String xmlOutFile = "cflint-result.xml";
	String xmlstyle = "cflint";
	String htmlOutFile = "cflint-result.html";
	String htmlStyle = "plain.xsl";
	String jsonOutFile = "cflint-result.json";
	String textOutFile = null;
	String[] includeCodes = null;
	String[] excludeCodes = null;
	private String extensions;
	boolean showprogress= false;
	boolean progressUsesThread=true;
	private String configfile = null;
	private Boolean stdIn = false;
	private String stdInFile = "source.cfc";
	private Boolean stdOut = false;

	public static void main(final String[] args) throws ParseException, IOException, TransformerException, JAXBException {
		//PropertyConfigurator.configure("/log4j.properties");
		//DOMConfigurator.configure(CFLintFilter.class.getResource("/log4j.xml").getFile());
		//Logger.getLogger("net.htmlparser.jericho");

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
		options.addOption("showprogress", false, "show progress bar");
		options.addOption("singlethread", false, "show progress bar");

		options.addOption("logerror", false, "log parsing errors as bugs");
		options.addOption("e", false, "log parsing errors as bugs");
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
		options.addOption("json", false, "output in json format");
		options.addOption("jsonfile", true, "specify the output json file (default: cflint-results.json)");
		options.addOption("text", false, "output in plain text");
		options.addOption("textfile", true, "specify the output text file (default: cflint-results.txt)");
		options.addOption("extensions", true, "specify the extensions of the CF source files (default: .cfm,.cfc)");
		options.addOption("configfile", true, "specify the location of the config file");
		options.addOption("stdin", true, "use stdin for file input (default: source.cfc)");
		options.addOption("stdout", false, "output to stdout only");


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
		main.logerror = (cmd.hasOption('e') || cmd.hasOption("logerror"));
		main.xmlOutput = cmd.hasOption("xml") || cmd.hasOption("xmlstyle") || cmd.hasOption("xmlfile");
		main.jsonOutput = cmd.hasOption("json") || cmd.hasOption("jsonfile");
		main.textOutput = cmd.hasOption("text") || cmd.hasOption("textfile");
		main.jsonOutput = cmd.hasOption("json") || cmd.hasOption("jsonFile");
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
		if (cmd.hasOption("jsonfile")) {
			main.jsonOutFile = cmd.getOptionValue("jsonfile");
		}
		if (cmd.hasOption("configfile")) {
			main.configfile  = cmd.getOptionValue("configfile");
		}
		if (cmd.hasOption("htmlfile")) {
			main.htmlOutFile = cmd.getOptionValue("htmlfile");
		}
		if (cmd.hasOption("textfile")) {
			main.textOutFile = cmd.getOptionValue("textfile");
		}
		if (cmd.hasOption("jsonfile")) {
			main.jsonOutFile = cmd.getOptionValue("jsonfile");
		}
		if (cmd.hasOption("extensions")) {
			main.extensions = cmd.getOptionValue("extensions");
		}

		if (cmd.hasOption("includeRule")) {
			main.includeCodes = cmd.getOptionValue("includeRule").split(",");
		}
		if (cmd.hasOption("excludeRule")) {
			main.excludeCodes = cmd.getOptionValue("excludeRule").split(",");
		}
		main.showprogress=cmd.hasOption("showprogress") || (!cmd.hasOption("showprogress") && cmd.hasOption("ui"));
		main.progressUsesThread=!cmd.hasOption("singlethread");
		main.stdIn = cmd.hasOption("stdin");
		if (main.stdIn) {
			String stdInOptionValue = cmd.getOptionValue("stdin");
			if (stdInOptionValue != null) {
				main.stdInFile = stdInOptionValue;
			}
		}
		main.stdOut = cmd.hasOption("stdout");
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

		if (htmlOutput) {
			Desktop.getDesktop().open(new File(htmlOutFile));
			return;
		}
		if (jsonOutput) {
			Desktop.getDesktop().open(new File(jsonOutFile));
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

		final String[] slist = new String[] { "xml", "html", "text","txt","json" };
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
			if (indx == 2||indx == 3) {
				textOutput = true;
			}

			if (indx == 4) {
				jsonOutput = true;
			}
		}
	}

	private void execute() throws IOException, TransformerException, JAXBException {
		CFLintConfig config = null;
		if(configfile != null){
			if(configfile.toLowerCase().endsWith(".xml")){
				config = ConfigUtils.unmarshal(new FileInputStream(configfile), CFLintConfig.class);
			}else{
				config = ConfigUtils.unmarshalJson(new FileInputStream(configfile), CFLintConfig.class);
			}

		}
		final CFLint cflint = new CFLint(config);
		cflint.setVerbose(verbose);
		cflint.setLogError(logerror);
		cflint.setQuiet(quiet);
		cflint.setShowProgress(showprogress);
		cflint.setProgressUsesThread(progressUsesThread);
		if(extensions != null && extensions.trim().length() > 0){
			try{
				cflint.setAllowedExtensions(Arrays.asList(extensions.trim().split(",")));
			}catch(Exception e){
				System.err.println("Unable to use extensions (" + extensions + ") using default instead. " + e.getMessage());
			}
		}
		CFLintFilter filter = CFLintFilter.createFilter(verbose);
		if(filterFile != null){
			File ffile = new File(filterFile);
			if(ffile.exists()){
				FileInputStream fis = new FileInputStream(ffile);
				byte b[] = new byte[fis.available()];
				fis.read(b);
				filter = CFLintFilter.createFilter(new String(b),verbose);
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
		}
		if (stdIn) {
			StringBuilder source = new StringBuilder();
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNextLine()) {
				String nextLine = scanner.nextLine();
				source.append(nextLine);
				source.append(System.lineSeparator());
			}
			scanner.close();
			cflint.process(source.toString(), stdInFile);
		}
		if (xmlOutput) {
			Writer xmlwriter = stdOut ? new OutputStreamWriter(System.out) : new FileWriter(xmlOutFile);
			if ("findbugs".equalsIgnoreCase(xmlstyle)) {
				if(verbose) {
					display("Writing XML findbugs style" + (stdOut ? "." : " to " + xmlOutFile));
				}
				new XMLOutput().outputFindBugs(cflint.getBugs(), xmlwriter);
			} else {
				if(verbose) {
					display("Writing XML" + (stdOut ? "." : " to " + xmlOutFile));
				}
				new XMLOutput().output(cflint.getBugs(), xmlwriter);
			}
		}
		if (textOutput) {
			if(textOutFile != null){
				if(verbose) {
					display("Writing text" + (stdOut ? "." : " to " + textOutFile));
				}
			}
			Writer textwriter = stdOut || textOutFile==null ? new OutputStreamWriter(System.out) : new FileWriter(textOutFile);
			new TextOutput().output(cflint.getBugs(), textwriter);

		}
		if (htmlOutput) {
			try {
				if(verbose) {
					display("Writing HTML" + (stdOut ? "." : " to " + htmlOutFile));
				}
				Writer htmlwriter = stdOut ? new OutputStreamWriter(System.out) : new FileWriter(htmlOutFile);
				new HTMLOutput(htmlStyle).output(cflint.getBugs(), htmlwriter);
			} catch (final TransformerException e) {
				throw new IOException(e);
			}
		}
		if (jsonOutput) {
			if(verbose) {
				display("Writing JSON" + (stdOut ? "." : " to " + jsonOutFile));
			}
			Writer jsonwriter = stdOut ? new OutputStreamWriter(System.out) : new FileWriter(jsonOutFile);
			new JSONOutput().output(cflint.getBugs(), jsonwriter);
		}
		if (includeCodes != null) {
			cflint.getBugs().getFilter().includeCode(includeCodes);
		}
		if (excludeCodes != null) {
			cflint.getBugs().getFilter().excludeCode(excludeCodes);
		}
	}

	private void display(String text) {
		if (verbose) {
			System.out.println(text);
		}
	}

	private boolean isValid() {
		if (folder.isEmpty() && !stdIn) {
			System.err.println("Set -scanFolder or -stdin");
			return false;
		}
		return true;
	}
}
