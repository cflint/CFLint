![CFLint](/src/main/resources/CFLint-logo.jpg)

CFLint
======

A static code analysis tool for ColdFusion.

License: [BSD](http://www.opensource.org/licenses/bsd-license.html)

Current Version: 1.2.0 (Jul xx 2017)

# Versions

See CHANGELOG.md for further information.

# Project and library organisation

CFLint is a project developed and worked on by volunteers. When logging issues please be nice and considerate, we're here to help. We really appreciate fixes and improvements, feel free to talk to us and/or provide pull requests.

/src/main contains the source code. Tests can be found in /src/test. CFLint relies heavily on the [CFParser](https://github.com/cfparser/cfparser) project as well as a bunch of 3rd party Java libraries.

The master branch is considered our stable codebase. Most of the development happens in the dev branch resp. local development branches for specific issues.
 
# Building CFLint

1. Fork the repository into your account and clone or download the codebase as a zip-file.
2. Install the tooling of your choice and build via Gradle or Maven (deprecated). CFlint requires Java 8.

    a. Gradle: execute
        
        gradlew build
        
    in the cflint directory

    b. Maven: execute
    
        mvn clean install
        
    in the cflint directory
    
    Alternatively import the CFLint codebase into the IDE of your choice and use its respectively Gradle/Maven integration. This should work out of the box for Eclipse and IntelliJ users.
    
# Using CFLint - Quickstart

Get the latest version from [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ccflint) or the [CFLint Github release page](https://github.com/cflint/CFLint/releases) or build the project.

If you want to use CFLint from within another Maven project, use:

    <dependency>
        <groupId>com.github.cflint</groupId>
        <artifactId>CFLint</artifactId>
        <version>1.2.0</version>
    </dependency>

Or always use the latest:

    <dependency>
        <groupId>com.github.cflint</groupId>
        <artifactId>CFLint</artifactId>
        <version>LATEST</version>
    </dependency>
    
With the binaries retrieved one or the other way, you can now use CFLint on the command line.

#### Use the "-all"-version of the jar-file: 

    CFLint-1.2.0-all.jar

#### Scan a folder with the complete set of rules:

    java -jar CFLint-1.2.0-all.jar -folder <baseFolder>

#### Scan a folder with the complete set of rules:
    
    java -jar CFLint-1.2.0-all.jar -file <fullPathToFile>
        
#### See parameters and help:

    java -jar CFLint-1.2.0-all.jar -help

# User manual

**Note: This is a work in progress, we're currently collating information from a variety of sources.**

## Introduction

The most simple options for executing CFLint is via the command line. CFLint currently has a UI mode (triggered by -ui on the command line) which will be removed by the latest for CFLint 2.0 - see [issue 316](https://github.com/cflint/CFLint/issues/316). If you rely on the UI mode, you're unfortunately on your own - no more work will go into this from here onwards.

## Configuration

Alternatively to the commnand line you can supply a global configuration via the -config switch or put .cflintrc files into certain directories. Configuring CFLint this way conceptually allows you to run specific rules in specific parts of your application.

### Rules

When CFLint executes, it scans and parses your code (using CFParser). The syntax tree is then being examined against a set of built-in rules. In CFLint those rules are called and implemented as plugins (they live in /src/main/java/com/cflint/plugins). By default all rules will be used against your codebase. This is what a lot of people will do, but using configuration allows you to build a custom scenario to test your code against.

### Global configuration

The -configfile options can be used to replace the standard global configuration file.

The standard configuration is src/main/resources/cflint.definition.json.  Normal usage of CFLint does not normally require replacing this file.

### Folder-based configuration

Putting a .cflintrc file into a directory allows you to specify certain rules that should be executed for this directory and its children. Additionally you can specify a handful of other properties.

An example .cflintrc file is shown below:
 
     {
       "output" : [ ],
       "rule" : [ ],
       "excludes" : [ ],
       "includes" : [ {
         "code" : "FUNCTION_HINT_MISSING"
       } ],
       "inheritParent" : false,
       "inheritPlugins" : true
     }

- rule allows you add a plugin for this folder that is not listed in the global configuration.  See ruleImpl in cflint.definition.json for examples.  

- excludes and includes allow you to specify an array of objects describing rules you want to be applied for this directory and its children. In the example above, the only rule to be checked for will be FUNCTION_HINT_MISSING. The messageText and severity properties allow you to customise those values for this specific part of your CFLint run. 

- inheritParent configures if the rules set in the global or any parent configuration should be inherited as a base set of rules. 

- Please note: inheritPlugins and output have been marked deprecated in CFLint 1.2.0 and will be removed in 1.3.0. If you are using .cflintrc files now, please remove the inheritPlugins and output property as soon as possible. Plugin inheritance will going forward always be treated as true, the team can not see a use case in which it should be disabled.  The value of the output attribute is ignored.

### Annotation-based configuration

Annotation-based configuration allows you to ignore ... more to come from Kai

### Precendence of configuration settings

Configuration of which plugins are run and which rules are included starts with the global configuration and flows through the command line parameters, folder level rules, and down to the annotations within the source.

- global configuration
- custom configuration file  (--configfile)
- rule groups (--rulegroups,  default behaviour is --rulegroups !Experimental)
- includes/excludes from the command line (--includeRule and --excludeRule)
- .cflintrc - folder level configuration, mostly for including/excluding specific messages
- annotations - explicitly exclude messages in the source code at the tag or line level.

The configuration rule that is closest to the rule is the one that takes effect.
* If an annotation excludes a message, it will not fire regardless of any configuration above it.
* If you exclude a rule at the command line level, but a .cflintrc adds it back in, it will fire for source files in that part of the source tree.


## Creating reports

More to come on this and the subsections from Kai

### XML

#### CFLint XML

#### Findbugs XML

### JSON

### Text




## Integration server support

For Jenkins, please look at the Jenkins/Hudson plugin mentioned further below. 

Jetbrains' TeamCity has support for Findbugs XML code inspection reports. They can be produced out of the box with CFLint from 1.2.0 onwards (see above in the Findbugs XML section).

There is support for SonarQube through Stepstone's Sonar ColdFusion plugin mentioned further below.

Other products in the integeration/build server category might work, too. If you're using a specific product that works for you with CFLint please let us know. If you can't get CFLint to work in an environment you use, please let us know as well - we might be able to help.



## IDE support

Currently there is IDE support for Sublime through a 3rd-party project (see below). 

There is also support for Adobe's CFBuilder through a 3rd-party project (see below). Users of CFBuilder, please also see the discussion in issue [#327](https://github.com/cflint/CFLint/issues/327).

Support for Jetbrains' IntelliJ is planned; talk to [@TheRealAgentK](https://github.com/TheRealAgentK) for more info if you're interested.

## Extending CFLint

### Adding custom rules

Some stuff from the Wiki to come

# Receipes

## Ignoring a directory for processing

The easiest way to achieve this is with a custom .cflintrc file:

The includes field is ignored if it is an empty list, so simply add a single item to it for which nothing matches.

    {
        "code" : "NOTHING"
    }

or simply:

    {}


The following will ignore all rules in the current folder and below.

    {
      "output" : [ ],
      "rule" : [ ],
      "excludes" : [ ],
      "includes" : [ {}],
      "inheritParent" : false,
      "inheritPlugins" : true
    }


This can be simplified using the default values of a .cflintrc file:

    {
      "includes" : [{}],
      "inheritParent" : false
    }

See the discussion in [#290](https://github.com/cflint/CFLint/issues/290) for more info.


## Filtering out specific processing results in specific folders

Supply a cflintexclude.json file in ???  
---TO DO START  
Where would such a file go?  
---TO DO END  

#### Example

To filter out the GLOBAL_VAR messages in the "some\package\location\" folder, add the following to cflintexclude.json</p>

**Windows**

    [
        other exclude rules...,
        {"file":".*some\\\\package\\\\location\\\\.*","code":"GLOBAL_VAR"}
    ]

Note: The back slashes must be escaped twice, once for JSON, once for regular expressions

**\*nix**

    [
        other exclude rules...,
        {"file":".*some/package/location/.*","code":"GLOBAL_VAR"}
    ]


# Support

Raise issues here on Github and we will look at them.

The [CFML Slack team](http://cfml-slack.herokuapp.com/) has a #cflint channel you can join and talk to most of the regular contributors and other users.


# How to contribute?

The main repository of this project is https://github.com/cflint/CFLint.

Please fork from there, create a local dev branch from origin/dev (named so that it explains the work in the branch), and submit a pull request against the main repository's dev branch. Even better, get in touch with us here on Github before you undertake any work so that it can be coordinated with what we're doing.

If you're interested in contributing on a regular basis, get in touch with [Ryan](https://github.com/ryaneberly) and we can add you to the internal CFLint Slack team.


# Interesting 3rd-party projects

Please note that the majority of the libraries and projects mentioned here are not directly related and maintainedby the CFLint team. Please see the authors and maintainers of those projects for support using their libraries first.

- [Jenkins/Hudson plugin](https://github.com/jenkinsci/CFLint-plugin) for CFLint
- [SublimeLinter plugin](https://github.com/ckaznocha/SublimeLinter-contrib-CFLint) for CFlint
- [CFBuilder plugin](https://github.com/cfjedimaster/CFLint-Extension) for CFLint
- [Sonar plugin](https://github.com/stepstone-tech/sonar-coldfusion)
- Vim [Syntastic support for CFLint](https://github.com/cflint/cflint-syntastic)














---TO DO START 

Get rid of / refactor content...

Check out the new [wiki!](https://github.com/cflint/CFLint/wiki) where you can:


  * [Built in Rules](https://github.com/cflint/CFLint/wiki/Built-In-Rules)
  * [Learn how to include/exclude rules](https://github.com/cflint/CFLint/wiki/Include-Exclude-Rules-Using-Exteral-XML-File)
  * [Learn CFLint from the command line](https://github.com/cflint/CFLint/wiki/How-Do-I-Use-This-Tool%3F)
  * [Get the library from Maven](https://github.com/cflint/CFLint/wiki/Get-the-library-from-Maven)
  * [Ignore Specific Flags](https://github.com/cflint/CFLint/wiki/Ignoring-Specific-Flags-In-Code)


---TO DO END



[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6f4b01d4d2cb4860b60ac666452071f1)](https://www.codacy.com/app/ryaneberly/CFLint?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cflint/CFLint&amp;utm_campaign=Badge_Grade)

[![Build Status](https://travis-ci.org/cflint/CFLint.svg?branch=master)](https://travis-ci.org/cflint/CFLint)
