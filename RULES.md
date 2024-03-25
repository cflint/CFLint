# Built-in Rules

## Rule List

### ArgDefChecker

#### ARG_DEFAULT_MISSING

Optional argument is missing a default value.

**Severity**: WARNING

**Message**: Argument *variable* is not required and does not define a default value.

### ArgVarChecker

#### ARG_VAR_CONFLICT

Variable declared in both local and argument scopes.

**Severity**: ERROR

**Message**: Variable *variable* should not be declared in both local and argument scopes.

#### ARG_VAR_MIXED

Variable referenced in local and argument scopes.

**Severity**: INFO

**Message**: Variable *variable* should not be referenced in local and argument scope.

### CFSwitchDefaultChecker

#### NO_DEFAULT_INSIDE_SWITCH

Missing default switch statement.

**Severity**: WARNING

**Message**: Not having a Default statement defined for a switch could pose potential issues.

### GlobalVarChecker

#### GLOBAL_VAR

Global variable exists.

**Severity**: WARNING

**Message**: Identifier *variable* is global. Referencing in a CFC or function should be avoided.

### NestedCFOutput

#### NESTED_CFOUTPUT

Nested cfoutput with cfquery tag.

**Severity**: ERROR

**Message**: Nested CFOutput, outer CFOutput has @query.

### OutputParmMissing

#### OUTPUT_ATTR

Tag should have output='false'.

**Severity**: INFO

**Message**: &lt;*tag* name="*variable*"&gt; should have @output='false'

### QueryParamChecker

#### QUERYPARAM_REQ

Use query parameters for variables in sql statements.

**Severity**: WARNING

**Message**: Use query parameters for variables in sql statements.

#### CFQUERYPARAM_REQ

cfquery should use &lt;cfqueryparam&gt;.

**Severity**: WARNING

**Message**: &lt;*tag*&gt; should use &lt;cfqueryparam/&gt; for variable '*variable*'.

### TypedQueryNew

#### QUERYNEW_DATATYPE

QueryNew statement should specify data types.

**Severity**: WARNING

**Message**: QueryNew statement should specify datatypes.

### VarScoper

#### MISSING_VAR

Variable is not declared with a var statement.

**Severity**: ERROR

**Message**: Variable *variable* is not declared with a var statement.

### CFDumpChecker

#### AVOID_USING_CFDUMP_TAG

Avoid use of cfdump tags.

**Severity**: WARNING

**Message**: Avoid leaving &lt;cfdump&gt; tags in committed code. Debug information should be omitted from release code

### CFExecuteChecker

#### AVOID_USING_CFEXECUTE_TAG

Avoid use of cfexecute tags.

**Severity**: WARNING

**Message**: Avoid leaving &lt;cfexecute&gt; tags in committed code. CFexecute can be used as an attack vector and is slow.

### CFBuiltInFunctionChecker

#### AVOID_USING_ISDATE

**Severity**: WARNING

**Message**: Avoid using the isDate built-in function.  It is too permissive.  Use isValid() instead.

### CFAbortChecker

#### AVOID_USING_CFABORT_TAG

Avoid use of cfabort tags.

**Severity**: INFO

**Message**: Avoid leaving &lt;cfabort&gt; tags in committed code.

### AbortChecker

#### AVOID_USING_ABORT

Avoid use of abort statements.

**Severity**: INFO

**Message**: Avoid using abort in production code.

### CFInsertChecker

#### AVOID_USING_CFINSERT_TAG

Avoid use of cfinsert tags.

**Severity**: WARNING

**Message**: Avoid using &lt;cfinsert&gt; tags. Use cfquery and cfstoredproc instead.

### CFModuleChecker

#### AVOID_USING_CFMODULE_TAG

Avoid use of cfmodule tags.

**Severity**: WARNING

**Message**: Avoid using &lt;cfmodule&gt; tags.

### CFUpdateChecker

#### AVOID_USING_CFUPDATE_TAG

Avoid use of cfupdate tags.

**Severity**: WARNING

**Message**: Avoid using &lt;cfupdate&gt; tags. Use cfquery and cfstoredproc instead.

