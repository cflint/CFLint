List of built-in rules and rule groups
======================================
## Rule Parameters 
<br>FunctionLengthChecker.length = *100*
<br>ComponentLengthChecker.length = *500*
<br>TooManyArgumentsChecker.maximum = *10*
<br>TooManyFunctionsChecker.maximum = *10*
<br>SimpleComplexityChecker.maximum = *10*
<br>VariableNameChecker.minLength = *3*
<br>VariableNameChecker.maxLength = *20*
<br>VariableNameChecker.maxWords = *4*
<br>VariableNameChecker.ignoreUpperCaseScopes = *CGI,URL*
<br>VariableNameChecker.ignoreAllCapsInScopes = *this,variables*
<br>VariableNameChecker.ignorePrefixPostfixOn = *thisTag*
<br>VariableNameChecker.case = *camelCase*
<br>ArgumentNameChecker.minLength = *3*
<br>ArgumentNameChecker.maxLength = *20*
<br>ArgumentNameChecker.maxWords = *4*
<br>ArgumentNameChecker.case = *camelCase*
<br>MethodNameChecker.minLength = *3*
<br>MethodNameChecker.maxLength = *25*
<br>MethodNameChecker.maxWords = *5*
<br>MethodNameChecker.case = *camelCase*
<br>ComponentNameChecker.minLength = *3*
<br>ComponentNameChecker.maxLength = *15*
<br>ComponentNameChecker.maxWords = *3*
<br>ComponentNameChecker.case = *PascalCase*
<br>GlobalLiteralChecker.maximum = *3*
<br>GlobalLiteralChecker.maxWarnings = *5*
<br>GlobalLiteralChecker.warningScope = *global*
<br>GlobalLiteralChecker.ignoreWords = *numeric,text,textnocase,asc,desc,in,out,inout,one,all,bigdecimal,boolean,byte,char,int,long,float,double,short,string,null*
<br>LocalLiteralChecker.maximum = *3*
<br>LocalLiteralChecker.maxWarnings = *5*
<br>LocalLiteralChecker.warningScope = *local*
<br>LocalLiteralChecker.ignoreWords = *numeric,text,textnocase,asc,desc,in,out,inout,one,all,bigdecimal,boolean,byte,char,int,long,float,double,short,string,null*
## Built-in rules
* ArgDefChecker
    * ARG_DEFAULT_MISSING - Optional argument is missing a default value.  *WARNING*
        * Argument *variable* is not required and does not define a default value.
* ArgVarChecker
    * ARG_VAR_CONFLICT - Variable declared in both local and argument scopes.  *ERROR*
        * Variable *variable* should not be declared in both local and argument scopes.
    * ARG_VAR_MIXED - Variable referenced in local and argument scopes.  *INFO*
        * Variable *variable* should not be referenced in local and argument scope.
* CFSwitchDefaultChecker
    * NO_DEFAULT_INSIDE_SWITCH - Missing default switch statement.  *WARNING*
        * Not having a Default statement defined for a switch could pose potential issues.
* GlobalVarChecker
    * GLOBAL_VAR - Global variable exists.  *WARNING*
        * Identifier *variable* is global. Referencing in a CFC or function should be avoided.
* NestedCFOutput
    * NESTED_CFOUTPUT - Nexted cfoutput with cfquery tag.  *ERROR*
        * Nested CFOutput, outer CFOutput has @query.
* OutputParmMissing
    * OUTPUT_ATTR - Tag should have output='false'.  *INFO*
        * &lt;*tag* name="*variable*"&gt; should have @output='false'
* QueryParamChecker
    * QUERYPARAM_REQ - SetSql() statement should use .addParam().  *WARNING*
        * setSql() statement should use .addParam() instead of #'s name="*variable*"
    * CFQUERYPARAM_REQ - cfquery should use &lt;cfqueryparam&gt;.  *WARNING*
        * &lt;*tag*&gt; should use &lt;cfqueryparam/&gt; for variable '*variable*'.
* TypedQueryNew
    * QUERYNEW_DATATYPE - QueryNew statement should specify datatypes.  *WARNING*
        * QueryNew statement should specify datatypes.
