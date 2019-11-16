# CHANGE LOG


## [CFLint-1.4.1](https://github.com/cflint/CFLint/tree/CFLint-1.4.1) (2018-09-28)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-1.4.0...CFLint-1.4.1)

**Implemented enhancements:**

- OSGI support [\#588](https://github.com/cflint/CFLint/issues/588)
- Support for Safe Navigation Operator [\#579](https://github.com/cflint/CFLint/issues/579)
-  command line " -folder .\ " doesn't match any files, " -folder .\views " does [\#576](https://github.com/cflint/CFLint/issues/576)
- Add queryExecute to QueryParamChecker [\#554](https://github.com/cflint/CFLint/issues/554)
- UNUSED\_METHOD\_ARGUMENT: Case not being retained [\#545](https://github.com/cflint/CFLint/issues/545)
- Inconsistent ALLCAPS  [\#544](https://github.com/cflint/CFLint/issues/544)
- CommandBox integration [\#310](https://github.com/cflint/CFLint/issues/310)

**Fixed bugs:**

- Argument-/ComponentNameChecker ignores parameter-override "maxWords" [\#587](https://github.com/cflint/CFLint/issues/587)
- ArrayIndexOutOfBoundsException errors [\#583](https://github.com/cflint/CFLint/issues/583)
- UNUSED\_LOCAL\_VARIABLE false positive when used in new object initialization [\#577](https://github.com/cflint/CFLint/issues/577)
- MISSING\_VAR not triggering for tags in script [\#574](https://github.com/cflint/CFLint/issues/574)
- getting false positives on escaped pounds [\#571](https://github.com/cflint/CFLint/issues/571)
- PARSE\_ERROR unable to parse [\#547](https://github.com/cflint/CFLint/issues/547)
- Parameters not being passed into results?? [\#543](https://github.com/cflint/CFLint/issues/543)
- VAR\_INVALID\_NAME for scoped variable [\#537](https://github.com/cflint/CFLint/issues/537)
- Strange PARSE\_ERROR [\#536](https://github.com/cflint/CFLint/issues/536)
- -quiet reports some internal parsing/ANTLR messaging [\#526](https://github.com/cflint/CFLint/issues/526)

**Closed issues:**

- Find not english code. [\#585](https://github.com/cflint/CFLint/issues/585)
- Upgrade cfparser dependency to 2.7.0 [\#575](https://github.com/cflint/CFLint/issues/575)
- hanging producing report [\#459](https://github.com/cflint/CFLint/issues/459)
- Document CFLint contribution/build/test/play with process [\#317](https://github.com/cflint/CFLint/issues/317)

**Merged pull requests:**

- \#588 [\#591](https://github.com/cflint/CFLint/pull/591) ([ryaneberly](https://github.com/ryaneberly))
- Release 1.4.1 b [\#590](https://github.com/cflint/CFLint/pull/590) ([ryaneberly](https://github.com/ryaneberly))

## [CFLint-1.4.0](https://github.com/cflint/CFLint/tree/1.4.0) (2018-05-27)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-1.3.0...1.4.0)

**Implemented enhancements:**

- Add queryExecute to QueryParamChecker [\#554](https://github.com/cflint/CFLint/issues/554)
- VAR\_INVALID\_NAME false positive with number-suffixed names [\#532](https://github.com/cflint/CFLint/issues/532)
- Stop CFLint from even scanning in known repo directories [\#527](https://github.com/cflint/CFLint/issues/527)
- No arguments being passed into CFLint [\#521](https://github.com/cflint/CFLint/issues/521)
- Report offsets [\#491](https://github.com/cflint/CFLint/issues/491)
- Add debug mode [\#329](https://github.com/cflint/CFLint/issues/329)

**Fixed bugs:**

- catch variables triggering MISSING\_VAR [\#553](https://github.com/cflint/CFLint/issues/553)
-  UNUSED\_METHOD\_ARGUMENT is triggered with a function argument [\#550](https://github.com/cflint/CFLint/issues/550)
- Change to CFLintAPI seems to have a negative impact on passing on values for quiet/verbose. [\#524](https://github.com/cflint/CFLint/issues/524)
- New MISSING\_VAR false positives in 1.3.0 around cfscript/tag syntax [\#517](https://github.com/cflint/CFLint/issues/517)
- Component path not recognized as valid return type [\#500](https://github.com/cflint/CFLint/issues/500)
- Path for object instantiation seen as variable [\#499](https://github.com/cflint/CFLint/issues/499)

**Closed issues:**

- cflintexclude.json not being used [\#531](https://github.com/cflint/CFLint/issues/531)
- -text doesn't write into a file anymore [\#519](https://github.com/cflint/CFLint/issues/519)
- \<cfcookie name...\> fires MISSING\_VAR [\#518](https://github.com/cflint/CFLint/issues/518)
- Describe built-in rules more precisely [\#505](https://github.com/cflint/CFLint/issues/505)
- Log currently processed file to console in verbose mode [\#438](https://github.com/cflint/CFLint/issues/438)
- Remove inheritPlugins and output [\#368](https://github.com/cflint/CFLint/issues/368)

## [CFLint-1.3.0-RC2](https://github.com/cflint/CFLint/tree/CFLint-1.3.0-RC2) (2017-12-28)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-1.3.0-RC1...CFLint-1.3.0-RC2)

**Implemented enhancements:**

- Parameter name standardization [\#503](https://github.com/cflint/CFLint/issues/503)
- Implement an api [\#409](https://github.com/cflint/CFLint/issues/409)

**Fixed bugs:**

- java cast exception when parsing include where template argument is a function result [\#506](https://github.com/cflint/CFLint/issues/506)
- Deprecated message in stdout [\#448](https://github.com/cflint/CFLint/issues/448)

**Closed issues:**

- Provide examples for how to use rule parameters [\#504](https://github.com/cflint/CFLint/issues/504)

**Merged pull requests:**

- Improved some issue messages. [\#502](https://github.com/cflint/CFLint/pull/502) ([KamasamaK](https://github.com/KamasamaK))

## [CFLint-1.3.0-RC1](https://github.com/cflint/CFLint/tree/CFLint-1.3.0-RC1) (2017-12-24)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-1.2.3...CFLint-1.3.0-RC1)

**Implemented enhancements:**

- inline ignore ARG\_TYPE\_MISSING --  support inline ignores at the statement level. [\#497](https://github.com/cflint/CFLint/issues/497)
- MISSING\_VAR reports for variables inside lock{} [\#488](https://github.com/cflint/CFLint/issues/488)
- COMPONENT\_\* errors quote displayName instead of filename [\#469](https://github.com/cflint/CFLint/issues/469)
- Exceptions that occur in plugins \(aka rules\) should not be reported as PARSE\_ERROR [\#358](https://github.com/cflint/CFLint/issues/358)
- Added more tags and attributes to check in VarScoper [\#495](https://github.com/cflint/CFLint/pull/495) ([KamasamaK](https://github.com/KamasamaK))

**Fixed bugs:**

- unused var, but it is used. [\#493](https://github.com/cflint/CFLint/issues/493)
- Ignore mechanism not working properly [\#489](https://github.com/cflint/CFLint/issues/489)
- debug="false" on cfquery reports as leaving debug on tags [\#487](https://github.com/cflint/CFLint/issues/487)
- MISSING\_VAR reports cfwddx\(\) attributes [\#483](https://github.com/cflint/CFLint/issues/483)
- MISSING\_VAR reports cfmail\(\) attributes [\#482](https://github.com/cflint/CFLint/issues/482)
- @CFLintIgnore UNUSED\_METHOD\_ARGUMENT ignored [\#467](https://github.com/cflint/CFLint/issues/467)
- multiple UNUSED\_LOCAL\_VARIABLE false positives with cflib safeText [\#465](https://github.com/cflint/CFLint/issues/465)
- Running on folder causes nonexistent issues to be produced [\#463](https://github.com/cflint/CFLint/issues/463)
- Parsing error on "var no = 1;" [\#442](https://github.com/cflint/CFLint/issues/442)
- NullPointerException when string contains closing HTML tag without a matching opening one [\#440](https://github.com/cflint/CFLint/issues/440)
- NullPointerException-s when 'component' word used in function comment in an interface [\#439](https://github.com/cflint/CFLint/issues/439)
- NullPointerException when calling function 'index' with variable as argument [\#437](https://github.com/cflint/CFLint/issues/437)
- CFLint JAR became 22MB again [\#420](https://github.com/cflint/CFLint/issues/420)
- False positive on unused arguments when argument used in query param [\#418](https://github.com/cflint/CFLint/issues/418)
- False positive unused var when local variable is used in queryparam [\#416](https://github.com/cflint/CFLint/issues/416)
- MISSING\_VAR in CFLOOP index [\#413](https://github.com/cflint/CFLint/issues/413)
- UNQUOTED\_STRUCT\_KEY only reported for implicit declaration [\#405](https://github.com/cflint/CFLint/issues/405)
- MISSING\_VAR not caught for undeclared struct [\#404](https://github.com/cflint/CFLint/issues/404)

**Closed issues:**

- ARG\_VAR\_MIXED reported when implicit struct has key equal to argument [\#478](https://github.com/cflint/CFLint/issues/478)
- wrong expression for VAR\_HAS\_PREFIX\_OR\_POSTFIX [\#476](https://github.com/cflint/CFLint/issues/476)
- Variable Query is not a valid name. Please use CamelCase or underscores. [\#472](https://github.com/cflint/CFLint/issues/472)
- Double NPE with nested array notation [\#452](https://github.com/cflint/CFLint/issues/452)
- NPE with nested array notation [\#451](https://github.com/cflint/CFLint/issues/451)
- PLUGIN\_ERROR on dev branch [\#450](https://github.com/cflint/CFLint/issues/450)
- Execution failed for task ':test'. [\#446](https://github.com/cflint/CFLint/issues/446)
- Config file usage [\#362](https://github.com/cflint/CFLint/issues/362)

**Merged pull requests:**

- Fixed regression with changing case for settings [\#496](https://github.com/cflint/CFLint/pull/496) ([KamasamaK](https://github.com/KamasamaK))
- Dev README [\#494](https://github.com/cflint/CFLint/pull/494) ([KamasamaK](https://github.com/KamasamaK))
- Update README.md [\#484](https://github.com/cflint/CFLint/pull/484) ([cameroncf](https://github.com/cameroncf))
- Add offset to BugInfo. [\#466](https://github.com/cflint/CFLint/pull/466) ([denuno](https://github.com/denuno))
- Missing var 404 [\#461](https://github.com/cflint/CFLint/pull/461) ([ryaneberly](https://github.com/ryaneberly))
- \#405 implemented. [\#460](https://github.com/cflint/CFLint/pull/460) ([ryaneberly](https://github.com/ryaneberly))
- reformat files to fix white space and minor formatting issues [\#458](https://github.com/cflint/CFLint/pull/458) ([justinmclean](https://github.com/justinmclean))
- add missing final to method arguments [\#457](https://github.com/cflint/CFLint/pull/457) ([justinmclean](https://github.com/justinmclean))
- removed unused variables \(mostly severity\) [\#456](https://github.com/cflint/CFLint/pull/456) ([justinmclean](https://github.com/justinmclean))
- add missing scope \(mostly private\) [\#455](https://github.com/cflint/CFLint/pull/455) ([justinmclean](https://github.com/justinmclean))
- move CF constants into own file [\#454](https://github.com/cflint/CFLint/pull/454) ([justinmclean](https://github.com/justinmclean))
- \#450 [\#453](https://github.com/cflint/CFLint/pull/453) ([ryaneberly](https://github.com/ryaneberly))
- Move settings/options out into it's own file [\#445](https://github.com/cflint/CFLint/pull/445) ([justinmclean](https://github.com/justinmclean))
- Refactor levels [\#441](https://github.com/cflint/CFLint/pull/441) ([justinmclean](https://github.com/justinmclean))

## [CFLint-1.2.3](https://github.com/cflint/CFLint/tree/CFLint-1.2.3) (2017-08-12)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-1.2.2...CFLint-1.2.3)

**Closed issues:**

- Null pointer Exception [\#408](https://github.com/cflint/CFLint/issues/408)

## [CFLint-1.2.2](https://github.com/cflint/CFLint/tree/CFLint-1.2.2) (2017-08-10)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint1.2.0...CFLint-1.2.2)

### Overview and major changes in [CFLint 1.2.2](https://github.com/cflint/CFLint/tree/CFLint1.2.2)

#### Linting

* Bugfixes for rule processing

#### Output

* Output of some rules has been improved and fixed
* Internal changes for output processing
* Timestamp issues in XML output sorted
* Ducumentation for output schemas

### Details

**Implemented enhancements:**

- Clarify/update documentation for command line use in Powershell [\#400](https://github.com/cflint/CFLint/issues/400)
- MISSING\_VAR issue in for loop [\#396](https://github.com/cflint/CFLint/issues/396)
- EXCESSIVE\_FUNCTIONS uses absolute path in message [\#367](https://github.com/cflint/CFLint/issues/367)
- Document output schemas [\#322](https://github.com/cflint/CFLint/issues/322)
- Test Harry's Sublime/CFLint instructions and i18n to English if ok [\#307](https://github.com/cflint/CFLint/issues/307)

**Fixed bugs:**

- Gradle builds have stopped \(or never did\) publishing CFLint version number [\#390](https://github.com/cflint/CFLint/issues/390)
- Timestamp in JSON output should be a number [\#383](https://github.com/cflint/CFLint/issues/383)
- CFQUERYPARAM\_REQ false positive with mixed case CFqueryparam [\#380](https://github.com/cflint/CFLint/issues/380)
- Support .cflintrc when using the -stdin argument. [\#373](https://github.com/cflint/CFLint/issues/373)
- PARSE\_ERROR when concatenating strings with single quotes [\#359](https://github.com/cflint/CFLint/issues/359)
- PARSE\_ERROR on scoped variable assignment in CFM [\#346](https://github.com/cflint/CFLint/issues/346)

**Closed issues:**

- PACKAGE\_CASE\_MISMATCH is reporting line 1 instead of actual. [\#402](https://github.com/cflint/CFLint/issues/402)
- Document .cflintrc schema [\#352](https://github.com/cflint/CFLint/issues/352)
- Document IDE integrations [\#312](https://github.com/cflint/CFLint/issues/312)

**Merged pull requests:**

- Dev prep 1.2.1 release [\#415](https://github.com/cflint/CFLint/pull/415) ([ryaneberly](https://github.com/ryaneberly))
- Added VS Code info. Some cleanup. [\#403](https://github.com/cflint/CFLint/pull/403) ([KamasamaK](https://github.com/KamasamaK))
- Update README.md [\#401](https://github.com/cflint/CFLint/pull/401) ([Ilaeria](https://github.com/Ilaeria))
- \#390 - Review changes [\#399](https://github.com/cflint/CFLint/pull/399) ([TheRealAgentK](https://github.com/TheRealAgentK))
- \#390 - Hopefully fixing Gradle build [\#397](https://github.com/cflint/CFLint/pull/397) ([TheRealAgentK](https://github.com/TheRealAgentK))
- \#390 - Adapting new way to grab version string [\#395](https://github.com/cflint/CFLint/pull/395) ([TheRealAgentK](https://github.com/TheRealAgentK))
- \#385 - Further HTML report improvements [\#394](https://github.com/cflint/CFLint/pull/394) ([TheRealAgentK](https://github.com/TheRealAgentK))
- \#390 Add Implementation-Version to MANIFEST [\#393](https://github.com/cflint/CFLint/pull/393) ([mpaluchowski](https://github.com/mpaluchowski))
- Changed build versions to 1.2.1-SNAPSHOT in dev branch and change doc… [\#389](https://github.com/cflint/CFLint/pull/389) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Dev [\#388](https://github.com/cflint/CFLint/pull/388) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Update timestamp type in result schemas [\#387](https://github.com/cflint/CFLint/pull/387) ([KamasamaK](https://github.com/KamasamaK))
- Dev [\#386](https://github.com/cflint/CFLint/pull/386) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Dev [\#384](https://github.com/cflint/CFLint/pull/384) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Merging commit 8a7a538 from dev [\#381](https://github.com/cflint/CFLint/pull/381) ([KamasamaK](https://github.com/KamasamaK))
- Add .editorconfig [\#379](https://github.com/cflint/CFLint/pull/379) ([mpaluchowski](https://github.com/mpaluchowski))
- Align Gradle dependencies with Maven ones [\#378](https://github.com/cflint/CFLint/pull/378) ([mpaluchowski](https://github.com/mpaluchowski))
-  Added .cflintrc schema and output schemas for JSON and XML. Tweaked README. [\#377](https://github.com/cflint/CFLint/pull/377) ([KamasamaK](https://github.com/KamasamaK))

## [CFLint1.2.0](https://github.com/cflint/CFLint/tree/CFLint1.2.0) (2017-07-29)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-1.0.1...CFLint1.2.0)

### Overview and major changes in [CFLint 1.2.0](https://github.com/cflint/CFLint/tree/CFLint1.2.0)

#### Parsing

* Numerous fixes for parsing CFML code, update to CFParser 2.4.10
* Upgrade to ANTLR 4.7

#### Linting

* Bugfixes for rule processing
* Added annotation-based ignoring of rules inline in code.
* JSON-based configuration has undergone a few changes and configuration properties have been deprecated.

#### Output

* Support for -showStats has been removed - scanning statistics are now always produced and displayed/included 
* Findbugs XML output now matches the requirements for Findbugs' bugcollection.xsd and has undergone major changes from earlier versions.
* CFLint XML output has some additional attributes for some XML elements in the output structure (no breaking changes)
* JSON output has undergone a rework of the existing data structure to cater for the output of additional information (breaking changes)

### Details

**Implemented enhancements:**

- ANTLR Tool version 4.6 mismatch [\#366](https://github.com/cflint/CFLint/issues/366)
- inheritPlugins in .cflintrc should be always true  [\#315](https://github.com/cflint/CFLint/issues/315)
- HTML outputs are broken [\#301](https://github.com/cflint/CFLint/issues/301)
- Move old bug count logic for output into cflintstats [\#299](https://github.com/cflint/CFLint/issues/299)
- Allowing to ignore CFQUERYPARAM\_REQ with SQL code [\#295](https://github.com/cflint/CFLint/issues/295)
- Empty files report FILE\_ERROR [\#291](https://github.com/cflint/CFLint/issues/291)
- Additions to CFLint XML \(was: Building a mechanism to pass timestamps and additional counters into FIndbugs XML\) [\#278](https://github.com/cflint/CFLint/issues/278)
- Additional improvements necessary for issues XML to be XML transformed into more meaningful Findbugs XML  [\#276](https://github.com/cflint/CFLint/issues/276)
- Generated findbugs xml report doesn't adhere to Findbugs XML Schema [\#274](https://github.com/cflint/CFLint/issues/274)
- We need this to run for Visual Studio Code also. [\#270](https://github.com/cflint/CFLint/issues/270)
- npm install [\#268](https://github.com/cflint/CFLint/issues/268)

**Fixed bugs:**

- PARSE\_ERROR when "function" used as variable name [\#364](https://github.com/cflint/CFLint/issues/364)
- Rule UNUSED\_LOCAL\_VARIABLE has issues with several tags [\#339](https://github.com/cflint/CFLint/issues/339)
- AVOID\_EMPTY\_FILES being reported if file doesn't exist [\#336](https://github.com/cflint/CFLint/issues/336)
- DefaultCFLintMarshaller doesn't have any of the CFLint XML improvements in XMLOutput [\#330](https://github.com/cflint/CFLint/issues/330)
- EXCESSIVE\_ARGUMENTS is not configured correctly [\#308](https://github.com/cflint/CFLint/issues/308)
- HTML outputs are broken [\#301](https://github.com/cflint/CFLint/issues/301)
- Stack overflow in dev branch [\#293](https://github.com/cflint/CFLint/issues/293)
- MISSING\_SEMI false positive in if/else [\#289](https://github.com/cflint/CFLint/issues/289)
- Script ignore syntax fails in mixed cfscript/tag code [\#285](https://github.com/cflint/CFLint/issues/285)
- MISSING\_VAR false positive cfquery loops [\#284](https://github.com/cflint/CFLint/issues/284)
- CFQUERYPARAM\_REQ false positives [\#282](https://github.com/cflint/CFLint/issues/282)
- Generated findbugs xml report doesn't adhere to Findbugs XML Schema [\#274](https://github.com/cflint/CFLint/issues/274)
- Findbugs XML output creates \<?xml...?\> tag twice [\#271](https://github.com/cflint/CFLint/issues/271)
- Unclear error messages: Error in plugin, 0 [\#269](https://github.com/cflint/CFLint/issues/269)
- Complex if\(\) freezes analysis [\#266](https://github.com/cflint/CFLint/issues/266)
- CFML analysis tries to parse tags inside strings/literals [\#253](https://github.com/cflint/CFLint/issues/253)

**Closed issues:**

- Maven build fails using the ZIP file from GitHub since it needs the `.git` folder [\#363](https://github.com/cflint/CFLint/issues/363)
- Add contributing guidelines document [\#361](https://github.com/cflint/CFLint/issues/361)
- PARSE\_ERROR when cfloop has "delimiters" attribute [\#357](https://github.com/cflint/CFLint/issues/357)
- Remove and deprecate "output" on top-level of .cflintrc [\#356](https://github.com/cflint/CFLint/issues/356)
- PARSE\_ERROR on component with a name coinciding with one instantiated elsewhere [\#347](https://github.com/cflint/CFLint/issues/347)
- GLOBAL\_LITERAL\_VALUE\_USED\_TOO\_OFTEN falsely indicated in CFM files with \<cfinclude\> [\#345](https://github.com/cflint/CFLint/issues/345)
- GLOBAL\_LITERAL\_VALUE\_USED\_TOO\_OFTEN triggered by built in function arguments [\#344](https://github.com/cflint/CFLint/issues/344)
- GLOBAL\_LITERAL\_VALUE\_USED\_TOO\_OFTEN triggered on CF\_SQL\_ [\#340](https://github.com/cflint/CFLint/issues/340)
- CFLint reporting a column that doesn't exist [\#326](https://github.com/cflint/CFLint/issues/326)
- Various smaller issues with output  [\#323](https://github.com/cflint/CFLint/issues/323)
- JSON output questions \(int/string and locations array\) [\#318](https://github.com/cflint/CFLint/issues/318)
- Add \#cflint channel on Slack CFML [\#309](https://github.com/cflint/CFLint/issues/309)
- Findbugs: totalsize should be lines of code scanned [\#303](https://github.com/cflint/CFLint/issues/303)
- CFQUERYPARAM\_REQ false positive: String in SQL Server XML function [\#297](https://github.com/cflint/CFLint/issues/297)
- Identifier cfcatch is global, referencing in a CFC or function should be avoided. [\#265](https://github.com/cflint/CFLint/issues/265)
- Parse errors when last element in structure has trailing comma [\#263](https://github.com/cflint/CFLint/issues/263)
- feaure: flag use of isDate\(\)  [\#262](https://github.com/cflint/CFLint/issues/262)
- cfscript version of calling stored proc giving errors with correct code. [\#261](https://github.com/cflint/CFLint/issues/261)
- Default Rules merging with config.xml [\#260](https://github.com/cflint/CFLint/issues/260)
- Parse error for method calls with certain names including contains and import. [\#258](https://github.com/cflint/CFLint/issues/258)
- Does not see argument as used when it is used in brackets inside of function. [\#257](https://github.com/cflint/CFLint/issues/257)
- Var 'declared' with local scope. [\#256](https://github.com/cflint/CFLint/issues/256)
- unsused local variable rule does not handle cfinclude [\#255](https://github.com/cflint/CFLint/issues/255)
- Variable Prefix and Postfix - More dynamic [\#254](https://github.com/cflint/CFLint/issues/254)
- cfset with var and square brackets causes PARSE\_ERROR [\#252](https://github.com/cflint/CFLint/issues/252)
- Graceful handling of semicolon missing at end of return statement [\#251](https://github.com/cflint/CFLint/issues/251)
- Parse error when multiple spaces in CFML tag [\#243](https://github.com/cflint/CFLint/issues/243)
- Support a way to ignore found issues in the future. [\#195](https://github.com/cflint/CFLint/issues/195)

**Merged pull requests:**

- Remove dependency copying [\#376](https://github.com/cflint/CFLint/pull/376) ([mpaluchowski](https://github.com/mpaluchowski))
- Dev [\#375](https://github.com/cflint/CFLint/pull/375) ([TheRealAgentK](https://github.com/TheRealAgentK))
- \#323 - Changing JSON structure and changing expected test results [\#370](https://github.com/cflint/CFLint/pull/370) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Various... [\#369](https://github.com/cflint/CFLint/pull/369) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Dev [\#360](https://github.com/cflint/CFLint/pull/360) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Dev [\#353](https://github.com/cflint/CFLint/pull/353) ([TheRealAgentK](https://github.com/TheRealAgentK))
- More documentation [\#351](https://github.com/cflint/CFLint/pull/351) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Some initial changes and content moving from Wiki into README for 1.2… [\#350](https://github.com/cflint/CFLint/pull/350) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Remove dependencies defined in cfparser and add git info to artifact. [\#349](https://github.com/cflint/CFLint/pull/349) ([denuno](https://github.com/denuno))
- Update to cfparser 2.4.9 [\#348](https://github.com/cflint/CFLint/pull/348) ([mpaluchowski](https://github.com/mpaluchowski))
- Lighten ANTLR dependencies [\#343](https://github.com/cflint/CFLint/pull/343) ([mpaluchowski](https://github.com/mpaluchowski))
- Put .cflintrc back into Parsing directory for test. Was missing after… [\#342](https://github.com/cflint/CFLint/pull/342) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Fix for test fail on case-insensitive filesystems [\#341](https://github.com/cflint/CFLint/pull/341) ([mpaluchowski](https://github.com/mpaluchowski))
- POM dependency cleanup [\#338](https://github.com/cflint/CFLint/pull/338) ([mpaluchowski](https://github.com/mpaluchowski))
- Updated to 1.2.0 in gradle.properties [\#337](https://github.com/cflint/CFLint/pull/337) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Updated pom in dev for 1.2.0-SNAPSHOT [\#335](https://github.com/cflint/CFLint/pull/335) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Dev [\#334](https://github.com/cflint/CFLint/pull/334) ([TheRealAgentK](https://github.com/TheRealAgentK))
- \#315 forcing inheritPluhins to be true [\#333](https://github.com/cflint/CFLint/pull/333) ([TheRealAgentK](https://github.com/TheRealAgentK))
- 323 and 330 [\#332](https://github.com/cflint/CFLint/pull/332) ([TheRealAgentK](https://github.com/TheRealAgentK))
- 300 remove show stats [\#325](https://github.com/cflint/CFLint/pull/325) ([TheRealAgentK](https://github.com/TheRealAgentK))
- 318 json output fixes [\#324](https://github.com/cflint/CFLint/pull/324) ([TheRealAgentK](https://github.com/TheRealAgentK))
- 290 parse error [\#321](https://github.com/cflint/CFLint/pull/321) ([TheRealAgentK](https://github.com/TheRealAgentK))
- 299 refactoring stats [\#320](https://github.com/cflint/CFLint/pull/320) ([TheRealAgentK](https://github.com/TheRealAgentK))
- \#303 Remaining fixes [\#305](https://github.com/cflint/CFLint/pull/305) ([TheRealAgentK](https://github.com/TheRealAgentK))
- 301 - html report fixes [\#304](https://github.com/cflint/CFLint/pull/304) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Partial fix for \#301 [\#302](https://github.com/cflint/CFLint/pull/302) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Work on \#278, Improving Findbugs XML output with stats counts and timestamp [\#298](https://github.com/cflint/CFLint/pull/298) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Retrospective fix for test case for \#282 \(and reopened \#195\) [\#296](https://github.com/cflint/CFLint/pull/296) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Cherry-picked and cleaned up @denuno's fix for \#285 [\#294](https://github.com/cflint/CFLint/pull/294) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Testcase for \#285 [\#287](https://github.com/cflint/CFLint/pull/287) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Added testcase for \#195 \(ignoring rules\) not working [\#283](https://github.com/cflint/CFLint/pull/283) ([TheRealAgentK](https://github.com/TheRealAgentK))
- PR for master-\>dev merge [\#281](https://github.com/cflint/CFLint/pull/281) ([TheRealAgentK](https://github.com/TheRealAgentK))
- 276 findbugs improvement [\#280](https://github.com/cflint/CFLint/pull/280) ([TheRealAgentK](https://github.com/TheRealAgentK))
- 275 remove old xsl [\#279](https://github.com/cflint/CFLint/pull/279) ([TheRealAgentK](https://github.com/TheRealAgentK))
- \#274 findbugs schema fixes [\#277](https://github.com/cflint/CFLint/pull/277) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Fix for \#271 [\#272](https://github.com/cflint/CFLint/pull/272) ([TheRealAgentK](https://github.com/TheRealAgentK))

## [CFLint-1.0.1](https://github.com/cflint/CFLint/tree/CFLint-1.0.1) (2017-03-13)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.6.1...CFLint-1.0.1)

**Implemented enhancements:**

- Gradle Deployment [\#80](https://github.com/cflint/CFLint/issues/80)
- JavaDocs [\#61](https://github.com/cflint/CFLint/issues/61)

**Closed issues:**

- Analysis tripped up over UTF-8 files with BOM [\#250](https://github.com/cflint/CFLint/issues/250)
- Using 'function' as shorthand structure key causes a parsing error [\#249](https://github.com/cflint/CFLint/issues/249)
- Component name missing from message for COMPONENT\_INVALID\_NAME [\#248](https://github.com/cflint/CFLint/issues/248)
- Detect when assigning to a variable declared as component property [\#246](https://github.com/cflint/CFLint/issues/246)
- Incorrect PLUGIN\_ERROR issue ID with appended exception name [\#245](https://github.com/cflint/CFLint/issues/245)
- PLUGIN\_ERROR on assignment to camel-case variable [\#244](https://github.com/cflint/CFLint/issues/244)
- Should recognize variables used with differing case [\#242](https://github.com/cflint/CFLint/issues/242)
- XML report includes semicolon after PLUGIN\_ERROR id and message [\#241](https://github.com/cflint/CFLint/issues/241)
- Escaped hashes in CFM files cause PLUGIN\_ERROR [\#240](https://github.com/cflint/CFLint/issues/240)
- Ignore VAR\_ALL\_CAPS for components [\#239](https://github.com/cflint/CFLint/issues/239)
- cflint/cfparser not handling the script version of cfmail correctly. [\#237](https://github.com/cflint/CFLint/issues/237)
- Parsing cfscript functions with hints renders multiple errors. [\#236](https://github.com/cflint/CFLint/issues/236)
- Not accounting for switch statements [\#234](https://github.com/cflint/CFLint/issues/234)
- Parse error using comparison operators in struct assignment [\#233](https://github.com/cflint/CFLint/issues/233)
- Parse error using Elvis operator in struct assignment [\#232](https://github.com/cflint/CFLint/issues/232)
- Combine the config and the filter file [\#231](https://github.com/cflint/CFLint/issues/231)
- Incorrect error in line 0 when CFML parsing encounters cfscript-like not-equals [\#230](https://github.com/cflint/CFLint/issues/230)
- Parsing error with nested if\(\)? [\#229](https://github.com/cflint/CFLint/issues/229)
- Multiple code: is not configured correctly [\#228](https://github.com/cflint/CFLint/issues/228)
- False positive for unused argument when argument is scoped [\#227](https://github.com/cflint/CFLint/issues/227)
- infinite loop in recent builds, triggering segmentation fault [\#226](https://github.com/cflint/CFLint/issues/226)
- --version should include CFParser version [\#224](https://github.com/cflint/CFLint/issues/224)
- Duplicate "Temporary variable could be named better" warnings [\#223](https://github.com/cflint/CFLint/issues/223)
- "if" without an "else" triggers "Nothing to parse" warning [\#222](https://github.com/cflint/CFLint/issues/222)
- Linter does not check code inside cfscript try/catch/finally blocks [\#220](https://github.com/cflint/CFLint/issues/220)
- Thank you [\#219](https://github.com/cflint/CFLint/issues/219)
- Where does cflint-ui come from? [\#212](https://github.com/cflint/CFLint/issues/212)
- False positive variable used in loop reported as unused [\#211](https://github.com/cflint/CFLint/issues/211)
- False positive when named arguments in function call enclosed in quotes [\#210](https://github.com/cflint/CFLint/issues/210)
- False positive when return statement contains assignment [\#209](https://github.com/cflint/CFLint/issues/209)
- Incorrect line number in XML export when previously scanned CFML-based file [\#208](https://github.com/cflint/CFLint/issues/208)
- can't parse param syntax without attribute names [\#207](https://github.com/cflint/CFLint/issues/207)
- Stackoverflow with execute\(\) function [\#200](https://github.com/cflint/CFLint/issues/200)
- Rules reporting missing hints should work with script-style code [\#199](https://github.com/cflint/CFLint/issues/199)
- New rule for specifying structure key names in quotation marks [\#198](https://github.com/cflint/CFLint/issues/198)
- False positive: structure keys reported as variables without scope prefix [\#197](https://github.com/cflint/CFLint/issues/197)
- Config system for includes/excludes is misleading \(or broken\) [\#194](https://github.com/cflint/CFLint/issues/194)
- Directory Scan [\#192](https://github.com/cflint/CFLint/issues/192)
- Doesn't seem to be checking missing\_var on cfscript cfcs [\#186](https://github.com/cflint/CFLint/issues/186)
- configfile excludes rules [\#185](https://github.com/cflint/CFLint/issues/185)
- writeDump arguments not recognised correctly [\#183](https://github.com/cflint/CFLint/issues/183)
- Named arguments in functions [\#182](https://github.com/cflint/CFLint/issues/182)
- CFLint - wrong line number in XML report [\#179](https://github.com/cflint/CFLint/issues/179)
- incompatible types: cfml.parsing.cfscript.CFIdentifier cannot be converted to java.lang.String [\#178](https://github.com/cflint/CFLint/issues/178)
- Errors: property & array returntype [\#177](https://github.com/cflint/CFLint/issues/177)
- cfmodule [\#176](https://github.com/cflint/CFLint/issues/176)
- Exclude rule for all INFO severity messages, excludes all messages [\#173](https://github.com/cflint/CFLint/issues/173)
- XML Output : MalformedByteSequenceException: Invalid byte 1 of 1-byte UTF-8 sequence [\#171](https://github.com/cflint/CFLint/issues/171)
- Build Error on dev b359dae [\#169](https://github.com/cflint/CFLint/issues/169)
- multiple location members in JSON output [\#158](https://github.com/cflint/CFLint/issues/158)
- EXCESSIVE\_COMPONENT\_LENGTH reported multiple times per CFC [\#156](https://github.com/cflint/CFLint/issues/156)
- Provide a roadmap [\#154](https://github.com/cflint/CFLint/issues/154)
- unused-local-variable ignores variable usage inside of \<cfquery\> \(QoQ\) [\#153](https://github.com/cflint/CFLint/issues/153)
- UNUSED\_METHOD\_ARGUMENT ignores scoped arguments [\#152](https://github.com/cflint/CFLint/issues/152)
- Propsal: Drop "Variable x should be longer than 3 characters" warning [\#151](https://github.com/cflint/CFLint/issues/151)
- Trying to update, can't find original git clone folder [\#150](https://github.com/cflint/CFLint/issues/150)
- explore going to jdom 2.x [\#149](https://github.com/cflint/CFLint/issues/149)
- AVOID\_USING\_CFSETTING\_DEBUG is not configured correctly [\#148](https://github.com/cflint/CFLint/issues/148)
- VAR\_ALLCAPS\_NAME handles scope [\#147](https://github.com/cflint/CFLint/issues/147)
- difference between output styles -text and -json [\#146](https://github.com/cflint/CFLint/issues/146)
- AVOID\_USING\_CFMODULE\_TAG appears too often [\#145](https://github.com/cflint/CFLint/issues/145)
- AVOID\_USING\_CFINCUDE\_TAG spelling [\#144](https://github.com/cflint/CFLint/issues/144)
- Finding UNUSED\_METHOD\_ARGUMENT appears too often [\#143](https://github.com/cflint/CFLint/issues/143)
- Parse issue [\#140](https://github.com/cflint/CFLint/issues/140)
- Code style [\#132](https://github.com/cflint/CFLint/issues/132)
- Group and decide which linting rules to include by default. [\#114](https://github.com/cflint/CFLint/issues/114)
- Enhancement: Enable default config file when parsing similar to other linters [\#103](https://github.com/cflint/CFLint/issues/103)
- Release 0.5.1-SNAPSHOT [\#97](https://github.com/cflint/CFLint/issues/97)
- support json config instead of xml [\#95](https://github.com/cflint/CFLint/issues/95)

**Merged pull requests:**

- Update SureFire argLine [\#247](https://github.com/cflint/CFLint/pull/247) ([mpaluchowski](https://github.com/mpaluchowski))
- Update the POM file [\#238](https://github.com/cflint/CFLint/pull/238) ([msp1kpj](https://github.com/msp1kpj))
- Add a Codacy badge to README.md [\#235](https://github.com/cflint/CFLint/pull/235) ([codacy-badger](https://github.com/codacy-badger))
- Dev [\#225](https://github.com/cflint/CFLint/pull/225) ([ryaneberly](https://github.com/ryaneberly))
- Dev [\#221](https://github.com/cflint/CFLint/pull/221) ([ryaneberly](https://github.com/ryaneberly))
- update dev [\#218](https://github.com/cflint/CFLint/pull/218) ([ryaneberly](https://github.com/ryaneberly))
- Dev [\#217](https://github.com/cflint/CFLint/pull/217) ([ryaneberly](https://github.com/ryaneberly))
- Dev to rulesets [\#216](https://github.com/cflint/CFLint/pull/216) ([ryaneberly](https://github.com/ryaneberly))
- Dev [\#215](https://github.com/cflint/CFLint/pull/215) ([ryaneberly](https://github.com/ryaneberly))
- Fixtravis [\#214](https://github.com/cflint/CFLint/pull/214) ([ryaneberly](https://github.com/ryaneberly))
- Fixed wrong parameter in error message [\#213](https://github.com/cflint/CFLint/pull/213) ([TheRealAgentK](https://github.com/TheRealAgentK))
- Dev [\#206](https://github.com/cflint/CFLint/pull/206) ([ryaneberly](https://github.com/ryaneberly))
- fix version [\#205](https://github.com/cflint/CFLint/pull/205) ([ryaneberly](https://github.com/ryaneberly))
- Dev [\#204](https://github.com/cflint/CFLint/pull/204) ([ryaneberly](https://github.com/ryaneberly))
- Dev [\#202](https://github.com/cflint/CFLint/pull/202) ([ryaneberly](https://github.com/ryaneberly))
- \#194 merging configfile with filter [\#201](https://github.com/cflint/CFLint/pull/201) ([rcastagno](https://github.com/rcastagno))
- Dev to Master [\#196](https://github.com/cflint/CFLint/pull/196) ([msp1kpj](https://github.com/msp1kpj))
- master to dev [\#193](https://github.com/cflint/CFLint/pull/193) ([ryaneberly](https://github.com/ryaneberly))
- Generate xml with issues using stax implementation [\#191](https://github.com/cflint/CFLint/pull/191) ([tstec](https://github.com/tstec))
- Allow to skip analyse for specific variable names [\#190](https://github.com/cflint/CFLint/pull/190) ([tstec](https://github.com/tstec))
- Workaround context leaking [\#189](https://github.com/cflint/CFLint/pull/189) ([tstec](https://github.com/tstec))
- Reset checker's statistics before analyzing each file. [\#188](https://github.com/cflint/CFLint/pull/188) ([tstec](https://github.com/tstec))
- Exclude commented arguments from counting [\#187](https://github.com/cflint/CFLint/pull/187) ([tstec](https://github.com/tstec))
- Dev [\#184](https://github.com/cflint/CFLint/pull/184) ([ryaneberly](https://github.com/ryaneberly))
- Dev [\#181](https://github.com/cflint/CFLint/pull/181) ([ryaneberly](https://github.com/ryaneberly))
- master to dev [\#175](https://github.com/cflint/CFLint/pull/175) ([ryaneberly](https://github.com/ryaneberly))
- Dev [\#172](https://github.com/cflint/CFLint/pull/172) ([ryaneberly](https://github.com/ryaneberly))
- Dev [\#170](https://github.com/cflint/CFLint/pull/170) ([denuno](https://github.com/denuno))
- This pull adds some null checks and bumps the cfml.parsing dependencies [\#168](https://github.com/cflint/CFLint/pull/168) ([denuno](https://github.com/denuno))
- Dev [\#167](https://github.com/cflint/CFLint/pull/167) ([ryaneberly](https://github.com/ryaneberly))
- Dev [\#166](https://github.com/cflint/CFLint/pull/166) ([ryaneberly](https://github.com/ryaneberly))
- bump to use cfml.parsing 2.2.2, and abstract adding scanners [\#165](https://github.com/cflint/CFLint/pull/165) ([denuno](https://github.com/denuno))
- String literals should not be duplicated [\#163](https://github.com/cflint/CFLint/pull/163) ([AymanDF](https://github.com/AymanDF))
- Merging collapsible if statements increases the code's readability [\#162](https://github.com/cflint/CFLint/pull/162) ([AymanDF](https://github.com/AymanDF))
- Using Collection.isEmpty\(\) to test for emptiness [\#161](https://github.com/cflint/CFLint/pull/161) ([AymanDF](https://github.com/AymanDF))
- Dead stores should be removed [\#160](https://github.com/cflint/CFLint/pull/160) ([AymanDF](https://github.com/AymanDF))
- Resources should be closed [\#159](https://github.com/cflint/CFLint/pull/159) ([AymanDF](https://github.com/AymanDF))
- master to dev [\#157](https://github.com/cflint/CFLint/pull/157) ([ryaneberly](https://github.com/ryaneberly))
- Dev [\#155](https://github.com/cflint/CFLint/pull/155) ([ryaneberly](https://github.com/ryaneberly))
- Fix issue where component name was always 'source' when using -stdin [\#142](https://github.com/cflint/CFLint/pull/142) ([sjmatta](https://github.com/sjmatta))

## [CFLint-0.6.1](https://github.com/cflint/CFLint/tree/CFLint-0.6.1) (2015-12-19)
[Full Changelog](https://github.com/cflint/CFLint/compare/v0.6.0...CFLint-0.6.1)

**Closed issues:**

- some tests fail while building CFLint [\#137](https://github.com/cflint/CFLint/issues/137)

## [v0.6.0](https://github.com/cflint/CFLint/tree/v0.6.0) (2015-12-15)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint0.5.1...v0.6.0)

**Implemented enhancements:**

- Suggest: console progress bar [\#32](https://github.com/cflint/CFLint/issues/32)

**Fixed bugs:**

- HTML version of output has wrong CFLint version. [\#129](https://github.com/cflint/CFLint/issues/129)

**Closed issues:**

- Build failure [\#133](https://github.com/cflint/CFLint/issues/133)
- Failure to build with maven on current master. [\#128](https://github.com/cflint/CFLint/issues/128)
- Optionally allow plugins to recieve structure notifications [\#112](https://github.com/cflint/CFLint/issues/112)
- Warnings for non-existing errors and non-lint errors before output. [\#105](https://github.com/cflint/CFLint/issues/105)
- Weird messaging for missing semicolon [\#104](https://github.com/cflint/CFLint/issues/104)
- Unable to parse function metadata with a colon in the attribute name [\#102](https://github.com/cflint/CFLint/issues/102)
- Parse error for string concatenation in object literal [\#101](https://github.com/cflint/CFLint/issues/101)
- parse error for "property propName;" [\#100](https://github.com/cflint/CFLint/issues/100)
- Unable to exclude rules [\#71](https://github.com/cflint/CFLint/issues/71)
- Problem with dynamic table and field names [\#65](https://github.com/cflint/CFLint/issues/65)
- Unexpected warnings in cfquery tag [\#64](https://github.com/cflint/CFLint/issues/64)
- GPG error on mvn clean install [\#23](https://github.com/cflint/CFLint/issues/23)

**Merged pull requests:**

- Merge pull request \#138 from cflint/master [\#139](https://github.com/cflint/CFLint/pull/139) ([ryaneberly](https://github.com/ryaneberly))
- move changes on master to dev [\#138](https://github.com/cflint/CFLint/pull/138) ([ryaneberly](https://github.com/ryaneberly))
- Revert "Hashmap Bug: HashMap.value\(\) does not ensure order, updated to Linked…" [\#136](https://github.com/cflint/CFLint/pull/136) ([justinmclean](https://github.com/justinmclean))
- Hashmap Bug: HashMap.value\(\) does not ensure order, updated to Linked… [\#135](https://github.com/cflint/CFLint/pull/135) ([playedinane](https://github.com/playedinane))
- Fix version number in XML and HTML outputs [\#131](https://github.com/cflint/CFLint/pull/131) ([justinmclean](https://github.com/justinmclean))
- Rule to check for unused arguments in methods [\#127](https://github.com/cflint/CFLint/pull/127) ([justinmclean](https://github.com/justinmclean))
- Rule to check for unused local variables [\#126](https://github.com/cflint/CFLint/pull/126) ([justinmclean](https://github.com/justinmclean))
- Introduce file driven tests. [\#125](https://github.com/cflint/CFLint/pull/125) ([ryaneberly](https://github.com/ryaneberly))
- Dev release prep [\#124](https://github.com/cflint/CFLint/pull/124) ([ryaneberly](https://github.com/ryaneberly))
- Rule to check method argument names [\#123](https://github.com/cflint/CFLint/pull/123) ([justinmclean](https://github.com/justinmclean))
- Fix issue were rule was being passed file path not file name [\#122](https://github.com/cflint/CFLint/pull/122) ([justinmclean](https://github.com/justinmclean))
- Rule to check if .cfm file starts with an upper case letter [\#121](https://github.com/cflint/CFLint/pull/121) ([justinmclean](https://github.com/justinmclean))
- Rule to check component naming [\#120](https://github.com/cflint/CFLint/pull/120) ([justinmclean](https://github.com/justinmclean))
- Rule to checking for old way of creating components via createObject [\#118](https://github.com/cflint/CFLint/pull/118) ([justinmclean](https://github.com/justinmclean))
- Pluginnotify [\#117](https://github.com/cflint/CFLint/pull/117) ([ryaneberly](https://github.com/ryaneberly))
- check for cfinclude inside cfcs [\#116](https://github.com/cflint/CFLint/pull/116) ([justinmclean](https://github.com/justinmclean))
- Dev [\#115](https://github.com/cflint/CFLint/pull/115) ([ryaneberly](https://github.com/ryaneberly))
- Add -stdin and -stdout parameters [\#113](https://github.com/cflint/CFLint/pull/113) ([sjmatta](https://github.com/sjmatta))
- \#95.  jsonconfig [\#111](https://github.com/cflint/CFLint/pull/111) ([ryaneberly](https://github.com/ryaneberly))
- Betterparsingerr [\#110](https://github.com/cflint/CFLint/pull/110) ([ryaneberly](https://github.com/ryaneberly))
- Provide some rules around naming of variables and methods [\#109](https://github.com/cflint/CFLint/pull/109) ([justinmclean](https://github.com/justinmclean))
- Update code and add tests to check in return statements [\#108](https://github.com/cflint/CFLint/pull/108) ([justinmclean](https://github.com/justinmclean))
- Dev [\#99](https://github.com/cflint/CFLint/pull/99) ([ryaneberly](https://github.com/ryaneberly))
- Dev [\#98](https://github.com/cflint/CFLint/pull/98) ([ryaneberly](https://github.com/ryaneberly))
- Added rule to check for repeated hard coded values [\#94](https://github.com/cflint/CFLint/pull/94) ([justinmclean](https://github.com/justinmclean))
- Rule to check for complex boolean expressions [\#93](https://github.com/cflint/CFLint/pull/93) ([justinmclean](https://github.com/justinmclean))
- Check for explicit boolean expression checking for true and false [\#92](https://github.com/cflint/CFLint/pull/92) ([justinmclean](https://github.com/justinmclean))
- Rule to check for structNew and suggest using {} instead [\#91](https://github.com/cflint/CFLint/pull/91) ([justinmclean](https://github.com/justinmclean))
- Rule to check for ArrayNew and suggest using \[\] instead [\#90](https://github.com/cflint/CFLint/pull/90) ([justinmclean](https://github.com/justinmclean))

## [CFLint0.5.1](https://github.com/cflint/CFLint/tree/CFLint0.5.1) (2015-10-12)
[Full Changelog](https://github.com/cflint/CFLint/compare/v0.5...CFLint0.5.1)

**Implemented enhancements:**

- Function Length Checker In Config vs. Code [\#68](https://github.com/cflint/CFLint/issues/68)
- JSON output [\#62](https://github.com/cflint/CFLint/issues/62)

**Closed issues:**

- context.addMessage vs bugs.add [\#72](https://github.com/cflint/CFLint/issues/72)
- Error when reading config file [\#69](https://github.com/cflint/CFLint/issues/69)
- cflint-disable / cflint-enable ? [\#66](https://github.com/cflint/CFLint/issues/66)
- Release v0.5.0 [\#63](https://github.com/cflint/CFLint/issues/63)
- Returning incorrect version number [\#46](https://github.com/cflint/CFLint/issues/46)

**Merged pull requests:**

- Rule to check for writeDump in cfset tags and script blocks [\#89](https://github.com/cflint/CFLint/pull/89) ([justinmclean](https://github.com/justinmclean))
- Rule to work out simple complexity measure for functions [\#88](https://github.com/cflint/CFLint/pull/88) ([justinmclean](https://github.com/justinmclean))
- Warn about abort statements in script blocks [\#87](https://github.com/cflint/CFLint/pull/87) ([justinmclean](https://github.com/justinmclean))
- Rule to check for incorrect name attribute on component [\#82](https://github.com/cflint/CFLint/pull/82) ([justinmclean](https://github.com/justinmclean))
- Component has too many functions [\#79](https://github.com/cflint/CFLint/pull/79) ([justinmclean](https://github.com/justinmclean))
- Function has too many arguments [\#78](https://github.com/cflint/CFLint/pull/78) ([justinmclean](https://github.com/justinmclean))
- Cfabort [\#76](https://github.com/cflint/CFLint/pull/76) ([justinmclean](https://github.com/justinmclean))
- Added counts of each error type. [\#75](https://github.com/cflint/CFLint/pull/75) ([justinmclean](https://github.com/justinmclean))
- Rule to check function return type exists or is any [\#74](https://github.com/cflint/CFLint/pull/74) ([justinmclean](https://github.com/justinmclean))
- Function argument checker [\#73](https://github.com/cflint/CFLint/pull/73) ([justinmclean](https://github.com/justinmclean))
- Added simple hint checker [\#70](https://github.com/cflint/CFLint/pull/70) ([justinmclean](https://github.com/justinmclean))

## [v0.5](https://github.com/cflint/CFLint/tree/v0.5) (2015-06-16)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.4-release...v0.5)

**Closed issues:**

- Having trouble using filterFile [\#60](https://github.com/cflint/CFLint/issues/60)
- Issue with pom.xml file [\#59](https://github.com/cflint/CFLint/issues/59)
- change license to BSD [\#58](https://github.com/cflint/CFLint/issues/58)
- Build failure [\#56](https://github.com/cflint/CFLint/issues/56)
- Support for CF 11 [\#55](https://github.com/cflint/CFLint/issues/55)
- Misc. Parsing Error? [\#54](https://github.com/cflint/CFLint/issues/54)
- maven install should install the binaries in a usable way [\#51](https://github.com/cflint/CFLint/issues/51)
- Argument appScope is not required and does not define a default value. [\#50](https://github.com/cflint/CFLint/issues/50)
- Unable to Parse QueryExecute Functions [\#49](https://github.com/cflint/CFLint/issues/49)
- Vim syntastic support [\#48](https://github.com/cflint/CFLint/issues/48)
- Speeding up CFLint [\#47](https://github.com/cflint/CFLint/issues/47)
- Lint of cfprocresult fails in function when array is used. [\#45](https://github.com/cflint/CFLint/issues/45)
- Arch Linux AUR [\#44](https://github.com/cflint/CFLint/issues/44)
- "import" statement breaks parsing for scripted components [\#43](https://github.com/cflint/CFLint/issues/43)
- maven build instructions should address common distribution requirements [\#42](https://github.com/cflint/CFLint/issues/42)
- License [\#41](https://github.com/cflint/CFLint/issues/41)
- Errors while parsing double assignments [\#40](https://github.com/cflint/CFLint/issues/40)
- Errors while parsing variables named "string" [\#39](https://github.com/cflint/CFLint/issues/39)
- CFLint-ui asking for 4G of Memory [\#38](https://github.com/cflint/CFLint/issues/38)
- Use latest \(antlr4\) version of cfparser. [\#37](https://github.com/cflint/CFLint/issues/37)
- Add Ability to Preview CFParser Snapshots from Sonatype. [\#36](https://github.com/cflint/CFLint/issues/36)
- CFLint Should Download Latest Version of CFParser [\#35](https://github.com/cflint/CFLint/issues/35)
- Failed to execute goal on project CFLint: Could not resolve dependencies [\#34](https://github.com/cflint/CFLint/issues/34)
- no cflint version could be extracted with SublimeLinter [\#33](https://github.com/cflint/CFLint/issues/33)
- parse error "can't look backwards more than one token in this stream" [\#24](https://github.com/cflint/CFLint/issues/24)
- NoViableAltException java.lang.NullPointerException [\#22](https://github.com/cflint/CFLint/issues/22)
- Mac OSX 10.9.4 Good build can not find  [\#20](https://github.com/cflint/CFLint/issues/20)
- Multiple exceptions in output [\#11](https://github.com/cflint/CFLint/issues/11)

**Merged pull requests:**

- Update README.md [\#57](https://github.com/cflint/CFLint/pull/57) ([AntoineGagnon](https://github.com/AntoineGagnon))
- note "mvn install -D assembleDirectory=" option [\#53](https://github.com/cflint/CFLint/pull/53) ([displague](https://github.com/displague))
- link to cflint-syntastic in the README [\#52](https://github.com/cflint/CFLint/pull/52) ([displague](https://github.com/displague))

## [CFLint-0.4-release](https://github.com/cflint/CFLint/tree/CFLint-0.4-release) (2015-02-10)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.3.1...CFLint-0.4-release)

## [CFLint-0.3.1](https://github.com/cflint/CFLint/tree/CFLint-0.3.1) (2015-02-06)
[Full Changelog](https://github.com/cflint/CFLint/compare/0.4...CFLint-0.3.1)

## [0.4](https://github.com/cflint/CFLint/tree/0.4) (2015-02-06)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.3...0.4)

## [CFLint-0.3](https://github.com/cflint/CFLint/tree/CFLint-0.3) (2015-02-06)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.4...CFLint-0.3)

## [CFLint-0.4](https://github.com/cflint/CFLint/tree/CFLint-0.4) (2015-02-06)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.3.0...CFLint-0.4)

## [CFLint-0.3.0](https://github.com/cflint/CFLint/tree/CFLint-0.3.0) (2015-02-05)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.2.0...CFLint-0.3.0)

**Implemented enhancements:**

- Excess Libraries [\#16](https://github.com/cflint/CFLint/issues/16)

**Fixed bugs:**

- CFParser Dependencies Not Resolved [\#18](https://github.com/cflint/CFLint/issues/18)

**Closed issues:**

- Trying out the configfile from the command line and getting errors [\#31](https://github.com/cflint/CFLint/issues/31)
- NESTED\_CFOUTPUT false positive [\#30](https://github.com/cflint/CFLint/issues/30)
- Exclude WEB-INF folder [\#29](https://github.com/cflint/CFLint/issues/29)
- cflint --version returns error [\#28](https://github.com/cflint/CFLint/issues/28)
- Maven build fails with error. [\#27](https://github.com/cflint/CFLint/issues/27)
-  QUERYPARAM\_REQ message should reflect the cf tag version not the cf script version. [\#26](https://github.com/cflint/CFLint/issues/26)
- Default cflintexclude.json should only ignore MISSING\_VAR's in init\(\) functions, other violations should NOT be ignored [\#25](https://github.com/cflint/CFLint/issues/25)
- \<cfset/\> on multiple lines does not process [\#21](https://github.com/cflint/CFLint/issues/21)
- Convert bugs.add\(\) to a plugin format. [\#19](https://github.com/cflint/CFLint/issues/19)
- Lint Error Question [\#17](https://github.com/cflint/CFLint/issues/17)
- Add the antlr grammar code generation to the maven build. [\#15](https://github.com/cflint/CFLint/issues/15)

## [CFLint-0.2.0](https://github.com/cflint/CFLint/tree/CFLint-0.2.0) (2014-08-12)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.1.7...CFLint-0.2.0)

**Closed issues:**

- Machine independent new lines [\#13](https://github.com/cflint/CFLint/issues/13)
- Docs for using linter via java [\#12](https://github.com/cflint/CFLint/issues/12)
- Typo in read me \(and logo\) [\#10](https://github.com/cflint/CFLint/issues/10)
- compat issue with Local History package [\#9](https://github.com/cflint/CFLint/issues/9)
- does not support tagless components [\#8](https://github.com/cflint/CFLint/issues/8)
- Add severity level to each issue in stdout [\#7](https://github.com/cflint/CFLint/issues/7)
- Add -version flag [\#6](https://github.com/cflint/CFLint/issues/6)
- It would be useful to have a -q \(--quiet\) option [\#4](https://github.com/cflint/CFLint/issues/4)
- It should be possible to specifiy just one file to be scanned. [\#3](https://github.com/cflint/CFLint/issues/3)
- Specifying -textfile as an argument, implies -text should be set. [\#2](https://github.com/cflint/CFLint/issues/2)

## [CFLint-0.1.7](https://github.com/cflint/CFLint/tree/CFLint-0.1.7) (2014-04-03)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.1.8...CFLint-0.1.7)

## [CFLint-0.1.8](https://github.com/cflint/CFLint/tree/CFLint-0.1.8) (2014-04-03)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.1.6...CFLint-0.1.8)

**Closed issues:**

- ignore .cfm~ files [\#1](https://github.com/cflint/CFLint/issues/1)

## [CFLint-0.1.6](https://github.com/cflint/CFLint/tree/CFLint-0.1.6) (2014-04-01)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.1.5...CFLint-0.1.6)

## [CFLint-0.1.5](https://github.com/cflint/CFLint/tree/CFLint-0.1.5) (2014-02-08)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.1.4...CFLint-0.1.5)

## [CFLint-0.1.4](https://github.com/cflint/CFLint/tree/CFLint-0.1.4) (2013-12-27)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.1.3...CFLint-0.1.4)

## [CFLint-0.1.3](https://github.com/cflint/CFLint/tree/CFLint-0.1.3) (2013-12-19)
[Full Changelog](https://github.com/cflint/CFLint/compare/CFLint-0.1.2...CFLint-0.1.3)

## [CFLint-0.1.2](https://github.com/cflint/CFLint/tree/CFLint-0.1.2) (2013-12-06)
[Full Changelog](https://github.com/cflint/CFLint/compare/0.1.0...CFLint-0.1.2)

## [0.1.0](https://github.com/cflint/CFLint/tree/0.1.0) (2013-11-23)


\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*

\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*

\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*

\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*

\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*

\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*

\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*
