<!---
  This shouldn't ignore the missing var int the 2nd cfscript;
--->
<cfcomponent>

 <cffunction name="testFunction" displayname="" access="public" output="false" returntype="void" hint="">
	<!---
	@CFLintIgnore SOMETHINGELSE,MISSING_VAR,ANOTHERTHINGTOIGNORE
	--->
  <cfscript>
     var test = '';
  </cfscript>
  <cfscript>
     someVar = '';
  </cfscript>
 </cffunction>
 
</cfcomponent>