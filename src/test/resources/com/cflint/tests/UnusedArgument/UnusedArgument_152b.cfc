<!--- unscoped use of name is detected, no unused arg flagged--->
<!--- but other arg xyzzy is determined to be unused --->
<cfcomponent>
<cffunction name="helloWorld" access="public" returntype="string">
  <cfargument name="name" type="string" required="false" default="">
  <cfargument name="xyzzy" type="string" required="false" default="">
  <cfscript>
    return (len(name)) ? "Hello #arguments.name#" : "Hello World";
  </cfscript>
</cffunction>
</cfcomponent>