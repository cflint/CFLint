package com.cflint.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ProgressMonitor;
import javax.xml.transform.TransformerException;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import com.cflint.CFLint;
import com.cflint.HTMLOutput;
import com.cflint.TextOutput;
import com.cflint.XMLOutput;
import com.cflint.tools.CFLintFilter;

public class CFLintTask extends Task {

	boolean showProgress;
	String includeRule;
	String excludeRule;
	File filterFile;
	File xmlFile;
	File htmlFile;
	File textFile;
	String xmlStyle = "cflint";
	String htmlStyle = "plain.xsl";
	String extensions;
	boolean verbose;
	boolean quiet;
	private final List<FileSet> filesets = new ArrayList<FileSet>();

	@Override
	public void execute() {
		try {
			final CFLint cflint = new CFLint();
			cflint.setVerbose(verbose);
			cflint.setQuiet(quiet);
			if (extensions != null && extensions.trim().length() > 0) {
				cflint.setAllowedExtensions(Arrays.asList(extensions.trim().split(",")));
			}
			CFLintFilter filter = CFLintFilter.createFilter();
			if (filterFile != null) {
				final File ffile = filterFile;
				if (ffile.exists()) {
					final FileInputStream fis = new FileInputStream(ffile);
					final byte b[] = new byte[fis.available()];
					fis.read(b);
					filter = CFLintFilter.createFilter(new String(b));
				}
			}
			if (excludeRule != null && excludeRule.trim().length() > 0) {
				final String[] excludeCodes = excludeRule.split(",");
				if (excludeCodes != null && excludeCodes.length > 0) {
					filter.excludeCode(excludeCodes);
				}
			}
			if (includeRule != null && includeRule.trim().length() > 0) {
				final String[] includeCodes = includeRule.split(",");
				if (includeCodes != null && includeCodes.length > 0) {
					filter.excludeCode(includeCodes);
				}
			}
			cflint.getBugs().setFilter(filter);
			if (xmlFile == null && htmlFile == null && textFile == null) {
				xmlFile = new File("cflint-result.xml");
			}
			if (xmlFile != null) {
				if (verbose) {
					System.out.println("Style:" + xmlStyle);
				}
				if ("findbugs".equalsIgnoreCase(xmlStyle)) {
					new XMLOutput().outputFindBugs(cflint.getBugs(), new FileWriter(xmlFile));
				} else {
					new XMLOutput().output(cflint.getBugs(), new FileWriter(xmlFile));
				}
			}
			if (textFile != null) {
				final Writer textwriter = textFile != null ? new FileWriter(textFile) : new OutputStreamWriter(System.out);
				new TextOutput().output(cflint.getBugs(), textwriter);

			}
			if (htmlFile != null) {
				try {
					new HTMLOutput(htmlStyle).output(cflint.getBugs(), new FileWriter(htmlFile));
				} catch (final TransformerException e) {
					throw new IOException(e);
				}
			}
			for (final FileSet fileset : filesets) {
				int progress = 1;
				final DirectoryScanner ds = fileset.getDirectoryScanner(getProject()); // 3
				final ProgressMonitor progressMonitor = showProgress && filesets.size() > 0 ? new ProgressMonitor(null,
						"CFLint", "", 1, ds.getIncludedFilesCount()) : null;
				final String[] includedFiles = ds.getIncludedFiles();
				for (final String includedFile : includedFiles) {
					if(progressMonitor.isCanceled()){
						throw new RuntimeException("CFLint scan cancelled");
					}
					final String filename = ds.getBasedir()+File.separator+includedFile;
					progressMonitor.setNote("scanning " + includedFile);
					cflint.scan(filename);
					progressMonitor.setProgress(progress++);
				}
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String shorten(String includedFile) {
		if (includedFile == null) {
			return "";
		}
		includedFile = includedFile.trim();
		if (includedFile.length() < 20) {
			return includedFile;
		}
		return ".." + includedFile.substring(includedFile.length() - 20);
	}

	public void addFileset(final FileSet fileset) {
		filesets.add(fileset);
	}

	public void setShowProgress(final boolean showProgress) {
		this.showProgress = showProgress;
	}

	public void setIncludeRule(final String includeRule) {
		this.includeRule = includeRule;
	}

	public void setExcludeRule(final String excludeRule) {
		this.excludeRule = excludeRule;
	}

	public void setFilterFile(final File filterFile) {
		this.filterFile = filterFile;
	}

	public void setXmlFile(final File xmlFile) {
		this.xmlFile = xmlFile;
	}

	public void setHtmlFile(final File htmlFile) {
		this.htmlFile = htmlFile;
	}

	public void setTextFile(final File textFile) {
		this.textFile = textFile;
	}

	public void setXmlStyle(final String xmlStyle) {
		this.xmlStyle = xmlStyle;
	}

	public void setHtmlStyle(final String htmlStyle) {
		this.htmlStyle = htmlStyle;
	}

	public void setExtensions(final String extensions) {
		this.extensions = extensions;
	}

	public void setVerbose(final boolean verbose) {
		this.verbose = verbose;
	}

	public void setQuiet(final boolean quiet) {
		this.quiet = quiet;
	}
}
