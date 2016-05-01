<!--- scoped use of name is detected, no unused arg flagged--->
<cfcomponent>
<cffunction name="helloWorld" access="public" returntype="string">
  <cfscript>
    var x = 1;
    var yyy = 1;
  </cfscript>
</cffunction>
</cfcomponent>