### CFIncludeChecker

#### AVOID_USING_CFINCLUDE_TAG

Avoid use of cfinclude tags.

**Severity**: WARNING

**Message**: Avoid using &lt;cfinclude&gt; tags. Use components instead.

### ComponentHintChecker

#### COMPONENT_HINT_MISSING

Component is missing a hint.

**Severity**: WARNING

**Message**: Component *variable* is missing a hint.

### FunctionHintChecker

#### FUNCTION_HINT_MISSING

Function is missing a hint.

**Severity**: INFO

**Message**: Function *variable* is missing a hint.

### ArgumentHintChecker

#### ARG_HINT_MISSING

Argument is missing a hint.

**Severity**: INFO

**Message**: Argument *variable* is missing a hint.

#### ARG_HINT_MISSING_SCRIPT

**Severity**: INFO

**Message**: Argument *variable* is missing a hint.  Use javadoc style annotations on cfscript functions.

### ArgumentTypeChecker

#### ARG_TYPE_MISSING

Component is missing a type.

**Severity**: WARNING

**Message**: Argument *variable* is missing a type.

#### ARG_TYPE_ANY

Component is of type any.

**Severity**: WARNING

**Message**: Argument *variable* is any. Please change to be a more specific type.

### FunctionLengthChecker

#### EXCESSIVE_FUNCTION_LENGTH

Method is too long.

**Severity**: WARNING

**Message**: Function *function* is *variable* lines. Should be fewer than 100 lines.

#### FunctionLengthChecker Parameters

* length = *100*

### ComponentLengthChecker

#### EXCESSIVE_COMPONENT_LENGTH

Component is too long.

**Severity**: WARNING

**Message**: Component *component* is *variable* lines. Should be fewer than 500 lines.

#### ComponentLengthChecker Parameters

* length = *500*

### FunctionTypeChecker

#### FUNCTION_TYPE_MISSING

Function is missing a return type.

**Severity**: WARNING

**Message**: Function *variable* is missing a return type.

#### FUNCTION_TYPE_ANY

Function has a return type of any.

**Severity**: INFO

**Message**: Function *variable* return type is any. Please change to be a more specific type.

### TooManyArgumentsChecker

#### EXCESSIVE_ARGUMENTS

Function has too many arguments.

**Severity**: WARNING

**Message**: Function *function* has too many arguments. Should be fewer than 10.

#### TooManyArgumentsChecker Parameters

* maximum = *10*

### TooManyFunctionsChecker

#### EXCESSIVE_FUNCTIONS

Too many functions.

**Severity**: WARNING

**Message**: Component *component* has too many functions. Should be fewer than 10.

#### TooManyFunctionsChecker Parameters

* maximum = *10*

### SimpleComplexityChecker

#### FUNCTION_TOO_COMPLEX

Function is too complex.

**Severity**: WARNING

**Message**: Function *function* is too complex. Consider breaking the function into smaller functions.

#### SimpleComplexityChecker Parameters

* maximum = *10*

### QueryNewChecker

#### QUERYNEW_DUPLICATE_COLUMNS

**Severity**: ERROR

**Message**: QueryNew declares column *variable* multiple times, this is a hard error in some CFML implementations.

### WriteDumpChecker

#### AVOID_USING_WRITEDUMP

Avoid use of writeDump statements.

**Severity**: INFO

**Message**: Avoid using the writeDump function in production code.

### StructNewChecker

#### AVOID_USING_STRUCTNEW

Avoid use of structNew statements. Use {} instead.

**Severity**: INFO

**Message**: Avoid using the structNew function in production code.

### IsDebugModeChecker

#### AVOID_USING_ISDEBUGMODE

Avoid use of isDebugMode statements.

**Severity**: WARNING

**Message**: Avoid using the IsDebugMode function in production code.

### FunctionCollisionChecker

#### FUNCTION_NAME_COLLISION

**Severity**: WARNING

**Message**: Avoid using the name *variable* for a function.  It is reserved in some CFML implementations.  See https://cfdocs.org/*variable*

### ArrayNewChecker

#### AVOID_USING_ARRAYNEW

