![CFLint](/src/main/resources/CFLint-logo.jpg)
======


A statistical code analysis tool for Cold Fusion (in the spirit of FindBugs and Lint)

<div><strong>Can you use it today? Functional? Yes</strong><br>
<strong>Mature, flawless?  No, you can help:<br></strong></div>
* <strong>Report an [issue](https://github.com/ryaneberly/CFLint/issues/new) </strong>
* <strong>Email [me](ryaneberly@gmail.com) </strong>

Download at [releases](https://github.com/ryaneberly/CFLint/releases) page.

See the [Jenkins](http://jenkins-ci.org/)/Hudson plugin [here](https://github.com/ryaneberly/CFLint-plugin).

Someone has created a [SublimeLinter](http://www.sublimelinter.com) plugin [here](https://github.com/ckaznocha/SublimeLinter-contrib-CFLint).


## Quick Start 
        cflint --folder <somefolder> 
        open cflint-result.html in your browser

## Usage
        
        cflint --folder c:\source\cfmx
        cflint-ui
        cflint --help

## Warning Codes
Note, most of these apply equally to cfscript and CFML.

### MISSING_VAR

Variable assigned without a scope.  This puts in a large scope that is usually intended.  Suggest using the 'var name' statement of explicit 'LOCAL.name' statement.

        <cffunction name="function1">
          <cfset unsafeVar = 123>
        </cffunction>

### NESTED_CFOUTPUT

Nested &lt;cfoutput/&gt; tags, the outer tag has an @query attribute, it should also specify the @group attribute.

        <cfoutput query="qry" group="SomeColumn">
          <cfoutput>#SomeColumn#</cfoutput>
        </cfoutput>
								
### QUERYNEW_DATATYPE

QueryNew should specify the datatypes of the columns.
 
        <cfset var qry = QueryNew('SomeColum','VarChar')>
								
### ARG_VAR_CONFLICT

Variable is varr'd with the same name as one of the arguments.  This is confusing or incorrect.
								
        <cffunction name="function1">
          <cfargument name="arg1">
          <cfset var arg1 = 123>
        </cffunction>

### ARG_VAR_MIXED

Variable referenced both as an unscoped (local) and an argument.  Code should consistently use one or the other.

        <cffunction name="function1">
          <cfargument name="arg1">
          <cfset arg1 = 123>
          <cfset var y = arguments.arg1>
        </cffunction>
								
### ARG_DEFAULT_MISSING

Arguments that are not required should specify a default value (@default)

        <cffunction name="function1">
          <cfargument name="arg1" required="false" default="somestring"/>
        </cffunction>
### OUTPUT_ATTR

Functions should specify @output="false"
								
        <cffunction name="function1" output="false">
        ..
        </cffunction>
        
### QUERYPARAM_REQ
     
        <cfquery name=\"LOCAL.categories\">
           SELECT * FROM product_categories p
           WHERE p.id = #LOCAL.id#
        </cfquery>
        
        Should use
        
        <cfqueryparam value="#LOCAL.id#"/>
