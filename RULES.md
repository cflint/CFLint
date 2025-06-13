# Built-in Rules

## Rule Table

| Rule Code | Checker | Description | Severity | Message |
|-----------|---------|-------------|----------|----------|
| ARG_DEFAULT_MISSING | ArgDefChecker | Optional argument is missing a default value | WARNING | Argument *variable* is not required and does not define a default value |
| ARG_VAR_CONFLICT | ArgVarChecker | Variable declared in both local and argument scopes | ERROR | Variable *variable* should not be declared in both local and argument scopes |
| ARG_VAR_MIXED | ArgVarChecker | Variable referenced in local and argument scopes | INFO | Variable *variable* should not be referenced in local and argument scope |
| NO_DEFAULT_INSIDE_SWITCH | CFSwitchDefaultChecker | Missing default switch statement | WARNING | Not having a Default statement defined for a switch could pose potential issues |
| GLOBAL_VAR | GlobalVarChecker | Global variable exists | WARNING | Identifier *variable* is global. Referencing in a CFC or function should be avoided |
| NESTED_CFOUTPUT | NestedCFOutput | Nested cfoutput with cfquery tag | ERROR | Nested CFOutput, outer CFOutput has @query |
| OUTPUT_ATTR | OutputParmMissing | Tag should have output='false' | INFO | &lt;*tag* name="*variable*"&gt; should have @output='false' |
| QUERYPARAM_REQ | QueryParamChecker | Use query parameters for variables in sql statements | WARNING | Use query parameters for variables in sql statements |
| CFQUERYPARAM_REQ | QueryParamChecker | cfquery should use &lt;cfqueryparam&gt; | WARNING | &lt;*tag*&gt; should use &lt;cfqueryparam/&gt; for variable '*variable*' |
| QUERYNEW_DATATYPE | TypedQueryNew | QueryNew statement should specify data types | WARNING | QueryNew statement should specify datatypes |
| MISSING_VAR | VarScoper | Variable is not declared with a var statement | ERROR | Variable *variable* is not declared with a var statement |
| AVOID_USING_CFDUMP_TAG | CFDumpChecker | Avoid use of cfdump tags | WARNING | Avoid leaving &lt;cfdump&gt; tags in committed code. Debug information should be omitted from release code |
| AVOID_USING_CFEXECUTE_TAG | CFExecuteChecker | Avoid use of cfexecute tags | WARNING | Avoid leaving &lt;cfexecute&gt; tags in committed code. CFexecute can be used as an attack vector and is slow |
| AVOID_USING_ISDATE | CFBuiltInFunctionChecker | Avoid using isDate built-in function | WARNING | Avoid using the isDate built-in function. It is too permissive. Use isValid() instead |
| AVOID_USING_CFABORT_TAG | CFAbortChecker | Avoid use of cfabort tags | INFO | Avoid leaving &lt;cfabort&gt; tags in committed code |
| AVOID_USING_ABORT | AbortChecker | Avoid use of abort statements | INFO | Avoid using abort in production code |
| AVOID_USING_CFINSERT_TAG | CFInsertChecker | Avoid use of cfinsert tags | WARNING | Avoid using &lt;cfinsert&gt; tags. Use cfquery and cfstoredproc instead |
| AVOID_USING_CFMODULE_TAG | CFModuleChecker | Avoid use of cfmodule tags | WARNING | Avoid using &lt;cfmodule&gt; tags |
| AVOID_USING_CFUPDATE_TAG | CFUpdateChecker | Avoid use of cfupdate tags | WARNING | Avoid using &lt;cfupdate&gt; tags. Use cfquery and cfstoredproc instead |
| AVOID_USING_CFINCLUDE_TAG | CFIncludeChecker | Avoid use of cfinclude tags | WARNING | Avoid using &lt;cfinclude&gt; tags. Use components instead |
| COMPONENT_HINT_MISSING | ComponentHintChecker | Component is missing a hint | WARNING | Component *variable* is missing a hint |
| FUNCTION_HINT_MISSING | FunctionHintChecker | Function is missing a hint | INFO | Function *variable* is missing a hint |
| ARG_HINT_MISSING | ArgumentHintChecker | Argument is missing a hint | INFO | Argument *variable* is missing a hint |
| ARG_HINT_MISSING_SCRIPT | ArgumentHintChecker | Argument is missing a hint (script syntax) | INFO | Argument *variable* is missing a hint. Use javadoc style annotations on cfscript functions |
| ARG_TYPE_MISSING | ArgumentTypeChecker | Component is missing a type | WARNING | Argument *variable* is missing a type |
| ARG_TYPE_ANY | ArgumentTypeChecker | Component is of type any | WARNING | Argument *variable* is any. Please change to be a more specific type |
| EXCESSIVE_FUNCTION_LENGTH | FunctionLengthChecker | Method is too long | WARNING | Function *function* is *variable* lines. Should be fewer than 100 lines |
| EXCESSIVE_COMPONENT_LENGTH | ComponentLengthChecker | Component is too long | WARNING | Component *component* is *variable* lines. Should be fewer than 500 lines |
| FUNCTION_TYPE_MISSING | FunctionTypeChecker | Function is missing a return type | WARNING | Function *variable* is missing a return type |
| FUNCTION_TYPE_ANY | FunctionTypeChecker | Function has a return type of any | INFO | Function *variable* return type is any. Please change to be a more specific type |
| EXCESSIVE_ARGUMENTS | TooManyArgumentsChecker | Function has too many arguments | WARNING | Function *function* has too many arguments. Should be fewer than 10 |
| EXCESSIVE_FUNCTIONS | TooManyFunctionsChecker | Too many functions | WARNING | Component *component* has too many functions. Should be fewer than 10 |
| FUNCTION_TOO_COMPLEX | SimpleComplexityChecker | Function is too complex | WARNING | Function *function* is too complex. Consider breaking the function into smaller functions |
| QUERYNEW_DUPLICATE_COLUMNS | QueryNewChecker | QueryNew declares duplicate columns | ERROR | QueryNew declares column *variable* multiple times, this is a hard error in some CFML implementations |
| AVOID_USING_WRITEDUMP | WriteDumpChecker | Avoid use of writeDump statements | INFO | Avoid using the writeDump function in production code |
| AVOID_USING_STRUCTNEW | StructNewChecker | Avoid use of structNew statements. Use {} instead | INFO | Avoid using the structNew function in production code |
| AVOID_USING_ISDEBUGMODE | IsDebugModeChecker | Avoid use of isDebugMode statements | WARNING | Avoid using the IsDebugMode function in production code |
| FUNCTION_NAME_COLLISION | FunctionCollisionChecker | Function name collision with reserved words | WARNING | Avoid using the name *variable* for a function. It is reserved in some CFML implementations. See https://cfdocs.org/*variable* |
| AVOID_USING_ARRAYNEW | ArrayNewChecker | Avoid use of arrayNew statements. Use [] instead | INFO | Use implict array construction instead (= []) |
| COMPLEX_BOOLEAN_CHECK | ComplexBooleanExpressionChecker | Complex boolean expression | WARNING | Boolean expression is too complex. Consider simplifying or moving to a named method |
| EXPLICIT_BOOLEAN_CHECK | BooleanExpressionChecker | Checking boolean expression explicitly | INFO | Explicit check of boolean expression is not needed |
| VAR_INVALID_NAME | VariableNameChecker | Variable has invalid name | INFO | Variable *variable* is not a valid name. Please use camelCase or underscores |
| VAR_ALLCAPS_NAME | VariableNameChecker | Variable name is all caps | INFO | Variable *variable* should not be upper case |
| SCOPE_ALLCAPS_NAME | VariableNameChecker | Variable scope name is all caps | INFO | Scope *variable* should not be upper case |
| VAR_TOO_SHORT | VariableNameChecker | Variable name is too short | INFO | Variable *variable* should be longer than 3 characters |
| VAR_TOO_LONG | VariableNameChecker | Variable name is too long | INFO | Variable *variable* should be shorter than 20 characters |
| VAR_TOO_WORDY | VariableNameChecker | Variable name contain too many words | INFO | Variable *variable* is too wordy. Try to think of a more concise name |
| VAR_IS_TEMPORARY | VariableNameChecker | Variable name looks temporary | INFO | Temporary variable *variable* could be named better |
| VAR_HAS_PREFIX_OR_POSTFIX | VariableNameChecker | Variable name has prefix or postfix | INFO | Variable has prefix or postfix *variable* and could be named better |
| ARGUMENT_MISSING_NAME | ArgumentNameChecker | Argument is missing a name | INFO | Argument is missing a name |
| ARGUMENT_INVALID_NAME | ArgumentNameChecker | Argument has invalid name | INFO | Argument *variable* is not a valid name. Please use camelCase or underscores |
| ARGUMENT_ALLCAPS_NAME | ArgumentNameChecker | Argument name is all caps | INFO | Argument *variable* should not be upper case |
| ARGUMENT_TOO_SHORT | ArgumentNameChecker | Argument name is too short | INFO | Argument *variable* should be longer than 3 characters |
| ARGUMENT_TOO_LONG | ArgumentNameChecker | Argument name is too long | INFO | Argument *variable* should be shorter than 20 characters |
| ARGUMENT_TOO_WORDY | ArgumentNameChecker | Argument name contain too many words | INFO | Argument *variable* is too wordy. Try to think of a more concise name |
| ARGUMENT_IS_TEMPORARY | ArgumentNameChecker | Argument name looks temporary | INFO | Temporary argument *variable* could be named better |
| ARGUMENT_HAS_PREFIX_OR_POSTFIX | ArgumentNameChecker | Argument name has prefix or postfix | INFO | Argument has prefix or postfix *variable* and could be named better |
| METHOD_INVALID_NAME | MethodNameChecker | Method has invalid name | INFO | Method name *function* is not a valid name. Please use camelCase or underscores |
| METHOD_ALLCAPS_NAME | MethodNameChecker | Method name is all caps | INFO | Method name *function* should not be upper case |
| METHOD_TOO_SHORT | MethodNameChecker | Method name is too short | INFO | Method name *function* should be longer than 3 characters |
| METHOD_TOO_LONG | MethodNameChecker | Method name is too long | INFO | Method name *function* should be shorter than 25 characters |
| METHOD_TOO_WORDY | MethodNameChecker | Method name contain too many words | INFO | Method name *function* is too wordy. Try to think of a more concise name |
| METHOD_IS_TEMPORARY | MethodNameChecker | Method name looks temporary | INFO | Method name *function* could be named better |
| METHOD_HAS_PREFIX_OR_POSTFIX | MethodNameChecker | Method name has prefix or postfix | INFO | Method name *function* has prefix or postfix and could be named better |
| COMPONENT_INVALID_NAME | ComponentNameChecker | Component has invalid name | INFO | Component name *component* is not a valid name. Please use PascalCase and start with a capital letter |
| COMPONENT_ALLCAPS_NAME | ComponentNameChecker | Component name is all caps | INFO | Component name *component* should not be all upper case |
| COMPONENT_TOO_SHORT | ComponentNameChecker | Component name is too short | INFO | Component name *component* should be longer than 3 characters |
| COMPONENT_TOO_LONG | ComponentNameChecker | Component name is too long | INFO | Component name *component* should be shorter than 15 characters |
| COMPONENT_TOO_WORDY | ComponentNameChecker | Component name contain too many words | INFO | Component name *component* is too wordy. Try to think of a more concise name |
| COMPONENT_IS_TEMPORARY | ComponentNameChecker | Component name looks temporary | INFO | Component name *component* could be named better |
| COMPONENT_HAS_PREFIX_OR_POSTFIX | ComponentNameChecker | Component name has prefix or postfix | INFO | Component name *component* has prefix or postfix and could be named better |
| FILE_SHOULD_START_WITH_LOWERCASE | FileCaseChecker | CFM File starts with upper case | INFO | File *filename* starts with an upper case letter. Only components (.cfc files) should start with an upper case letter |
| AVOID_USING_CREATEOBJECT | CreateObjectChecker | Avoid use of creatObject statements | INFO | CreateObject found. Use createObject(path_to_component) or even better new path_to_component() |
| AVOID_USING_DEBUG_ATTR | CFDebugAttributeChecker | Avoid use of debug attribute | WARNING | Avoid leaving debug attribute on tags |
| AVOID_USING_CFSETTING_DEBUG | CFDebugAttributeChecker | Avoid using showDebugOutput attribute on cfsetting | WARNING | Avoid using showDebugOutput attribute on cfsetting |
| UNUSED_LOCAL_VARIABLE | UnusedLocalVarChecker | Unused local variable | INFO | Local variable *variable* is not used in function *function*. Consider removing it |
| UNUSED_METHOD_ARGUMENT | UnusedArgumentChecker | Unused method argument | INFO | Argument *variable* is not used in function. Consider removing it |
| COMPARE_INSTEAD_OF_ASSIGN | CFCompareVsAssignChecker | Using comparison where assignment was probably meant | WARNING | CWE-482: Comparing instead of Assigning |
| UNQUOTED_STRUCT_KEY | StructKeyChecker | Unquoted struct key is not case-sensitive | WARNING | Unquoted struct key *variable* is not case-sensitive. Quoting it is recommended |
| STRUCT_ARRAY_NOTATION | StructKeyChecker | Unquoted struct key should use array notation | WARNING | Unquoted struct key *variable* is not case-sensitive. Using array notation is recommended |
| SQL_SELECT_STAR | SelectStarChecker | Avoid using 'select *' in queries | WARNING | Avoid using 'select *' in a query |
| NEVER_USE_QUERY_IN_CFM | CFQueryChecker | Don't use cfquery in .cfm files | ERROR | Don't use &lt;cfquery&gt; in .cfm files. Database should not be coupled with view |
| USE_DISPLAY_NAME | ComponentDisplayNameChecker | Component has name attribute instead of displayName | INFO | Component *variable* has a name attribute, but perhaps you meant to use displayName |
| GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN | GlobalLiteralChecker | Literal value used too often globally | INFO | Literal *variable* occurs several times in one or more files. Consider giving it a name and not hard coding values |
| LOCAL_LITERAL_VALUE_USED_TOO_OFTEN | LocalLiteralChecker | Literal value used too often locally | INFO | Literal *variable* occurs several times in the same file. Consider giving it a name and not hard coding values |
| PACKAGE_CASE_MISMATCH | PackageCaseChecker | Package case does not match its use | WARNING | The case of the package folder and the object declaration do not match for *variable* |

