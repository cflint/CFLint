<!--- scoped use of name is detected, no unused arg flagged--->
<cfcomponent>
<cffunction name="helloWorld" access="public" returntype="string">
  <cfscript>
    var x = 1;
    ARGUMENTS.ALLcAPSWAYTOOLONGETETCETC;
    var temp = 1;
    var WayMuchTooWordyExample = '';
    var objBadName = '';
  </cfscript>
</cffunction>
</cfcomponent>