* VarScoper
    * MISSING_VAR - Variable is not declared with a var statement.  *ERROR*
        * Variable *variable* is not declared with a var statement.
* CFDumpChecker
    * Parameters
        * tagName = *cfdump*
    * AVOID_USING_CFDUMP_TAG - Avoid use of cfdump tags.  *WARNING*
        * Avoid leaving &lt;cfdump&gt; tags in committed code. Debug information should be omitted from release code
* CFExecuteChecker
    * Parameters
        * tagName = *cfexecute*
    * AVOID_USING_CFEXECUTE_TAG - Avoid use of cfexecute tags.  *WARNING*
        * Avoid leaving &lt;cfexecute&gt; tags in committed code. CFexecute can be used as an attack vector and is slow.
* CFBuiltInFunctionChecker
    * AVOID_USING_ISDATE -   *WARNING*
        * Avoid using the isDate built-in function.  It is too permissive.  Use isValid() instead.
* CFAbortChecker
    * Parameters
        * tagName = *cfabort*
    * AVOID_USING_CFABORT_TAG - Avoid use of cfabort tags.  *INFO*
        * Avoid leaving &lt;cfabort&gt; tags in committed code.
* AbortChecker
    * AVOID_USING_ABORT - Avoid use of abort statements.  *INFO*
        * Avoid using abort in production code.
* CFInsertChecker
    * Parameters
        * tagName = *cfinsert*
    * AVOID_USING_CFINSERT_TAG - Avoid use of cfinsert tags.  *WARNING*
        * Avoid using &lt;cfinsert&gt; tags. Use cfquery and cfstoredproc instead.
* CFModuleChecker
    * Parameters
        * tagName = *cfmodule*
    * AVOID_USING_CFMODULE_TAG - Avoid use of cfmodule tags.  *WARNING*
        * Avoid using &lt;cfmodule&gt; tags.
* CFUpdateChecker
    * Parameters
        * tagName = *cfupdate*
    * AVOID_USING_CFUPDATE_TAG - Avoid use of cfupdate tags.  *WARNING*
        * Avoid using &lt;cfupdate&gt; tags. Use cfquery and cfstoredproc instead.
* CFIncludeChecker
    * Parameters
        * tagName = *cfinclude*
        * scope = *component*
    * AVOID_USING_CFINCLUDE_TAG - Avoid use of cfinclude tags.  *WARNING*
        * Avoid using &lt;cfinclude&gt; tags. Use components instead.
* ComponentHintChecker
    * COMPONENT_HINT_MISSING - Component is missing a hint.  *WARNING*
        * Component *variable* is missing a hint.
* FunctionHintChecker
    * FUNCTION_HINT_MISSING - Function is missing a hint.  *INFO*
        * Function *variable* is missing a hint.
* ArgumentHintChecker
    * ARG_HINT_MISSING - Argument is missing a hint.  *INFO*
        * Argument *variable* is missing a hint.
    * ARG_HINT_MISSING_SCRIPT -   *INFO*
        * Argument *variable* is missing a hint.  Use javadoc style annotations on cfscript functions.
* ArgumentTypeChecker
    * ARG_TYPE_MISSING - Component is missing a type.  *WARNING*
        * Argument *variable* is missing a type.
    * ARG_TYPE_ANY - Component is of type any.  *WARNING*
        * Argument *variable* is any. Please change to be a more specific type.
* FunctionLengthChecker
    * Parameters
        * length = *100*
    * EXCESSIVE_FUNCTION_LENGTH - Method is too long.  *WARNING*
        * Function *function* is *variable* lines. Should be fewer than 100 lines.
* ComponentLengthChecker
    * Parameters
        * length = *500*
    * EXCESSIVE_COMPONENT_LENGTH - Component is too long.  *WARNING*
        * Component *component* is *variable* lines. Should be fewer than 500 lines.
* FunctionTypeChecker
    * FUNCTION_TYPE_MISSING - Function is missing a return type.  *WARNING*
        * Function *variable* is missing a return type.
    * FUNCTION_TYPE_ANY - Function has a return type of any.  *INFO*
        * Function *variable* return type is any. Please change to be a more specific type.