Avoid use of arrayNew statements. Use [] instead.

**Severity**: INFO

**Message**: Use implict array construction instead (= []).

### ComplexBooleanExpressionChecker

#### COMPLEX_BOOLEAN_CHECK

Complex boolean expression.

**Severity**: WARNING

**Message**: Boolean expression is too complex. Consider simplifying or moving to a named method.

### BooleanExpressionChecker

#### EXPLICIT_BOOLEAN_CHECK

Checking boolean expression explicitly.

**Severity**: INFO

**Message**: Explicit check of boolean expression is not needed.

### VariableNameChecker

#### VAR_INVALID_NAME

Variable has invalid name.

**Severity**: INFO

**Message**: Variable *variable* is not a valid name. Please use camelCase or underscores.

#### VAR_ALLCAPS_NAME

Variable name is all caps.

**Severity**: INFO

**Message**: Variable *variable* should not be upper case.

#### SCOPE_ALLCAPS_NAME

Variable scope name is all caps.

**Severity**: INFO

**Message**: Scope *variable* should not be upper case.

#### VAR_TOO_SHORT

Variable name is too short.

**Severity**: INFO

**Message**: Variable *variable* should be longer than 3 characters.

#### VAR_TOO_LONG

Variable name is too long.

**Severity**: INFO

**Message**: Variable *variable* should be shorter than 20 characters.

#### VAR_TOO_WORDY

Variable name contain too many words.

**Severity**: INFO

**Message**: Variable *variable* is too wordy. Try to think of a more concise name.

#### VAR_IS_TEMPORARY

Variable name looks temporary.

**Severity**: INFO

**Message**: Temporary variable *variable* could be named better.

#### VAR_HAS_PREFIX_OR_POSTFIX

Variable name has prefix or postfix.

**Severity**: INFO

**Message**: Variable has prefix or postfix *variable* and could be named better.

#### VariableNameChecker Parameters

* minLength = *3*

* maxLength = *20*

* maxWords = *4*

* ignoreUpperCaseScopes = *CGI,URL*

* ignoreAllCapsInScopes = *this,variables*

* ignorePrefixPostfixOn = *thisTag*

* case = *camelCase*

### ArgumentNameChecker

#### ARGUMENT_MISSING_NAME

**Severity**: INFO

**Message**: Argument is missing a name.

#### ARGUMENT_INVALID_NAME

Argument has invalid name.

**Severity**: INFO

**Message**: Argument *variable* is not a valid name. Please use camelCase or underscores.

#### ARGUMENT_ALLCAPS_NAME

Argument name is all caps.

**Severity**: INFO

**Message**: Argument *variable* should not be upper case.

#### ARGUMENT_TOO_SHORT

Argument name is too short.

**Severity**: INFO

**Message**: Argument *variable* should be longer than 3 characters.

#### ARGUMENT_TOO_LONG

Argument name is too long.

**Severity**: INFO

**Message**: Argument *variable* should be shorter than 20 characters.

#### ARGUMENT_TOO_WORDY

Argument name contain too many words.

**Severity**: INFO

**Message**: Argument *variable* is too wordy. Try to think of a more concise name.

#### ARGUMENT_IS_TEMPORARY

Argument name looks temporary.

**Severity**: INFO

**Message**: Temporary argument *variable* could be named better.

#### ARGUMENT_HAS_PREFIX_OR_POSTFIX

Argument name has prefix or postfix.

**Severity**: INFO

**Message**: Argument has prefix or postfix *variable* and could be named better.

#### ArgumentNameChecker Parameters

* minLength = *3*

* maxLength = *20*

* maxWords = *4*

* case = *camelCase*

### MethodNameChecker

#### METHOD_INVALID_NAME

Method has invalid name.

**Severity**: INFO

**Message**: Method name *function* is not a valid name. Please use camelCase or underscores.

#### METHOD_ALLCAPS_NAME

Method name is all caps.

**Severity**: INFO

**Message**: Method name *function* should not be upper case.

#### METHOD_TOO_SHORT

Method name is too short.

**Severity**: INFO

**Message**: Method name *function* should be longer than 3 characters.

