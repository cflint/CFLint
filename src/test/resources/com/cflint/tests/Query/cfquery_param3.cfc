<cfcomponent>
	<cffunction name="foo">
		<cfquery name="local.something" datasource="#variables.DSN#">
		      SELECT top #arguments.count#
		      	fooCol1, siteID, fooCol3
		      FROM
		      FooTable
		</cfquery>

	</cffunction>
</cfcomponent>