* TooManyArgumentsChecker
    * Parameters
        * maximum = *10*
    * EXCESSIVE_ARGUMENTS - Function has too many arguments.  *WARNING*
        * Function *function* has too many arguments. Should be fewer than 10.
* TooManyFunctionsChecker
    * Parameters
        * maximum = *10*
    * EXCESSIVE_FUNCTIONS - Too many functions.  *WARNING*
        * Component *component* has too many functions. Should be fewer than 10.
* SimpleComplexityChecker
    * Parameters
        * maximum = *10*
    * FUNCTION_TOO_COMPLEX - Function is too complex.  *WARNING*
        * Function *function* is too complex. Consider breaking the function into smaller functions.
* WriteDumpChecker
    * Parameters
        * functionName = *writeDump*
    * AVOID_USING_WRITEDUMP - Avoid use of writedump statements.  *INFO*
        * Avoid using the writeDump function in production code.
* StructNewChecker
    * Parameters
        * functionName = *structNew*
    * AVOID_USING_STRUCTNEW - Avoid use of structnew statements. Use {} instead.  *INFO*
        * Avoid using the structNew function in production code.
* IsDebugModeChecker
    * Parameters
        * functionName = *IsDebugMode*
    * AVOID_USING_ISDEBUGMODE - Avoid use of isdebugmode statements.  *WARNING*
        * Avoid using the IsDebugMode function in production code.
* ArrayNewChecker
    * AVOID_USING_ARRAYNEW - Avoid use of arraynew statements. Use [] instead.  *INFO*
        * Use implict array construction instead (= []).
* ComplexBooleanExpressionChecker
    * COMPLEX_BOOLEAN_CHECK - Complex boolean expression.  *WARNING*
        * Boolean expression is too complex. Consider simplifying or moving to a named method.
* BooleanExpressionChecker
    * EXPLICIT_BOOLEAN_CHECK - Checking boolean expression explicitly.  *INFO*
        * Explicit check of boolean expression is not needed.
* VariableNameChecker
    * Parameters
        * minLength = *3*
        * maxLength = *20*
        * maxWords = *4*
        * ignoreUpperCaseScopes = *CGI,URL*
        * ignoreAllCapsInScopes = *this,variables*
        * ignorePrefixPostfixOn = *thisTag*
        * case = *camelCase*
    * VAR_INVALID_NAME - Variable has invalid name.  *INFO*
        * Variable *variable* is not a valid name. Please use camelCase or underscores.
    * VAR_ALLCAPS_NAME - Variable name is allcaps.  *INFO*
        * Variable *variable* should not be upper case.
    * SCOPE_ALLCAPS_NAME - Variable scope name is allcaps.  *INFO*
        * Scope *variable* should not be upper case.
    * VAR_TOO_SHORT - Variable name is too short.  *INFO*
        * Variable *variable* should be longer than 3 characters.
    * VAR_TOO_LONG - Variable name is too long.  *INFO*
        * Variable *variable* should be shorter than 20 characters.
    * VAR_TOO_WORDY - Variable name contain too many words.  *INFO*
        * Variable *variable* is too wordy. Try to think of a more concise name.
    * VAR_IS_TEMPORARY - Variable name looks temporary.  *INFO*
        * Temporary variable *variable* could be named better.
    * VAR_HAS_PREFIX_OR_POSTFIX - Variable name has prefix or postfix.  *INFO*
        * Variable has prefix or postfix *variable* and could be named better.