#### METHOD_TOO_LONG

Method name is too long.

**Severity**: INFO

**Message**: Method name *function* should be shorter than 25 characters.

#### METHOD_TOO_WORDY

Method name contain too many words.

**Severity**: INFO

**Message**: Method name *function* is too wordy. Try to think of a more concise name.

#### METHOD_IS_TEMPORARY

Method name looks temporary.

**Severity**: INFO

**Message**: Method name *function* could be named better.

#### METHOD_HAS_PREFIX_OR_POSTFIX

Method name has prefix or postfix.

**Severity**: INFO

**Message**: Method name *function* has prefix or postfix and could be named better.

#### MethodNameChecker Parameters

* minLength = *3*

* maxLength = *25*

* maxWords = *5*

* case = *camelCase*

### ComponentNameChecker

#### COMPONENT_INVALID_NAME

Component has invalid name.

**Severity**: INFO

**Message**: Component name *component* is not a valid name. Please use PascalCase and start with a capital letter.

#### COMPONENT_ALLCAPS_NAME

Component name is all caps.

**Severity**: INFO

**Message**: Component name *component* should not be all upper case.

#### COMPONENT_TOO_SHORT

Component name is too short.

**Severity**: INFO

**Message**: Component name *component* should be longer than 3 characters.

#### COMPONENT_TOO_LONG

Component name is too long.

**Severity**: INFO

**Message**: Component name *component* should be shorter than 15 characters.

#### COMPONENT_TOO_WORDY

Component name contain too many words.

**Severity**: INFO

**Message**: Component name *component* is too wordy. Try to think of a more concise name.

#### COMPONENT_IS_TEMPORARY

Component name looks temporary.

**Severity**: INFO

**Message**: Component name *component* could be named better.

#### COMPONENT_HAS_PREFIX_OR_POSTFIX

Component name has prefix or postfix.

**Severity**: INFO

**Message**: Component name *component* has prefix or postfix and could be named better.

#### ComponentNameChecker Parameters

* minLength = *3*

* maxLength = *15*

* maxWords = *3*

* case = *PascalCase*

### FileCaseChecker

#### FILE_SHOULD_START_WITH_LOWERCASE

CFM File starts with upper case.

**Severity**: INFO

**Message**: File *filename* starts with an upper case letter. Only components (.cfc files) should start with an upper case letter.

### CreateObjectChecker

#### AVOID_USING_CREATEOBJECT

Avoid use of creatObject statements.

**Severity**: INFO

**Message**: CreateObject found.  Use createObject(path_to_component) or even better new path_to_component().

### CFDebugAttributeChecker

#### AVOID_USING_DEBUG_ATTR

Avoid use of debug attribute.

**Severity**: WARNING

**Message**: Avoid leaving debug attribute on tags.

#### AVOID_USING_CFSETTING_DEBUG

Avoid using showDebugOutput attribute on cfsetting.

**Severity**: WARNING

**Message**: Avoid using showDebugOutput attribute on cfsetting.

### UnusedLocalVarChecker

#### UNUSED_LOCAL_VARIABLE

Unused local variable.

**Severity**: INFO

**Message**: Local variable *variable* is not used in function *function*. Consider removing it.

### UnusedArgumentChecker

#### UNUSED_METHOD_ARGUMENT

Unused method argument.

**Severity**: INFO

**Message**: Argument *variable* is not used in function. Consider removing it.

### CFCompareVsAssignChecker

#### COMPARE_INSTEAD_OF_ASSIGN

Using comparison where assignment was probably meant.

**Severity**: WARNING

**Message**: CWE-482: Comparing instead of Assigning

### StructKeyChecker

#### UNQUOTED_STRUCT_KEY

**Severity**: WARNING

**Message**: Unquoted struct key *variable* is not case-sensitive.  Quoting it is recommended.

#### STRUCT_ARRAY_NOTATION

**Severity**: WARNING

**Message**: Unquoted struct key *variable* is not case-sensitive.  Using array notation is recommended.

### SelectStarChecker

#### SQL_SELECT_STAR

**Severity**: WARNING

**Message**: Avoid using 'select *' in a query.

