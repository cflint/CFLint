List of built-in rules and rule groups
======================================

## Built-in rules

* ArgDefChecker
    * ARG_DEFAULT_MISSING - Optional argument is missing a default value.
* ArgVarChecker
    * ARG_VAR_CONFLICT - Variable declared in both local and argument scopes.
    * ARG_VAR_MIXED - Variable referenced in local and argument scopes.
* CFSwitchDefaultChecker
    * NO_DEFAULT_INSIDE_SWITCH - Missing default switch statement.
* GlobalVarChecker
    * GLOBAL_VAR - Global variable exists.
* NestedCFOutput
    * NESTED_CFOUTPUT - Nested `cfoutput` with `cfquery` tag.
* OutputParmMissing
    * OUTPUT_ATTR - Tag should have `output='false'`.
* QueryParamChecker
    * QUERYPARAM_REQ - `SetSql()` statement should use `.addParam()`.
    * CFQUERYPARAM_REQ - `cfquery` should use `<cfqueryparam>`.
* TypedQueryNew
    * QUERYNEW_DATATYPE - QueryNew statement should specify datatypes.
* VarScoper
    * MISSING_VAR - Variable is not declared with a var statement.
* CFDumpChecker
    * AVOID_USING_CFDUMP_TAG - Avoid use of `cfdump` tags.
* CFExecuteChecker
    * AVOID_USING_CFEXECUTE_TAG - Avoid use of `cfexecute` tags.
* CFAbortChecker
    * AVOID_USING_CFABORT_TAG - Avoid use of `cfabort` tags.
* AbortChecker
    * AVOID_USING_ABORT - Avoid use of abort statements.
* CFInsertChecker
    * AVOID_USING_CFINSERT_TAG - Avoid use of `cfinsert` tags.
* CFModuleChecker
    * AVOID_USING_CFMODULE_TAG - Avoid use of `cfmodule` tags.
* CFUpdateChecker
    * AVOID_USING_CFUPDATE_TAG - Avoid use of `cfupdate` tags.
* CFIncludeChecker
    * AVOID_USING_CFINCLUDE_TAG - Avoid use of `cfinclude` tags.
* ComponentHintChecker
    * COMPONENT_HINT_MISSING - Component is missing a hint.
* FunctionHintChecker
    * FUNCTION_HINT_MISSING - Function is missing a hint.
* ArgumentHintChecker
    * ARG_HINT_MISSING - Argument is missing a hint.
* ArgumentTypeChecker
    * ARG_TYPE_MISSING - Component is missing a type.
    * ARG_TYPE_ANY - Component is of type any.
* FunctionLengthChecker
    * EXCESSIVE_FUNCTION_LENGTH - Method is too long.
* ComponentLengthChecker
    * EXCESSIVE_COMPONENT_LENGTH - Component is too long.
* FunctionTypeChecker
    * FUNCTION_TYPE_MISSING - Function is missing a return type.
    * FUNCTION_TYPE_ANY - Function has a return type of any.
* TooManyArgumentsChecker
    * EXCESSIVE_ARGUMENTS - Function has too many arguments.
* TooManyFunctionsChecker
    * EXCESSIVE_FUNCTIONS - Too many functions.
* SimpleComplexityChecker
    * FUNCTION_TOO_COMPLEX - Function is too complex.
* WriteDumpChecker
    * AVOID_USING_WRITEDUMP - Avoid use of `writedump` statements.
* StructNewChecker
    * AVOID_USING_STRUCTNEW - Avoid use of `structnew` statements. Use `{}` instead.
* IsDebugModeChecker
    * AVOID_USING_ISDEBUGMODE - Avoid use of `isdebugmode` statements.
* ArrayNewChecker
    * AVOID_USING_ARRAYNEW - Avoid use of `arraynew` statements. Use `[]` instead.
* ComplexBooleanExpressionChecker
    * COMPLEX_BOOLEAN_CHECK - Complex boolean expression.
* BooleanExpressionChecker
    * EXPLICIT_BOOLEAN_CHECK - Checking boolean expression explicitly.
