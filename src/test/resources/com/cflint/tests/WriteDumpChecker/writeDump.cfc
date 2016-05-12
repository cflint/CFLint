<!--- scoped use of name is detected, no unused arg flagged--->
<cfcomponent>
<cffunction name="helloWorld" access="public" returntype="string">
  <cfargument name="name" type="string" required="false" default="">
  <cfscript>
    writeDump(name);
  </cfscript>
</cffunction>
</cfcomponent>