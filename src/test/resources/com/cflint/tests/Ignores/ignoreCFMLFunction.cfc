<cfcomponent>

	<!---
	@CFLintIgnore SOMETHINGELSE,MISSING_VAR,ANOTHERTHINGTOIGNORE
	--->
 <cffunction name="testFunction" displayname="" access="public" output="false" returntype="void" hint="">
  <cfscript>
     someVar = '';
  </cfscript>
 </cffunction>
 
</cfcomponent>