* VariableNameChecker
    * VAR_INVALID_NAME - Variable has invalid name.
    * VAR_ALLCAPS_NAME - Variable name is allcaps.
    * VAR_TOO_SHORT - Variable name is too short.
    * VAR_TOO_LONG - Variable name is too long.
    * VAR_TOO_WORDY - Variable name contain too many words.
    * VAR_IS_TEMPORARY - Variable name looks temporary.
    * VAR_HAS_PREFIX_OR_POSTFIX - Variable name has prefix or postfix.
* ArgumentNameChecker
    * ARGUMENT_INVALID_NAME - Argument has invalid name.
    * ARGUMENT_ALLCAPS_NAME - Argument name is allcaps.
    * ARGUMENT_TOO_SHORT - Argument name is too short.
    * ARGUMENT_TOO_LONG - Argument name is too long.
    * ARGUMENT_TOO_WORDY - Argument name contain too many words.
    * ARGUMENT_IS_TEMPORARY - Argument name looks temporary.
    * ARGUMENT_HAS_PREFIX_OR_POSTFIX - Argument name has prefix or postfix.
* MethodNameChecker
    * METHOD_INVALID_NAME - Method has invalid name.
    * METHOD_ALLCAPS_NAME - Method name is allcaps.
    * METHOD_TOO_SHORT - Method name is too short.
    * METHOD_TOO_LONG - Method name is too long.
    * METHOD_TOO_WORDY - Method name contain too many words.
    * METHOD_IS_TEMPORARY - Method name looks temporary.
    * METHOD_HAS_PREFIX_OR_POSTFIX - Method name has prefix or postfix.
* ComponentNameChecker
    * COMPONENT_INVALID_NAME - Component has invalid name.
    * COMPONENT_ALLCAPS_NAME - Component name is allcaps.
    * COMPONENT_TOO_SHORT - Component name is too short.
    * COMPONENT_TOO_LONG - Component name is too long.
    * COMPONENT_TOO_WORDY - Component name contain too many words.
    * COMPONENT_IS_TEMPORARY - Component name looks temporary.
    * COMPONENT_HAS_PREFIX_OR_POSTFIX - Component name has prefix or postfix.
* FileCaseChecker
    * FILE_SHOULD_START_WITH_LOWERCASE - CFM File starts with upper case.
* CreateObjectChecker
    * AVOID_USING_CREATEOBJECT - Avoid use of `creatobject` statements.
* CFDebugAttributeChecker
    * AVOID_USING_DEBUG_ATTR - Avoid use of debug attribute.
* UnusedLocalVarChecker
    * UNUSED_LOCAL_VARIABLE - Unused local variable.
* UnusedArgumentChecker
    * UNUSED_METHOD_ARGUMENT - Unused method argument.
* CFCompareVsAssignChecker
    * COMPARE_INSTEAD_OF_ASSIGN - Using comparison where assignment was probably meant.

Also:

* UnknownCategory
    * AVOID_EMPTY_FILES - Reports on empty (and therefore probably unnecessary) files.

## Rule Groups

### BugProne
 * ARG_VAR_CONFLICT : ERROR
 * NO_DEFAULT_INSIDE_SWITCH : WARNING
 * NESTED_CFOUTPUT : ERROR
 * OUTPUT_ATTR : INFO
 * MISSING_VAR : ERROR
 * COMPARE_INSTEAD_OF_ASSIGN : WARNING

### Correctness
 * ARG_DEFAULT_MISSING : WARNING
 * ARG_TYPE_ANY : WARNING
 * ARG_TYPE_MISSING : WARNING
 * ARG_VAR_MIXED : INFO
 * QUERYNEW_DATATYPE : WARNING
 * UNUSED_LOCAL_VARIABLE : INFO
 * UNUSED_METHOD_ARGUMENT : INFO
 * UNQUOTED_STRUCT_KEY : WARNING
 * USE_DISPLAY_NAME : INFO

