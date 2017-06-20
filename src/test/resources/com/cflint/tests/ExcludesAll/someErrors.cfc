<!---
  This shouldn't ignore the missing var int the 2nd cfscript;
--->
<cfcomponent>

 <cffunction name="testFunction" displayname="" access="public" output="false" returntype="void" hint="">
	<cfscript>
     var test = '';
     someVar = '';
  </cfscript>
 </cffunction>
 
</cfcomponent>