* ArgumentNameChecker
    * Parameters
        * minLength = *3*
        * maxLength = *20*
        * maxWords = *4*
        * case = *camelCase*
    * ARGUMENT_MISSING_NAME -   *INFO*
        * Argument is missing a name.
    * ARGUMENT_INVALID_NAME - Argument has invalid name.  *INFO*
        * Argument *variable* is not a valid name. Please use camelCase or underscores.
    * ARGUMENT_ALLCAPS_NAME - Argument name is allcaps.  *INFO*
        * Argument *variable* should not be upper case.
    * ARGUMENT_TOO_SHORT - Argument name is too short.  *INFO*
        * Argument *variable* should be longer than 3 characters.
    * ARGUMENT_TOO_LONG - Argument name is too long.  *INFO*
        * Argument *variable* should be shorter than 20 characters.
    * ARGUMENT_TOO_WORDY - Argument name contain too many words.  *INFO*
        * Argument *variable* is too wordy. Try to think of a more concise name.
    * ARGUMENT_IS_TEMPORARY - Argument name looks temporary.  *INFO*
        * Temporary argument *variable* could be named better.
    * ARGUMENT_HAS_PREFIX_OR_POSTFIX - Argument name has prefix or postfix.  *INFO*
        * Argument has prefix or postfix *variable* and could be named better.
* MethodNameChecker
    * Parameters
        * minLength = *3*
        * maxLength = *25*
        * maxWords = *5*
        * case = *camelCase*
    * METHOD_INVALID_NAME - Method has invalid name.  *INFO*
        * Method name *function* is not a valid name. Please use camelCase or underscores.
    * METHOD_ALLCAPS_NAME - Method name is allcaps.  *INFO*
        * Method name *function* should not be upper case.
    * METHOD_TOO_SHORT - Method name is too short.  *INFO*
        * Method name *function* should be longer than 3 characters.
    * METHOD_TOO_LONG - Method name is too long.  *INFO*
        * Method name *function* should be shorter than 25 characters.
    * METHOD_TOO_WORDY - Method name contain too many words.  *INFO*
        * Method name *function* is too wordy. Try to think of a more concise name.
    * METHOD_IS_TEMPORARY - Method name looks temporary.  *INFO*
        * Method name *function* could be named better.
    * METHOD_HAS_PREFIX_OR_POSTFIX - Method name has prefix or postfix.  *INFO*
        * Method name *function* has prefix or postfix and could be named better.
* ComponentNameChecker
    * Parameters
        * minLength = *3*
        * maxLength = *15*
        * maxWords = *3*
        * case = *PascalCase*
    * COMPONENT_INVALID_NAME - Component has invalid name.  *INFO*
        * Component name *component* is not a valid name. Please use PascalCase and start with a capital letter.
    * COMPONENT_ALLCAPS_NAME - Component name is allcaps.  *INFO*
        * Component name *component* should not be all upper case.
    * COMPONENT_TOO_SHORT - Component name is too short.  *INFO*
        * Component name *component* should be longer than 3 characters.
    * COMPONENT_TOO_LONG - Component name is too long.  *INFO*
        * Component name *component* should be shorter than 15 characters.
    * COMPONENT_TOO_WORDY - Component name contain too many words.  *INFO*
        * Component name *component* is too wordy. Try to think of a more concise name.
    * COMPONENT_IS_TEMPORARY - Component name looks temporary.  *INFO*
        * Component name *component* could be named better.
    * COMPONENT_HAS_PREFIX_OR_POSTFIX - Component name has prefix or postfix.  *INFO*
        * Component name *component* has prefix or postfix and could be named better.
* FileCaseChecker
    * FILE_SHOULD_START_WITH_LOWERCASE - CFM File starts with upper case.  *INFO*
        * File *filename* starts with an upper case letter. Only components (.cfc files) should start with an upper case letter.
* CreateObjectChecker
    * AVOID_USING_CREATEOBJECT - Avoid use of creatobject statements.  *INFO*
        * CreateObject found.  Use createObject(path_to_component) or even better new path_to_component().
* CFDebugAttributeChecker
    * AVOID_USING_DEBUG_ATTR - Avoid use of debug attribute.  *WARNING*
        * Avoid leaving debug attribute on tags.
    * AVOID_USING_CFSETTING_DEBUG - Avoid using showDebugOutput attribute on cfsetting.  *WARNING*
        * Avoid using showDebugOutput attribute on cfsetting.
