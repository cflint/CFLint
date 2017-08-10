History
=======

## Overview of major changes in CFLint 1.2.0

### Parsing

* Numerous fixes for parsing CFML code, update to CFParser 2.4.10
* Upgrade to ANTLR 4.7

### Linting

* Bugfixes for rule processing
* Added annotation-based ignoring of rules inline in code.
* JSON-based configuration has undergone a few changes and configuration properties have been deprecated.

### Output

* Support for -showStats has been removed - scanning statistics are now always produced and displayed/included 
* Findbugs XML output now matches the requirements for Findbugs' bugcollection.xsd and has undergone major changes from earlier versions.
* CFLint XML output has some additional attributes for some XML elements in the output structure (no breaking changes)
* JSON output has undergone a rework of the existing data structure to cater for the output of additional information (breaking changes)


## CFLint1.2.0 
##### GitHub [#152](https://github.com/cflint/CFLint/issues/152) UNUSED_METHOD_ARGUMENT ignores scoped arguments
##### GitHub [#158](https://github.com/cflint/CFLint/issues/158) multiple location members in JSON output
##### GitHub [#171](https://github.com/cflint/CFLint/issues/171) XML Output : MalformedByteSequenceException: Invalid byte 1 of 1-byte UTF-8 sequence
##### GitHub [#173](https://github.com/cflint/CFLint/issues/173) Exclude rule for all INFO severity messages, excludes all messages
##### GitHub [#179](https://github.com/cflint/CFLint/issues/179) CFLint - wrong line number in XML report
##### GitHub [#182](https://github.com/cflint/CFLint/issues/182) Named arguments in functions
##### GitHub [#185](https://github.com/cflint/CFLint/issues/185) configfile excludes rules
##### GitHub [#186](https://github.com/cflint/CFLint/issues/186) Doesn&#39;t seem to be checking missing_var on cfscript cfcs
##### GitHub [#192](https://github.com/cflint/CFLint/issues/192) Directory Scan
##### GitHub [#194](https://github.com/cflint/CFLint/issues/194) Config system for includes/excludes is misleading (or broken)
##### GitHub [#195](https://github.com/cflint/CFLint/issues/195) Support a way to ignore found issues in the future.
##### GitHub [#197](https://github.com/cflint/CFLint/issues/197) False positive: structure keys reported as variables without scope prefix
##### GitHub [#198](https://github.com/cflint/CFLint/issues/198) New rule for specifying structure key names in quotation marks
##### GitHub [#199](https://github.com/cflint/CFLint/issues/199) Rules reporting missing hints should work with script-style code
##### GitHub [#208](https://github.com/cflint/CFLint/issues/208) Incorrect line number in XML export when previously scanned CFML-based file
##### GitHub [#211](https://github.com/cflint/CFLint/issues/211) False positive variable used in loop reported as unused
##### GitHub [#212](https://github.com/cflint/CFLint/issues/212) Where does cflint-ui come from?
##### GitHub [#220](https://github.com/cflint/CFLint/issues/220) Linter does not check code inside cfscript try/catch/finally blocks
##### GitHub [#223](https://github.com/cflint/CFLint/issues/223) Duplicate &quot;Temporary variable could be named better&quot; warnings
##### GitHub [#243](https://github.com/cflint/CFLint/issues/243) Parse error when multiple spaces in CFML tag
##### GitHub [#253](https://github.com/cflint/CFLint/issues/253) CFML analysis tries to parse tags inside strings/literals
##### GitHub [#254](https://github.com/cflint/CFLint/issues/254) Variable Prefix and Postfix - More dynamic
##### GitHub [#256](https://github.com/cflint/CFLint/issues/256) Var &#39;declared&#39; with local scope.
##### GitHub [#257](https://github.com/cflint/CFLint/issues/257) Does not see argument as used when it is used in brackets inside of function.
##### GitHub [#260](https://github.com/cflint/CFLint/issues/260) Default Rules merging with config.xml
##### GitHub [#262](https://github.com/cflint/CFLint/issues/262) feaure: flag use of isDate() 
##### GitHub [#265](https://github.com/cflint/CFLint/issues/265) Identifier cfcatch is global, referencing in a CFC or function should be avoided.
##### GitHub [#267](https://github.com/cflint/CFLint/issues/267) Is it possible for dot paths to alert when case doesn&#39;t match
##### GitHub [#269](https://github.com/cflint/CFLint/issues/269) Unclear error messages: Error in plugin, 0
##### GitHub [#271](https://github.com/cflint/CFLint/issues/271) Findbugs XML output creates &lt;?xml...?&gt; tag twice
##### GitHub [#272](https://github.com/cflint/CFLint/pull/272) Fix for #271
##### GitHub [#278](https://github.com/cflint/CFLint/issues/278) Additions to CFLint XML (was: Building a mechanism to pass timestamps and additional counters into FIndbugs XML)
##### GitHub [#282](https://github.com/cflint/CFLint/issues/282) CFQUERYPARAM_REQ false positives
##### GitHub [#284](https://github.com/cflint/CFLint/issues/284) MISSING_VAR false positive cfquery loops
##### GitHub [#285](https://github.com/cflint/CFLint/issues/285) Script ignore syntax fails in mixed cfscript/tag code
##### GitHub [#290](https://github.com/cflint/CFLint/issues/290) How to use .cflintrc to ignore a directory?
##### GitHub [#291](https://github.com/cflint/CFLint/issues/291) Empty files report FILE_ERROR
##### GitHub [#293](https://github.com/cflint/CFLint/issues/293) Stack overflow in dev branch
##### GitHub [#295](https://github.com/cflint/CFLint/issues/295) Allowing to ignore CFQUERYPARAM_REQ with SQL code
##### GitHub [#296](https://github.com/cflint/CFLint/pull/296) Retrospective fix for test case for #282 (and reopened #195)
##### GitHub [#299](https://github.com/cflint/CFLint/issues/299) Move old bug count logic for output into cflintstats
##### GitHub [#300](https://github.com/cflint/CFLint/issues/300) XML output with findbugs style should have -showStats set implicitly
##### GitHub [#301](https://github.com/cflint/CFLint/issues/301) HTML outputs are broken
##### GitHub [#303](https://github.com/cflint/CFLint/issues/303) Findbugs: totalsize should be lines of code scanned
##### GitHub [#315](https://github.com/cflint/CFLint/issues/315) inheritPlugins in .cflintrc should be always true 
##### GitHub [#318](https://github.com/cflint/CFLint/issues/318) JSON output questions (int/string and locations array)
##### GitHub [#321](https://github.com/cflint/CFLint/pull/321) 290 parse error
##### GitHub [#323](https://github.com/cflint/CFLint/issues/323) Various smaller issues with output 
##### GitHub [#326](https://github.com/cflint/CFLint/issues/326) CFLint reporting a column that doesn&#39;t exist
##### GitHub [#330](https://github.com/cflint/CFLint/issues/330) DefaultCFLintMarshaller doesn&#39;t have any of the CFLint XML improvements in XMLOutput
##### GitHub [#336](https://github.com/cflint/CFLint/issues/336) AVOID_EMPTY_FILES being reported if file doesn&#39;t exist
##### GitHub [#339](https://github.com/cflint/CFLint/issues/339) Rule UNUSED_LOCAL_VARIABLE has issues with several tags
##### GitHub [#340](https://github.com/cflint/CFLint/issues/340) GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN triggered on CF_SQL_
##### GitHub [#341](https://github.com/cflint/CFLint/pull/341) Fix for test fail on case-insensitive filesystems
##### GitHub [#343](https://github.com/cflint/CFLint/pull/343) Lighten ANTLR dependencies
##### GitHub [#344](https://github.com/cflint/CFLint/issues/344) GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN triggered by built in function arguments
##### GitHub [#345](https://github.com/cflint/CFLint/issues/345) GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN falsely indicated in CFM files with &lt;cfinclude&gt;
##### GitHub [#347](https://github.com/cflint/CFLint/issues/347) PARSE_ERROR on component with a name coinciding with one instantiated elsewhere
##### GitHub [#348](https://github.com/cflint/CFLint/pull/348) Update to cfparser 2.4.9
##### GitHub [#352](https://github.com/cflint/CFLint/issues/352) Document .cflintrc schema
##### GitHub [#353](https://github.com/cflint/CFLint/pull/353) Dev
##### GitHub [#356](https://github.com/cflint/CFLint/issues/356) Remove and deprecate &quot;output&quot; on top-level of .cflintrc
##### GitHub [#357](https://github.com/cflint/CFLint/issues/357) PARSE_ERROR when cfloop has &quot;delimiters&quot; attribute
##### GitHub [#359](https://github.com/cflint/CFLint/issues/359) PARSE_ERROR when concatenating strings with single quotes
##### GitHub [#361](https://github.com/cflint/CFLint/issues/361) Add contributing guidelines document
##### GitHub [#362](https://github.com/cflint/CFLint/issues/362) Config file usage
##### GitHub [#363](https://github.com/cflint/CFLint/issues/363) Maven build fails using the ZIP file from GitHub since it needs the `.git` folder
##### GitHub [#366](https://github.com/cflint/CFLint/issues/366) ANTLR Tool version 4.6 mismatch
##### GitHub [#370](https://github.com/cflint/CFLint/pull/370) #323 - Changing JSON structure and changing expected test results
## CFLint-1.0.1 
##### GitHub [#103](https://github.com/cflint/CFLint/issues/103) Enhancement: Enable default config file when parsing similar to other linters
##### GitHub [#114](https://github.com/cflint/CFLint/issues/114) Group and decide which linting rules to include by default.
##### GitHub [#194](https://github.com/cflint/CFLint/issues/194) Config system for includes/excludes is misleading (or broken)
##### GitHub [#222](https://github.com/cflint/CFLint/issues/222) &quot;if&quot; without an &quot;else&quot; triggers &quot;Nothing to parse&quot; warning
##### GitHub [#224](https://github.com/cflint/CFLint/issues/224) --version should include CFParser version
##### GitHub [#226](https://github.com/cflint/CFLint/issues/226) infinite loop in recent builds, triggering segmentation fault
##### GitHub [#227](https://github.com/cflint/CFLint/issues/227) False positive for unused argument when argument is scoped
##### GitHub [#228](https://github.com/cflint/CFLint/issues/228) Multiple code: is not configured correctly
##### GitHub [#229](https://github.com/cflint/CFLint/issues/229) Parsing error with nested if()?
##### GitHub [#230](https://github.com/cflint/CFLint/issues/230) Incorrect error in line 0 when CFML parsing encounters cfscript-like not-equals
##### GitHub [#234](https://github.com/cflint/CFLint/issues/234) Not accounting for switch statements
##### GitHub [#235](https://github.com/cflint/CFLint/pull/235) Add a Codacy badge to README.md
##### GitHub [#239](https://github.com/cflint/CFLint/issues/239) Ignore VAR_ALL_CAPS for components
##### GitHub [#241](https://github.com/cflint/CFLint/issues/241) XML report includes semicolon after PLUGIN_ERROR id and message
##### GitHub [#242](https://github.com/cflint/CFLint/issues/242) Should recognize variables used with differing case
##### GitHub [#244](https://github.com/cflint/CFLint/issues/244) PLUGIN_ERROR on assignment to camel-case variable
##### GitHub [#245](https://github.com/cflint/CFLint/issues/245) Incorrect PLUGIN_ERROR issue ID with appended exception name
##### GitHub [#246](https://github.com/cflint/CFLint/issues/246) Detect when assigning to a variable declared as component property
##### GitHub [#247](https://github.com/cflint/CFLint/pull/247) Update SureFire argLine
##### GitHub [#248](https://github.com/cflint/CFLint/issues/248) Component name missing from message for COMPONENT_INVALID_NAME
##### GitHub [#250](https://github.com/cflint/CFLint/issues/250) Analysis tripped up over UTF-8 files with BOM
## CFLint-0.6.1 
## v0.6.0 
##### GitHub [#104](https://github.com/cflint/CFLint/issues/104) Weird messaging for missing semicolon
##### GitHub [#105](https://github.com/cflint/CFLint/issues/105) Warnings for non-existing errors and non-lint errors before output.
##### GitHub [#62](https://github.com/cflint/CFLint/issues/62) JSON output
##### GitHub [#67](https://github.com/cflint/CFLint/issues/67) Gradle Install
##### GitHub [#80](https://github.com/cflint/CFLint/issues/80) Gradle Deployment
##### GitHub [#95](https://github.com/cflint/CFLint/issues/95) support json config instead of xml
## CFLint0.5.1 
##### GitHub [#65](https://github.com/cflint/CFLint/issues/65) Problem with dynamic table and field names
##### GitHub [#66](https://github.com/cflint/CFLint/issues/66) cflint-disable / cflint-enable ?
##### GitHub [#71](https://github.com/cflint/CFLint/issues/71) Unable to exclude rules
##### GitHub [#89](https://github.com/cflint/CFLint/pull/89) Rule to check for writeDump in cfset tags and script blocks
##### GitHub [#97](https://github.com/cflint/CFLint/issues/97) Release 0.5.1-SNAPSHOT
## v0.5 
##### GitHub [#33](https://github.com/cflint/CFLint/issues/33) no cflint version could be extracted with SublimeLinter
##### GitHub [#35](https://github.com/cflint/CFLint/issues/35) CFLint Should Download Latest Version of CFParser
##### GitHub [#36](https://github.com/cflint/CFLint/issues/36) Add Ability to Preview CFParser Snapshots from Sonatype.
##### GitHub [#38](https://github.com/cflint/CFLint/issues/38) CFLint-ui asking for 4G of Memory
##### GitHub [#42](https://github.com/cflint/CFLint/issues/42) maven build instructions should address common distribution requirements
##### GitHub [#45](https://github.com/cflint/CFLint/issues/45) Lint of cfprocresult fails in function when array is used.
##### GitHub [#49](https://github.com/cflint/CFLint/issues/49) Unable to Parse QueryExecute Functions
##### GitHub [#51](https://github.com/cflint/CFLint/issues/51) maven install should install the binaries in a usable way
##### GitHub [#56](https://github.com/cflint/CFLint/issues/56) Build failure
##### GitHub [#58](https://github.com/cflint/CFLint/issues/58) change license to BSD
##### GitHub [#59](https://github.com/cflint/CFLint/issues/59) Issue with pom.xml file
##### GitHub [#60](https://github.com/cflint/CFLint/issues/60) Having trouble using filterFile
##### GitHub [#62](https://github.com/cflint/CFLint/issues/62) JSON output
##### GitHub [#63](https://github.com/cflint/CFLint/issues/63) Release v0.5.0
## CFLint-0.4-release 
##### GitHub [#19](https://github.com/cflint/CFLint/issues/19) Convert bugs.add() to a plugin format.
##### GitHub [#21](https://github.com/cflint/CFLint/issues/21) &lt;cfset/&gt; on multiple lines does not process
##### GitHub [#24](https://github.com/cflint/CFLint/issues/24) parse error &quot;can&#39;t look backwards more than one token in this stream&quot;
##### GitHub [#25](https://github.com/cflint/CFLint/issues/25) Default cflintexclude.json should only ignore MISSING_VAR&#39;s in init() functions, other violations should NOT be ignored
##### GitHub [#26](https://github.com/cflint/CFLint/issues/26)  QUERYPARAM_REQ message should reflect the cf tag version not the cf script version.
##### GitHub [#27](https://github.com/cflint/CFLint/issues/27) Maven build fails with error.
##### GitHub [#30](https://github.com/cflint/CFLint/issues/30) NESTED_CFOUTPUT false positive
##### GitHub [#31](https://github.com/cflint/CFLint/issues/31) Trying out the configfile from the command line and getting errors
## CFLint-0.4 
##### No issue
## CFLint-0.3.0 
##### No issue
## CFLint-0.2.0 
##### GitHub [#11](https://github.com/cflint/CFLint/issues/11) Multiple exceptions in output
##### GitHub [#2](https://github.com/cflint/CFLint/issues/2) Specifying -textfile as an argument, implies -text should be set.
##### GitHub [#4](https://github.com/cflint/CFLint/issues/4) It would be useful to have a -q (--quiet) option
##### GitHub [#6](https://github.com/cflint/CFLint/issues/6) Add -version flag
##### GitHub [#7](https://github.com/cflint/CFLint/issues/7) Add severity level to each issue in stdout
##### GitHub [#8](https://github.com/cflint/CFLint/issues/8) does not support tagless components
## CFLint-0.1.8 
##### No issue
## CFLint-0.1.6 
##### GitHub [#1](https://github.com/cflint/CFLint/issues/1) ignore .cfm~ files
## CFLint-0.1.5 
##### No issue
## CFLint-0.1.4 
##### No issue
## CFLint-0.1.3 
##### No issue
## CFLint-0.1.2 
##### No issue
