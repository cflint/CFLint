![CFLint](/src/main/resources/CFLint-logo.jpg)

# CFLint

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.cflint/CFLint/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.cflint/CFLint) [![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/6f4b01d4d2cb4860b60ac666452071f1)](https://www.codacy.com/app/ryaneberly/CFLint?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cflint/CFLint&amp;utm_campaign=Badge_Grade) [![Build Status](https://travis-ci.org/cflint/CFLint.svg?branch=master)](https://travis-ci.org/cflint/CFLint)

A static code analysis tool for CFML.

License: [BSD](http://www.opensource.org/licenses/bsd-license.html)

Current Version: 1.5.x

## Versions

See [CHANGELOG.md](/CHANGELOG.md) for further information.

## Project and library organization

CFLint is a project developed and worked on by volunteers. When logging issues please, be nice and considerate. We're here to help. We really appreciate fixes and improvements, so feel free to talk to us and/or provide pull requests.

`/src/main` contains the source code. Tests can be found in `/src/test`. CFLint relies heavily on the [CFParser](https://github.com/cfparser/cfparser) project as well as a bunch of third-party Java libraries.

The master branch is considered our stable codebase. Most of the development happens in the dev branch resp. local development branches for specific issues.

## Building CFLint

1. Fork the repository into your account and clone or download the codebase as a zip-file.
1. Install the tooling of your choice and build via Gradle or Maven (deprecated). CFLint requires Java 8.

    a. Gradle: execute

        gradlew build

    in the cflint directory

    b. Maven: execute

        mvn clean install

    in the cflint directory

    Alternatively, import the CFLint codebase into the IDE of your choice and use its respectively Gradle/Maven integration. This should work out of the box for Eclipse and IntelliJ users.

## Using CFLint - Quickstart Guide

Get the latest version from [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ccflint) or the [CFLint GitHub release page](https://github.com/cflint/CFLint/releases) or build the project.

If you want to use CFLint from within another Maven project, use:

```xml
<dependency>
    <groupId>com.github.cflint</groupId>
    <artifactId>CFLint</artifactId>
    <version>1.5.0</version>
</dependency>
```

Or always use the latest:

```xml
<dependency>
    <groupId>com.github.cflint</groupId>
    <artifactId>CFLint</artifactId>
    <version>LATEST</version>
</dependency>
```

With the binaries retrieved one or the other way, you can now use CFLint on the command line.

### Use the "-all"-version of the jar-file

    CFLint-1.5.0-all.jar

### Scan a folder with the complete set of rules

    java -jar CFLint-1.5.0-all.jar -folder <baseFolder>

### Scan a file with the complete set of rules

    java -jar CFLint-1.5.0-all.jar -file <fullPathToFile>

### See command line parameters and help

    java -jar CFLint-1.5.0-all.jar -help

# User manual

**Note: This is a work in progress, we're currently collating information from a variety of sources.**

## Introduction

The simplest options for executing CFLint is via the command line. CFLint currently has a UI mode (triggered by -ui on the command line) which will be removed by the latest for CFLint 2.0 - see [Issue #316](https://github.com/cflint/CFLint/issues/316). If you rely on the UI mode, you're unfortunately on your own - no more work will go into this from here onwards.

## Configuration

Alternatively to the command line, you can put `.cflintrc` files into certain directories. Configuring CFLint this way conceptually allows you to run specific rules in specific parts of your application.

CFLint currently supports JSON- and XML-based configuration. XML-based configuration is deprecated in CFLint 1.3.0 and will be removed in CFLint 2.0.

### Rules

When CFLint executes, it scans and parses your code (using CFParser). The syntax tree is then being examined against a set of built-in rules.

In CFLint, those rules are called and implemented as plugins (they live in `/src/main/java/com/cflint/plugins`). By default, all rules will be used against your codebase. This is what a lot of people will do, but using configuration allows you to build a custom scenario to test your code against. See [RULES.md](/RULES.md) for more information on rules and their meaning.

CFLint is opinionated and every release after 1.3.0 will never scan in directories starting with a ```.``` to prevent wasting time of hidden directories such as build configuration, module/library storage or version control information.

### Global configuration

The default and global configuration file is [`/src/main/resources/cflint.definition.json`](/src/main/resources/cflint.definition.json). Common usage of CFLint usually does not require replacing this file.

### Folder-based configuration

Putting a `.cflintrc` file into a directory allows you to specify certain rules that should be executed for this directory and its children. Additionally, you can specify a handful of other properties.

An example `.cflintrc` file is shown below:

```json
{
    "rule": [ ],
    "excludes": [ ],
    "includes": [ {
        "code": "FUNCTION_HINT_MISSING"
    } ],
    "inheritParent": false,
    "parameters": { }
}
```

* `rule` allows you add a plugin for this folder that is not listed in the global configuration. See `ruleImpl` in `cflint.definition.json` for examples.

* `excludes` and `includes` allow you to specify an array of objects describing rules you want to be applied for this directory and its children. In the example above, the only rule to be checked for will be FUNCTION_HINT_MISSING.

* `inheritParent` configures if the rules set in the global or any parent configuration should be inherited as a base set of rules.

* `parameters` allows configuration of rules. See [RULES.md](/RULES.md) for the parameters of each rule and their defaults. You must precede the parameter name with the rule name separated by a dot.

* Please note: `inheritPlugins` and `output` were marked deprecated in CFLint 1.2.0 and removed in 1.4.0. Plugin inheritance is now always treated as true since the team cannot see a use case in which it should be disabled. The output type can be controlled elsewhere, such as command-line flags.

We provide a [schema with the deprecated properties excluded](/src/main/resources/schemas/.cflintrc.schema.json).

See [Recipes](#recipes) for some usage examples of `.cflintrc`. Example files can be found by browsing the [project test files](/src/test/resources/com/cflint/tests).

### Annotation-based configuration

Quite often there are scenarios in which you would generally want to run a certain set of rules against your code but in specific cases need to ignore an otherwise valid violation.

A common example are violations of CFQUERYPARAM_REQ that can't be fixed by applying `<cfqueryparam>` because your DB server doesn't allow params in certain positions (for instance in a `SELECT something FROM #application.config.linkedServerName#.DefaultDatabase.dbo.Comment` scenario). See [Issue #282](https://github.com/cflint/CFLint/issues/282) for more examples.

CFLint offers an annotation-based configuration to deal with this and similar scenarios. Annotations can be placed on the component- or function-level in a CFC or inline with code.

#### Tag-based CFML

    <!---
    @CFLintIgnore SOMETHINGELSE,MISSING_VAR,ANOTHERTHINGTOIGNORE
    --->

#### CFScript

Ignoring all rules on the current line:

    //cflint ignore:line

Ignoring a specific rule (or a comma-separated list of rules) on the current line:

    //cflint ignore:MISSING_VAR

Multiline ignore annotation:

    /*
        @CFLintIgnore SOMETHINGELSE,MISSING_VAR,ANOTHERTHINGTOIGNORE
    */

#### Ignoring within SQL

Within SQL, you can also use

    <!--- @CFLintIgnore CFQUERYPARAM_REQ --->

to ignore a rule violation on the next line.

### Precedence of configuration settings

Configuration of which plugins are run and which rules are included starts with the global configuration and flows through the command line parameters, folder level rules, and down to the annotations within the source.

* global configuration
* custom configuration file  (`-configfile`, we do **not** encourage this option to be used in day-to-day operations of CFLint)
* rule groups (`-rulegroups`,  default behavior is --rulegroups !Experimental)
* includes/excludes from the command line (`-includeRule` and `-excludeRule`)
* .cflintrc - folder level configuration, mostly for including/excluding specific messages
* annotations - explicitly exclude messages in the source code at the tag or line level.

The configuration rule that is closest to the rule is the one that takes effect.

* If an annotation excludes a message, it will not fire regardless of any configuration above it.
* If you exclude a rule at the command line level, but a `.cflintrc` adds it back in, it will fire for source files in that part of the source tree.
* If you are passing in multiple parameters at the command line level, in Windows Powershell the parameters must be included in "double quotes", e.g. `-includeRule "MISSING_VAR,CFQUERYPARAM_REQ"`

## Creating reports

CFLint supports a variety of reporting and output options that you can control via command-line flags. Beyond the targeted output formats of Text, XML, JSON or HTML you can also run CFLint with options for quiet, verbose and debug output.

If no targeted output format is specified at all, CFLint will default to creating an HTML report in the file `cflint-result.html`.

### Execution modes

You can force CFLint's output behavior to stdout and stderr by specifying options for quiet, verbose and debug. If you do not specify either, CFlint will return basic internal information and error output to stdout and stderr.

#### Quiet

Quiet mode (`-quiet <boolean>`) suppresses most of the output CFLint would otherwise create during linting. This might contain actual errors and exceptions but also information like the termination of recursive template parsing or certain configuration issues. Do not run quiet mode if you likely will need assistance with error messages or want to understand better what CFLint is doing.

This is the minimum output mode you can run CFLint in and the feature was originally inspired by [Issue #4](https://github.com/cflint/CFLint/issues/4).

There might be occasional messages from CFParser and ANTLR being pushed into stderr at this stage - even though CFlint runs in quiet mode. This is a known [issue](https://github.com/cflint/CFLint/issues/526) and will be addressed in the future.

#### Verbose

Verbose mode (`-verbose <boolean>`) enables verbose linting output. This contains information on selected output formats and configuration files being found and processes during linting as well as the currently processed file CFLint is working on (showing only files that are actually scanned).

If you want more information about the inner workings of CFLint during execution, verbose mode is the minimum you should run CFLint in.

#### Debug

Debug mode (`-debug <boolean>`) enables debug output. Debug mode implies verbose mode but adds additional information such as the parser tokens and every processed file (regardless of being supported by your or the default extension list) into the output streams.

#### Precedence

It is possible to switch on and run quiet, verbose and debug modes together at the same time. This is partly intended as you might not want to see error information being suppressed by quiet mode, but still want so see certain information being shown in verbose mode. Please take this behavior with a grain of salt though - there might be the odd scenario in which combining `-quiet`, `-verbose` and `-debug` causes unusual output.

The exception is debug mode. In debug mode, CFLint will always ignore user settings for verbose and quiet and set `verbose` to `true` and `quiet` to `false`.

### HTML

The flag `-html` instructs CFLint to create an HTML document. The full syntax is:

    -html -html <outputFileName>

### XML

The flag `-xml` instructs CFLint to create XML. There are two options for XML reporting.

The first option is what we call CFLint XML. It's an internal format that adheres to a basic schema provided [here](/src/main/resources/schemas/cflint-result.xsd). You could then use this format as-is or to do further processing of your choice.

The second option is FindBugs XML. The resulting XML document adheres to the current version of the FindBugs BugCollection [XML Schema Definition](src/main/resources/findbugs/bugcollection.xsd) and can be used in most CI-/Build-Server products. JetBrains TeamCity 10+ can import this format out of the box.

*Please note*: Currently it's not possible to produce BOTH flavors of XML reports at the same time. This is a known limitation. This limitation will be removed as part of CFLint 2.0 (see [Issue #331](https://github.com/cflint/CFLint/issues/331)).

#### CFLint XML

To create CFLint XML provide the following command-line arguments:

    -xml -xmlstyle cflint -xmlfile <outputFileName>

Example of CFLint XML:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<issues version="1.2.1" timestamp="1500107134">
    <issue severity="WARNING" id="CFQUERYPARAM_REQ" message="CFQUERYPARAM_REQ" category="CFLint" abbrev="CR">
        <location file="/Users/kai/Documents/Code/paypal.cfc" fileName="paypal.cfc" function="doSomething" column="0" line="325" message="&lt;cfquery&gt; should use &lt;cfqueryparam/&gt; for variable 'arguments.PaymentType'." variable="arguments.PaymentType">
            <Expression><![CDATA[<cfquery name="doPayment" datasource="#paymentDatasource#">...some more Details...]]></Expression>
        </location>
    </issue>
    <issue severity="WARNING" id="CFQUERYPARAM_REQ" message="CFQUERYPARAM_REQ" category="CFLint" abbrev="CR">
        <location file="/Users/kai/Documents/Code/paypal.cfc" fileName="paypal.cfc" function="doSomethingElse" column="0" line="432" message="&lt;cfquery&gt; should use &lt;cfqueryparam/&gt; for variable 'arguments.something'." variable="arguments.something">
            <Expression><![CDATA[<cfquery name="doPayment" datasource="#paymentDatasource#">...some more Details...]]></Expression>
        </location>
    </issue>
...
    <counts totalfiles="108" totallines="55596">
        <count code="CFQUERYPARAM_REQ" count="39"></count>
        <count severity="WARNING" count="39"></count>
    </counts>
</issues>
```

#### FindBugs XML

To create FindBugs XML provide the following command-line arguments:

    -xml -xmlstyle findbugs -xmlfile <outputFileName>

The FindBugs XML format is currently created using an XSLT document, transforming the CFLint report to FindBugs XML ([`/src/main/resources/findbugs/cflint-to-findbugs.xsl`](/src/main/resources/findbugs/cflint-to-findbugs.xsl)).

### JSON

JSON output can be created with

    -json -jsonfile <outputFileName>

Example of CFLint JSON:

```json
{
    "version" : "1.2.1",
    "timestamp" : 1501202128,
    "issues" : [ {
        "severity" : "ERROR",
        "id" : "MISSING_VAR",
        "message" : "MISSING_VAR",
        "category" : "CFLINT",
        "abbrev" : "MV",
        "locations" : [ {
            "file" : "src/test/resources/com/cflint/tests/Ignores/ignoreCFMLAny2.cfc",
            "fileName" : "ignoreCFMLAny2.cfc",
            "function" : "testFunction",
            "column" : 6,
            "line" : 14,
            "message" : "Variable someVar is not declared with a var statement.",
            "variable" : "someVar",
            "expression" : "someVar"
        } ]
    } ],
    "counts" : {
        "totalFiles" : 7,
        "totalLines" : 49,
        "countByCode" : [ {
            "code" : "MISSING_VAR",
            "count" : 1
        } ],
        "countBySeverity" : [ {
            "severity" : "ERROR",
            "count" : 1
        } ]
    }
}
```

The JSON schema is available [here](/src/main/resources/schemas/cflint-result.schema.json).

### Text

Plain text output can be created with

    -text -textfile <outputFileName>

Example of plain text output:

    Issue
    Severity:WARNING
    Message code:CFQUERYPARAM_REQ
        File:/Users/kai/Documents/Code/paypal.cfc
        Column:0
        Line:79
            Message:<cfquery> should use <cfqueryparam/> for variable 'arguments.something'.
            Variable:'arguments.something' in function:
            Expression:<cfquery name=\"qry\" datasource=\"#variables.dsn#\" cachedwithin=\"#createTimeSpan(0,0,arguments.cacheInMins,0)#\">\r\n...some Details...

    Severity:WARNING
    Message code:CFQUERYPARAM_REQ
        File:/Users/kai/Documents/Code/paypal.cfc
        Column:0
        Line:145
            Message:<cfquery> should use <cfqueryparam/> for variable 'arguments.something'.
            Variable:'arguments.something' in function:
            Expression:<cfquery name=\"qry\" datasource=\"#variables.dsn#\" cachedwithin=\"#createTimeSpan(0,0,arguments.cacheInMins,0)#\">\r\n...some Details...

    ...


    Total files:108
    Total lines:55690

    Issue counts:1
    CFQUERYPARAM_REQ:4

    Total issues:4
    Total warnings:4

## API

To interact directly with CFLint within the JVM use the CFLint API.

```java
import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;

CFLintAPI api = new CFLintAPI();
CFLintResult result = api.scan(filename);
String jsonResult = result.getJSON();
```

## Integration server support

For **Jenkins**, please look at the Jenkins/Hudson plugin mentioned further below.

JetBrains' **TeamCity** has support for FindBugs XML code inspection reports. They can be produced out of the box with CFLint from 1.2.0 onwards (see above in the [FindBugs XML section](#findbugs-xml)).

There is support for **SonarQube** through StepStone's Sonar ColdFusion plugin mentioned further below.

For **Azure DevOps/TFS**, please look at the Azure Pipeline/TFS Build extension mentioned further below.

There's an NPM wrapper for CFLint below. Please be aware that the wrapper seems to come with its own bundled CFLint binary which might not be up-to-date, which is outside of our control.

Other products in the integration/build server category might work, too. If you're using a specific product that works for you with CFLint, please let us know. If you can't get CFLint to work in an environment you use, please let us know as well - we might be able to help.

## IDE support

There are several IDE integrations for CFLint that are available. Below are some brief descriptions, but if you'd like to know more, see [Interesting third-party projects](#interesting-third-party-projects).

There is IDE support for **Sublime Text 3** through a third-party project utilizing SublimeLinter.

There is also support for **Adobe ColdFusion Builder** through a third-party project. Users of CFBuilder, please also see the discussion in [Issue #327](https://github.com/cflint/CFLint/issues/327).

Users of **Atom** can integrate via AtomLinter through a third-party project.

An extension for **Visual Studio Code** is also available as a third-party project.

Support for JetBrains' **IntelliJ** is available as third-party plugin.

## Extending CFLint

### Adding custom rules

```java
package com.cflint.plugins.core;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;
import com.cflint.tools.CFTool;

public class ArgDefChecker extends CFLintScannerAdapter {

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
            for (final CFFunctionParameter argument : function.getFormals()) {
                // handler.addArgument(param.getName());
                final String name = argument.getName();
                if (!argument.toString().contains("required") && !argument.toString().contains("=")) {
                    function.getLine();
                    function.getColumn();
                    context.addMessage("ARG_DEFAULT_MISSING", name);
                }
            }
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals("cfargument")) {
            final String name = element.getAttributeValue("name");
            final boolean required = CFTool.toBoolean(element.getAttributeValue("required"));
            final String defaultExpr = element.getAttributeValue("default");
            if (!required && defaultExpr == null) {
                element.getSource().getRow(element.getBegin());
                element.getSource().getColumn(element.getBegin());
                context.addMessage("ARG_DEFAULT_MISSING", name);
            }
        }
    }
}
```

Looking at the function `element`, the arguments are:

* `element` - the current CFML tag
* `context` - the current file being checked
* `bugs` - the appending object of violations

# Recipes

## Ignoring a directory for processing

The easiest way to achieve this is with a custom `.cflintrc` file:

The `includes` field is ignored if it is an empty list, so simply add a single item to it for which nothing matches.

```json
{
    "code" : "NOTHING"
}
```

or more simply:

```json
    {}
```

The following will ignore all rules in the current folder and below.

```json
{
    "rule" : [ ],
    "excludes" : [ ],
    "includes" : [ {} ],
    "inheritParent" : false,
    "parameters" : {}
}
```

This can be simplified using the default values of a `.cflintrc` file:

```json
{
    "includes" : [{}],
    "inheritParent" : false
}
```

See the discussion in [Issue #290](https://github.com/cflint/CFLint/issues/290) for more info.

## Configuring a parameter

Parameters within the rules can be overridden in the `.cflintrc` files. Use the rule name and the parameter joined with a dot.

```json
{
    "parameters" : {
        "VariableNameChecker.maxLength": "15"
    }
}
```

## Filtering out specific processing results in specific folders

Supply a `cflintexclude.json` file in the command line with the -filterFile argument.

### Examples

To filter out the GLOBAL_VAR messages in the "some\package\location\" folder, add the following to `cflintexclude.json`

#### Windows

    [
        other exclude rules...,
        {"file":".*some\\\\package\\\\location\\\\.*","code":"GLOBAL_VAR"}
    ]

Note: The back slashes must be escaped twice, once for JSON, once for regular expressions

#### \*nix

    [
        other exclude rules...,
        {"file":".*some/package/location/.*","code":"GLOBAL_VAR"}
    ]

# Support

Raise issues here on GitHub and we will look at them.

The [CFML Slack team](http://cfml-slack.herokuapp.com/) has a `#cflint` channel you can join and talk to most of the regular contributors and other users.

# How to contribute

See [CONTRIBUTING.md](/CONTRIBUTING.md) for further information.

# Interesting third-party projects

Please note that the majority of the libraries and projects mentioned here are not directly related to and maintained by the CFLint team. Please see the authors and maintainers of the respective project for support using their libraries first.

* [Jenkins/Hudson plugin](https://github.com/jenkinsci/CFLint-plugin)
* [Sublime Text package](https://github.com/ckaznocha/SublimeLinter-contrib-CFLint)
* [ColdFusion Builder extension](https://github.com/cfjedimaster/CFLint-Extension)
* [Atom package](https://github.com/ditinc/linter-cflint)
* [Visual Studio Code extension](https://github.com/KamasamaK/vscode-cflint)
* [IntelliJ plugin](https://bitbucket.org/SyncHot/cflint/)
* [Azure Pipeline/TFS Build extension](https://marketplace.visualstudio.com/items?itemName=JoshKnutsonExtensions.devops-extension-cflint)
* [SonarQube plugin](https://github.com/stepstone-tech/sonar-coldfusion)
* [NPM wrapper](https://github.com/morgdenn/npm-cflint)
* Vim [Syntastic support for CFLint](https://github.com/cflint/cflint-syntastic)
* [IntelliJ IDEA Ultimate plugin](https://github.com/Pr1st0n/cflint-intellij)

If you have been working on (or are thinking about starting) a project related to CFLint, please let us know. We're happy to include relevant third-party projects to the list above.