## Rule Groups

### BugProne
- ARG_VAR_CONFLICT
- NO_DEFAULT_INSIDE_SWITCH
- NESTED_CFOUTPUT
- OUTPUT_ATTR
- MISSING_VAR
- COMPARE_INSTEAD_OF_ASSIGN
- AVOID_USING_ISDATE

### Correctness
- ARG_DEFAULT_MISSING
- ARG_TYPE_ANY
- ARG_TYPE_MISSING
- ARG_VAR_MIXED
- QUERYNEW_DATATYPE
- UNUSED_LOCAL_VARIABLE
- UNUSED_METHOD_ARGUMENT
- UNQUOTED_STRUCT_KEY
- STRUCT_ARRAY_NOTATION
- USE_DISPLAY_NAME

### BadPractice
- AVOID_USING_ABORT
- AVOID_USING_CFABORT_TAG
- AVOID_USING_CFDUMP_TAG
- AVOID_USING_CFEXECUTE_TAG
- AVOID_USING_CFINSERT_TAG
- AVOID_USING_CFMODULE_TAG
- AVOID_USING_CFUPDATE_TAG
- AVOID_USING_WRITEDUMP
- GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN
- GLOBAL_VAR
- LOCAL_LITERAL_VALUE_USED_TOO_OFTEN
- SQL_SELECT_STAR
- AVOID_USING_DEBUG_ATTR
- AVOID_USING_CFSETTING_DEBUG
- AVOID_USING_CFINCLUDE_TAG
- AVOID_USING_ISDEBUGMODE

