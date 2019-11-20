<cfcomponent>
	<cffunction name="foo">
		<cfset var local = {}>
		<cfset local.stEditor = 123>
		<cfscript>foo(local.stEditor)</cfscript>
	</cffunction>
</cfcomponent>