### BadPractice
 * AVOID_USING_ABORT : INFO
 * AVOID_USING_CFABORT_TAG : INFO
 * AVOID_USING_CFDUMP_TAG : WARNING
 * AVOID_USING_CFEXECUTE_TAG : WARNING
 * AVOID_USING_CFINSERT_TAG : WARNING
 * AVOID_USING_CFMODULE_TAG : WARNING
 * AVOID_USING_CFUPDATE_TAG : WARNING
 * AVOID_USING_WRITEDUMP : INFO
 * GLOBAL_LITERAL_VALUE_USED_TOO_OFTEN : WARNING
 * GLOBAL_VAR : WARNING
 * LOCAL_LITERAL_VALUE_USED_TOO_OFTEN : WARNING
 * SQL_SELECT_STAR : WARNING
 * AVOID_USING_DEBUG_ATTR : WARNING
 * AVOID_USING_CFSETTING_DEBUG : WARNING
 * AVOID_USING_CFINCLUDE_TAG : WARNING
 * AVOID_USING_ISDEBUGMODE : WARNING
 * AVOID_EMPTY_FILES : WARNING

### Security
 * CFQUERYPARAM_REQ : WARNING
 * QUERYPARAM_REQ : WARNING

### CodeStyle
 * ARG_HINT_MISSING : INFO
 * COMPONENT_HINT_MISSING : WARNING
 * FUNCTION_HINT_MISSING : INFO
 * FUNCTION_TYPE_ANY : INFO
 * FUNCTION_TYPE_MISSING : WARNING
 * ARG_HINT_MISSING_SCRIPT : INFO

### ModernSyntax
 * AVOID_USING_ARRAYNEW : INFO
 * AVOID_USING_STRUCTNEW : INFO
 * AVOID_USING_CREATEOBJECT : INFO

### Complexity
 * COMPLEX_BOOLEAN_CHECK : WARNING
 * EXCESSIVE_FUNCTIONS : WARNING
 * EXCESSIVE_ARGUMENTS : WARNING
 * EXPLICIT_BOOLEAN_CHECK : INFO
 * EXCESSIVE_COMPONENT_LENGTH : WARNING
 * EXCESSIVE_FUNCTION_LENGTH : WARNING
 * FUNCTION_TOO_COMPLEX : WARNING

### Naming
 * METHOD_HAS_PREFIX_OR_POSTFIX : INFO
 * METHOD_INVALID_NAME : INFO
 * METHOD_IS_TEMPORARY : INFO
 * METHOD_TOO_SHORT : INFO
 * METHOD_TOO_LONG : INFO
 * METHOD_TOO_WORDY : INFO
 * VAR_ALLCAPS_NAME : INFO
 * VAR_HAS_PREFIX_OR_POSTFIX : INFO
 * VAR_INVALID_NAME : INFO
 * VAR_IS_TEMPORARY : INFO
 * VAR_TOO_SHORT : INFO
 * VAR_TOO_LONG : INFO
 * VAR_TOO_WORDY : INFO
 * SCOPE_ALLCAPS_NAME : INFO
 * ARGUMENT_MISSING_NAME : INFO
 * ARGUMENT_INVALID_NAME : INFO
 * ARGUMENT_ALLCAPS_NAME : INFO
 * ARGUMENT_TOO_SHORT : INFO
 * ARGUMENT_TOO_LONG : INFO
 * ARGUMENT_TOO_WORDY : INFO
 * ARGUMENT_IS_TEMPORARY : INFO
 * ARGUMENT_HAS_PREFIX_OR_POSTFIX : INFO
 * METHOD_ALLCAPS_NAME : INFO
 * COMPONENT_INVALID_NAME : INFO
 * COMPONENT_ALLCAPS_NAME : INFO
 * COMPONENT_TOO_SHORT : INFO
 * COMPONENT_TOO_LONG : INFO
 * COMPONENT_TOO_WORDY : INFO
 * COMPONENT_IS_TEMPORARY : INFO
 * COMPONENT_HAS_PREFIX_OR_POSTFIX : INFO
 * PACKAGE_CASE_MISMATCH : WARNING

### Experimental
 * NEVER_USE_QUERY_IN_CFM : WARN
 * FILE_SHOULD_START_WITH_LOWERCASE : INFO