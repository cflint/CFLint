<cfcomponent>
<cffunction name="test" access="public" returnType="any" output="true">
	<cfscript>
		var resultText="";
	</cfscript>
	<cfset resultText="error">
	<cfsetting showdebugoutput="false" />
	<cfcontent type="text" reset="yes"><cfoutput>#resultText#<cfset foo=""/></cfoutput><cfabort>
</cffunction>
</cfcomponent>