### Security
- CFQUERYPARAM_REQ
- QUERYPARAM_REQ

### CodeStyle
- ARG_HINT_MISSING
- COMPONENT_HINT_MISSING
- FUNCTION_HINT_MISSING
- FUNCTION_TYPE_ANY
- FUNCTION_TYPE_MISSING
- ARG_HINT_MISSING_SCRIPT

### ModernSyntax
- AVOID_USING_ARRAYNEW
- AVOID_USING_STRUCTNEW
- AVOID_USING_CREATEOBJECT

### Complexity
- COMPLEX_BOOLEAN_CHECK
- EXCESSIVE_FUNCTIONS
- EXCESSIVE_ARGUMENTS
- EXPLICIT_BOOLEAN_CHECK
- EXCESSIVE_COMPONENT_LENGTH
- EXCESSIVE_FUNCTION_LENGTH
- FUNCTION_TOO_COMPLEX

### Naming
- METHOD_HAS_PREFIX_OR_POSTFIX
- METHOD_INVALID_NAME
- METHOD_IS_TEMPORARY
- METHOD_TOO_SHORT
- METHOD_TOO_LONG
- METHOD_TOO_WORDY
- VAR_ALLCAPS_NAME
- VAR_HAS_PREFIX_OR_POSTFIX
- VAR_INVALID_NAME
- VAR_IS_TEMPORARY
- VAR_TOO_SHORT
- VAR_TOO_LONG
- VAR_TOO_WORDY
- SCOPE_ALLCAPS_NAME
- ARGUMENT_MISSING_NAME
- ARGUMENT_INVALID_NAME
- ARGUMENT_ALLCAPS_NAME
- ARGUMENT_TOO_SHORT
- ARGUMENT_TOO_LONG
- ARGUMENT_TOO_WORDY
- ARGUMENT_IS_TEMPORARY
- ARGUMENT_HAS_PREFIX_OR_POSTFIX
- METHOD_ALLCAPS_NAME
- COMPONENT_INVALID_NAME
- COMPONENT_ALLCAPS_NAME
- COMPONENT_TOO_SHORT
- COMPONENT_TOO_LONG
- COMPONENT_TOO_WORDY
- COMPONENT_IS_TEMPORARY
- COMPONENT_HAS_PREFIX_OR_POSTFIX
- PACKAGE_CASE_MISMATCH

