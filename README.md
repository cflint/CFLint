![CFLint](/src/main/resources/CFLint-logo.jpg)

CFLint
======

A static code analysis tool for ColdFusion.

License: [BSD](http://www.opensource.org/licenses/bsd-license.html)

Current Version: 1.2.0 (Jul xx 2017)

## Versions

See CHANGELOG.md for further information.

## Project and library organisation

CFLint is a project developed and worked on by volunteers. When logging issues please be nice and considerate, we're here to help. We really appreciate fixes and improvements, feel free to talk to us and/or provide pull requests.

/src/main contains the source code. Tests can be found in /src/test. CFLint relies heavily on the [CFParser](https://github.com/cfparser/cfparser) project as well as a bunch of 3rd party Java libraries.

The master branch is considered our stable codebase. Most of the development happens in the dev branch resp. local development branches for specific issues.
 
## Building CFLint

1. Fork the repository into your account and clone or download the codebase as a zip-file.
2. Build via Gradle or Maven (deprecated). CFlint requires Java 8.

    a. Gradle: execute
        
        gradlew build
        
    in the cflint directory

    b. Maven: execute
    
        mvn clean install
        
    in the cflint directory
    
    Alternatively import the CFLint codebase into the IDE of your choice and use its respectively Gradle/Maven integration. This should work out of the box for Eclipse and IntelliJ users.
    
## Using CFLint
 
### Quickstart

Get the latest version from [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ccflint) or the [CFLint Github release page](https://github.com/cflint/CFLint/releases) or build the project.

Use the "-all"-version of the jar-file: 

    CFLint-1.2.0-all.jar

#### Scan a Folder with the complete set of rules

    java -jar CFLint-1.2.0-all.jar -folder <baseFolder>
    
#### See Parameters

    java -jar CFLint-1.2.0-all.jar -help


 
## How to contribute

The main repository of this project is https://github.com/.... 

Please fork from there, create a local dev branch from origin/dev (named so that it explains the work in the branch), and submit a pull request against the main repository's dev branch. Even better, get in touch with us here on Github before you undertake any work so that it can be coordinated with what we're doing.

















Check out the new [wiki!](https://github.com/cflint/CFLint/wiki) where you can:

  * [Quick Start](https://github.com/cflint/CFLint/wiki/Quick-Start)
  * [Learn how to install CFLint locally](https://github.com/cflint/CFLint/wiki/How-Do-I-Install-This-Tool%3F)
  * [Built in Rules](https://github.com/cflint/CFLint/wiki/Built-In-Rules)
  * [Learn how to include/exclude rules](https://github.com/cflint/CFLint/wiki/Include-Exclude-Rules-Using-Exteral-XML-File)
  * [Learn CFLint from the command line](https://github.com/cflint/CFLint/wiki/How-Do-I-Use-This-Tool%3F)
  * [Get the library from Maven](https://github.com/cflint/CFLint/wiki/Get-the-library-from-Maven)
  * [Ignore Specific Flags](https://github.com/cflint/CFLint/wiki/Ignoring-Specific-Flags-In-Code)
  * [Suggest a feature](https://github.com/cflint/CFLint/wiki/How-Do-I-Suggest-Features%3F)


See the [Jenkins](http://jenkins-ci.org/)/Hudson plugin [here](https://github.com/jenkinsci/CFLint-plugin).

Someone has created a [SublimeLinter](http://www.sublimelinter.com) plugin [here](https://github.com/ckaznocha/SublimeLinter-contrib-CFLint).

Ray Camden created a [CFBuilder](http://www.adobe.com/products/coldfusion-builder.html) plugin [here](https://github.com/cfjedimaster/CFLint-Extension) and blogs about CFLint [here](http://www.raymondcamden.com/2014/7/31/Linting-your-ColdFusion-code#more).

Sonar plugin [here](https://github.com/stepstone-tech/sonar-coldfusion)

Vim [Syntastic](https://github.com/scrooloose/syntastic) support for CFML is available [here](https://github.com/cflint/cflint-syntastic).

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6f4b01d4d2cb4860b60ac666452071f1)](https://www.codacy.com/app/ryaneberly/CFLint?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cflint/CFLint&amp;utm_campaign=Badge_Grade)

[![Build Status](https://travis-ci.org/cflint/CFLint.svg?branch=master)](https://travis-ci.org/cflint/CFLint)
