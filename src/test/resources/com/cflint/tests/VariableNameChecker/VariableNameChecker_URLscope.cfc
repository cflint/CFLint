<!--- scoped use of name is detected, no unused arg flagged--->
<cfcomponent>
<cffunction name="helloWorld" access="public" returntype="string">
  <cfscript>
    URL.xxxx= 1;
  </cfscript>
</cffunction>
</cfcomponent>