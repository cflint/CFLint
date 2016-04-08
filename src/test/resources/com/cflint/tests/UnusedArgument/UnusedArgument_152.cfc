<!--- scoped use of name is detected, no unused arg flagged--->
<cfcomponent>
<cffunction name="helloWorld" access="public" returntype="string">
  <cfargument name="name" type="string" required="false" default="">
  <cfscript>
    return (len(arguments.name)) ? "Hello #arguments.name#" : "Hello World";
  </cfscript>
</cffunction>
</cfcomponent>