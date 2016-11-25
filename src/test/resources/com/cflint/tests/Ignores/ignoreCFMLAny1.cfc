<cfcomponent>

 <cffunction name="testFunction" displayname="" access="public" output="false" returntype="void" hint="">
	<!---
	@CFLintIgnore SOMETHINGELSE,MISSING_VAR,ANOTHERTHINGTOIGNORE
	--->
  <cfscript>
     someVar = '';
  </cfscript>
 </cffunction>
 
</cfcomponent>