### CFQueryChecker

#### NEVER_USE_QUERY_IN_CFM

**Message**: Don't use &lt;cfquery&gt; in .cfm files. Database should not be coupled with view.

### ComponentDisplayNameChecker

#### USE_DISPLAY_NAME

**Severity**: INFO

**Message**: Component *variable* has a name attribute, but perhaps you meant to use displayName.

### GlobalLiteralChecker

#### GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN

**Severity**: INFO

**Message**: Literal *variable* occurs several times in one or more files. Consider giving it a name and not hard coding values.

#### GlobalLiteralChecker Parameters

* maximum = *3*

* maxWarnings = *5*

* warningScope = *global*

* ignoreWords = *numeric,text,textnocase,asc,desc,in,out,inout,one,all,bigdecimal,boolean,byte,char,int,long,float,double,short,string,null*

### LocalLiteralChecker

#### LOCAL_LITERAL_VALUE_USED_TOO_OFTEN

**Severity**: INFO

**Message**: Literal *variable* occurs several times in the same file. Consider giving it a name and not hard coding values.

#### LocalLiteralChecker Parameters

* maximum = *3*

* maxWarnings = *5*

* warningScope = *local*

* ignoreWords = *numeric,text,textnocase,asc,desc,in,out,inout,one,all,bigdecimal,boolean,byte,char,int,long,float,double,short,string,null*

### PackageCaseChecker

#### PACKAGE_CASE_MISMATCH

Warn with package case does not match its use.

**Severity**: WARNING

**Message**: The case of the package folder and the object declaration do not match for *variable*.

## Rule Groups

### BugProne

