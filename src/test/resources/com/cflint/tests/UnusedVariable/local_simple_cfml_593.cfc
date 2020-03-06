<cfcomponent>
<cffunction name="bar">
  <cfscript>
    local.foo="";        
    local.foo2 = "";
    local.foo3 = foo2;
  </cfscript>
</cffunction>
</cfcomponent>