* UnusedLocalVarChecker
    * Parameters
        * usedTagAttributes = *[cfquery/name, cfloop/index, cfloop/item, cfchart/name, cfdocument/name, cfftp/name, cfhtmltopdf/name, cfhttp/resultname, cfimage/name, cfimap/name, cfldap/name, cfoutput/query, cfpdf/name, cfreport/name, cfsavecontent/name, cfstoreproc/result, cfxml/variable]*
    * UNUSED_LOCAL_VARIABLE - Unused local variable.  *INFO*
        * Local variable *variable* is not used in function *function*. Consider removing it.
* UnusedArgumentChecker
    * UNUSED_METHOD_ARGUMENT - Unused method argument.  *INFO*
        * Argument *variable* is not used in function. Consider removing it.
* CFCompareVsAssignChecker
    * COMPARE_INSTEAD_OF_ASSIGN - Using comparision where assignment was probably meant.  *WARNING*
        * CWE-482: Comparing instead of Assigning
* StructKeyChecker
    * UNQUOTED_STRUCT_KEY -   *WARNING*
        * Unquoted struct key *variable* is not case-sensitive.  Quoting it is recommended.
    * STRUCT_ARRAY_NOTATION -   *WARNING*
        * Unquoted struct key *variable* is not case-sensitive.  Using array notation is recommended.
* SelectStarChecker
    * SQL_SELECT_STAR -   *WARNING*
        * Avoid using 'select *' in a query.
* CFQueryChecker
    * NEVER_USE_QUERY_IN_CFM -   **
        * Don't use &lt;cfquery&gt; in .cfm files. Database should not be coupled with view.
* ComponentDisplayNameChecker
    * USE_DISPLAY_NAME -   *INFO*
        * Component *variable* has a name attribute, but perhaps you meant to use displayName.
* GlobalLiteralChecker
    * Parameters
        * maximum = *3*
        * maxWarnings = *5*
        * warningScope = *global*
        * ignoreWords = *numeric,text,textnocase,asc,desc,in,out,inout,one,all,bigdecimal,boolean,byte,char,int,long,float,double,short,string,null*
    * GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN -   *WARNING*
        * Literal *variable* occurs several times in one or more files. Consider giving it a name and not hard coding values.
* LocalLiteralChecker
    * Parameters
        * maximum = *3*
        * maxWarnings = *5*
        * warningScope = *local*
        * ignoreWords = *numeric,text,textnocase,asc,desc,in,out,inout,one,all,bigdecimal,boolean,byte,char,int,long,float,double,short,string,null*
    * LOCAL_LITERAL_VALUE_USED_TOO_OFTEN -   *WARNING*
        * Literal *variable* occurs several times in the same file. Consider giving it a name and not hard coding values.
* PackageCaseChecker
    * PACKAGE_CASE_MISMATCH - Warn with package case does not match its use.  *WARNING*
        * The case of the package folder and the object declaration do not match for *variable*.
## Rule Groups
### BugProne
 * ARG_VAR_CONFLICT   *ERROR*
 * NO_DEFAULT_INSIDE_SWITCH   *WARNING*
 * NESTED_CFOUTPUT   *ERROR*
 * OUTPUT_ATTR   *INFO*
 * MISSING_VAR   *ERROR*
 * COMPARE_INSTEAD_OF_ASSIGN   *WARNING*
 * AVOID_USING_ISDATE   *WARNING*
### Correctness
 * ARG_DEFAULT_MISSING   *WARNING*
 * ARG_TYPE_ANY   *WARNING*
 * ARG_TYPE_MISSING   *WARNING*
 * ARG_VAR_MIXED   *INFO*
 * QUERYNEW_DATATYPE   *WARNING*
 * UNUSED_LOCAL_VARIABLE   *INFO*
 * UNUSED_METHOD_ARGUMENT   *INFO*
 * UNQUOTED_STRUCT_KEY   *WARNING*
 * STRUCT_ARRAY_NOTATION   *WARNING*
 * USE_DISPLAY_NAME   *INFO*
