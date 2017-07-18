<cfcomponent>
	<cffunction name="foo">
		<cfquery name="local.something" datasource="#variables.DSN#">
		      SELECT
		      	fooCol1, siteID, fooCol3
		      FROM
		      FooTable
		</cfquery>

	</cffunction>
</cfcomponent>