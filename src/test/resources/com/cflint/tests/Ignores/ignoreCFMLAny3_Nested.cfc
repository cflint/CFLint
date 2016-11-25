<!---
  This shouldn ignore the missing var int the nested cfscript;
--->
<cfcomponent>

 <cffunction name="testFunction" displayname="" access="public" output="false" returntype="void" hint="">
	<!---
	@CFLintIgnore SOMETHINGELSE,MISSING_VAR,ANOTHERTHINGTOIGNORE
	--->
  <div>
  <cfsavecontent>
  <cfscript>
     someVar = '';
  </cfscript>
  </cfsavecontent>
  </div>
 </cffunction>
 
</cfcomponent>