### BadPractice
 * AVOID_USING_ABORT   *INFO*
 * AVOID_USING_CFABORT_TAG   *INFO*
 * AVOID_USING_CFDUMP_TAG   *WARNING*
 * AVOID_USING_CFEXECUTE_TAG   *WARNING*
 * AVOID_USING_CFINSERT_TAG   *WARNING*
 * AVOID_USING_CFMODULE_TAG   *WARNING*
 * AVOID_USING_CFUPDATE_TAG   *WARNING*
 * AVOID_USING_WRITEDUMP   *INFO*
 * GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN   *WARNING*
 * GLOBAL_VAR   *WARNING*
 * LOCAL_LITERAL_VALUE_USED_TOO_OFTEN   *WARNING*
 * SQL_SELECT_STAR   *WARNING*
 * AVOID_USING_DEBUG_ATTR   *WARNING*
 * AVOID_USING_CFSETTING_DEBUG   *WARNING*
 * AVOID_USING_CFINCLUDE_TAG   *WARNING*
 * AVOID_USING_ISDEBUGMODE   *WARNING*
### Security
 * CFQUERYPARAM_REQ   *WARNING*
 * QUERYPARAM_REQ   *WARNING*
### CodeStyle
 * ARG_HINT_MISSING   *INFO*
 * COMPONENT_HINT_MISSING   *WARNING*
 * FUNCTION_HINT_MISSING   *INFO*
 * FUNCTION_TYPE_ANY   *INFO*
 * FUNCTION_TYPE_MISSING   *WARNING*
 * ARG_HINT_MISSING_SCRIPT   *INFO*
### ModernSyntax
 * AVOID_USING_ARRAYNEW   *INFO*
 * AVOID_USING_STRUCTNEW   *INFO*
 * AVOID_USING_CREATEOBJECT   *INFO*
### Complexity
 * COMPLEX_BOOLEAN_CHECK   *WARNING*
 * EXCESSIVE_FUNCTIONS   *WARNING*
 * EXCESSIVE_ARGUMENTS   *WARNING*
 * EXPLICIT_BOOLEAN_CHECK   *INFO*
 * EXCESSIVE_COMPONENT_LENGTH   *WARNING*
 * EXCESSIVE_FUNCTION_LENGTH   *WARNING*
 * FUNCTION_TOO_COMPLEX   *WARNING*
### Naming
 * METHOD_HAS_PREFIX_OR_POSTFIX   *INFO*
 * METHOD_INVALID_NAME   *INFO*
 * METHOD_IS_TEMPORARY   *INFO*
 * METHOD_TOO_SHORT   *INFO*
 * METHOD_TOO_LONG   *INFO*
 * METHOD_TOO_WORDY   *INFO*
 * VAR_ALLCAPS_NAME   *INFO*
 * VAR_HAS_PREFIX_OR_POSTFIX   *INFO*
 * VAR_INVALID_NAME   *INFO*
 * VAR_IS_TEMPORARY   *INFO*
 * VAR_TOO_SHORT   *INFO*
 * VAR_TOO_LONG   *INFO*
 * VAR_TOO_WORDY   *INFO*
 * SCOPE_ALLCAPS_NAME   *INFO*
 * ARGUMENT_MISSING_NAME   *INFO*
 * ARGUMENT_INVALID_NAME   *INFO*
 * ARGUMENT_ALLCAPS_NAME   *INFO*
 * ARGUMENT_TOO_SHORT   *INFO*
 * ARGUMENT_TOO_LONG   *INFO*
 * ARGUMENT_TOO_WORDY   *INFO*
 * ARGUMENT_IS_TEMPORARY   *INFO*
 * ARGUMENT_HAS_PREFIX_OR_POSTFIX   *INFO*
 * METHOD_ALLCAPS_NAME   *INFO*
 * COMPONENT_INVALID_NAME   *INFO*
 * COMPONENT_ALLCAPS_NAME   *INFO*
 * COMPONENT_TOO_SHORT   *INFO*
 * COMPONENT_TOO_LONG   *INFO*
 * COMPONENT_TOO_WORDY   *INFO*
 * COMPONENT_IS_TEMPORARY   *INFO*
 * COMPONENT_HAS_PREFIX_OR_POSTFIX   *INFO*
 * PACKAGE_CASE_MISMATCH   *WARNING*
### Experimental
 * NEVER_USE_QUERY_IN_CFM   **
 * FILE_SHOULD_START_WITH_LOWERCASE   *INFO*