### Experimental
- NEVER_USE_QUERY_IN_CFM
- FILE_SHOULD_START_WITH_LOWERCASE

## Configurable Parameters

| Checker | Parameter | Default Value |
|---------|-----------|---------------|
| FunctionLengthChecker | length | 100 |
| ComponentLengthChecker | length | 500 |
| TooManyArgumentsChecker | maximum | 10 |
| TooManyFunctionsChecker | maximum | 10 |
| SimpleComplexityChecker | maximum | 10 |
| VariableNameChecker | minLength | 3 |
| VariableNameChecker | maxLength | 20 |
| VariableNameChecker | maxWords | 4 |
| VariableNameChecker | ignoreUpperCaseScopes | CGI,URL |
| VariableNameChecker | ignoreAllCapsInScopes | this,variables |
| VariableNameChecker | ignorePrefixPostfixOn | thisTag |
| VariableNameChecker | case | camelCase |
| ArgumentNameChecker | minLength | 3 |
| ArgumentNameChecker | maxLength | 20 |
| ArgumentNameChecker | maxWords | 4 |
| ArgumentNameChecker | case | camelCase |
| MethodNameChecker | minLength | 3 |
| MethodNameChecker | maxLength | 25 |
| MethodNameChecker | maxWords | 5 |
| MethodNameChecker | case | camelCase |
| ComponentNameChecker | minLength | 3 |
| ComponentNameChecker | maxLength | 15 |
| ComponentNameChecker | maxWords | 3 |
| ComponentNameChecker | case | PascalCase |
| GlobalLiteralChecker | maximum | 3 |
| GlobalLiteralChecker | maxWarnings | 5 |
| GlobalLiteralChecker | warningScope | global |
| GlobalLiteralChecker | ignoreWords | numeric,text,textnocase,asc,desc,in,out,inout,one,all,bigdecimal,boolean,byte,char,int,long,float,double,short,string,null |
| LocalLiteralChecker | maximum | 3 |
| LocalLiteralChecker | maxWarnings | 5 |
| LocalLiteralChecker | warningScope | local |
| LocalLiteralChecker | ignoreWords | numeric,text,textnocase,asc,desc,in,out,inout,one,all,bigdecimal,boolean,byte,char,int,long,float,double,short,string,null |