* [ARG_VAR_CONFLICT](#arg_var_conflict)

* [NO_DEFAULT_INSIDE_SWITCH](#no_default_inside_switch)

* [NESTED_CFOUTPUT](#nested_cfoutput)

* [OUTPUT_ATTR](#output_attr)

* [MISSING_VAR](#missing_var)

* [COMPARE_INSTEAD_OF_ASSIGN](#compare_instead_of_assign)

* [AVOID_USING_ISDATE](#avoid_using_isdate)

### Correctness

* [ARG_DEFAULT_MISSING](#arg_default_missing)

* [ARG_TYPE_ANY](#arg_type_any)

* [ARG_TYPE_MISSING](#arg_type_missing)

* [ARG_VAR_MIXED](#arg_var_mixed)

* [QUERYNEW_DATATYPE](#querynew_datatype)

* [UNUSED_LOCAL_VARIABLE](#unused_local_variable)

* [UNUSED_METHOD_ARGUMENT](#unused_method_argument)

* [UNQUOTED_STRUCT_KEY](#unquoted_struct_key)

* [STRUCT_ARRAY_NOTATION](#struct_array_notation)

* [USE_DISPLAY_NAME](#use_display_name)

### BadPractice

* [AVOID_USING_ABORT](#avoid_using_abort)

* [AVOID_USING_CFABORT_TAG](#avoid_using_cfabort_tag)

* [AVOID_USING_CFDUMP_TAG](#avoid_using_cfdump_tag)

* [AVOID_USING_CFEXECUTE_TAG](#avoid_using_cfexecute_tag)

* [AVOID_USING_CFINSERT_TAG](#avoid_using_cfinsert_tag)

* [AVOID_USING_CFMODULE_TAG](#avoid_using_cfmodule_tag)

* [AVOID_USING_CFUPDATE_TAG](#avoid_using_cfupdate_tag)

* [AVOID_USING_WRITEDUMP](#avoid_using_writedump)

* [GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN](#global_literal_value_used_too_often)

* [GLOBAL_VAR](#global_var)

* [LOCAL_LITERAL_VALUE_USED_TOO_OFTEN](#local_literal_value_used_too_often)

* [SQL_SELECT_STAR](#sql_select_star)

* [AVOID_USING_DEBUG_ATTR](#avoid_using_debug_attr)

* [AVOID_USING_CFSETTING_DEBUG](#avoid_using_cfsetting_debug)

* [AVOID_USING_CFINCLUDE_TAG](#avoid_using_cfinclude_tag)

* [AVOID_USING_ISDEBUGMODE](#avoid_using_isdebugmode)

### Security

* [CFQUERYPARAM_REQ](#cfqueryparam_req)

* [QUERYPARAM_REQ](#queryparam_req)

### CodeStyle

* [ARG_HINT_MISSING](#arg_hint_missing)

* [COMPONENT_HINT_MISSING](#component_hint_missing)

* [FUNCTION_HINT_MISSING](#function_hint_missing)

* [FUNCTION_TYPE_ANY](#function_type_any)

* [FUNCTION_TYPE_MISSING](#function_type_missing)

* [ARG_HINT_MISSING_SCRIPT](#arg_hint_missing_script)

### ModernSyntax

* [AVOID_USING_ARRAYNEW](#avoid_using_arraynew)

* [AVOID_USING_STRUCTNEW](#avoid_using_structnew)

* [AVOID_USING_CREATEOBJECT](#avoid_using_createobject)

### Complexity

* [COMPLEX_BOOLEAN_CHECK](#complex_boolean_check)

* [EXCESSIVE_FUNCTIONS](#excessive_functions)

* [EXCESSIVE_ARGUMENTS](#excessive_arguments)

* [EXPLICIT_BOOLEAN_CHECK](#explicit_boolean_check)

* [EXCESSIVE_COMPONENT_LENGTH](#excessive_component_length)

* [EXCESSIVE_FUNCTION_LENGTH](#excessive_function_length)

* [FUNCTION_TOO_COMPLEX](#function_too_complex)

### Naming

* [METHOD_HAS_PREFIX_OR_POSTFIX](#method_has_prefix_or_postfix)

* [METHOD_INVALID_NAME](#method_invalid_name)

* [METHOD_IS_TEMPORARY](#method_is_temporary)

* [METHOD_TOO_SHORT](#method_too_short)

* [METHOD_TOO_LONG](#method_too_long)

* [METHOD_TOO_WORDY](#method_too_wordy)

* [VAR_ALLCAPS_NAME](#var_allcaps_name)

* [VAR_HAS_PREFIX_OR_POSTFIX](#var_has_prefix_or_postfix)

* [VAR_INVALID_NAME](#var_invalid_name)

* [VAR_IS_TEMPORARY](#var_is_temporary)

* [VAR_TOO_SHORT](#var_too_short)

* [VAR_TOO_LONG](#var_too_long)

* [VAR_TOO_WORDY](#var_too_wordy)

* [SCOPE_ALLCAPS_NAME](#scope_allcaps_name)

* [ARGUMENT_MISSING_NAME](#argument_missing_name)

* [ARGUMENT_INVALID_NAME](#argument_invalid_name)

* [ARGUMENT_ALLCAPS_NAME](#argument_allcaps_name)

* [ARGUMENT_TOO_SHORT](#argument_too_short)

* [ARGUMENT_TOO_LONG](#argument_too_long)

* [ARGUMENT_TOO_WORDY](#argument_too_wordy)

* [ARGUMENT_IS_TEMPORARY](#argument_is_temporary)

* [ARGUMENT_HAS_PREFIX_OR_POSTFIX](#argument_has_prefix_or_postfix)

* [METHOD_ALLCAPS_NAME](#method_allcaps_name)

* [COMPONENT_INVALID_NAME](#component_invalid_name)

* [COMPONENT_ALLCAPS_NAME](#component_allcaps_name)

* [COMPONENT_TOO_SHORT](#component_too_short)

* [COMPONENT_TOO_LONG](#component_too_long)

* [COMPONENT_TOO_WORDY](#component_too_wordy)

* [COMPONENT_IS_TEMPORARY](#component_is_temporary)

* [COMPONENT_HAS_PREFIX_OR_POSTFIX](#component_has_prefix_or_postfix)

* [PACKAGE_CASE_MISMATCH](#package_case_mismatch)

### Experimental

* [NEVER_USE_QUERY_IN_CFM](#never_use_query_in_cfm)

* [FILE_SHOULD_START_WITH_LOWERCASE](#file_should_start_with_lowercase)
