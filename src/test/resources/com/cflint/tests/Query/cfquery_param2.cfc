<cfcomponent>
	<cffunction name="foo">
		<cfquery name="local.something" datasource="#variables.DSN#">
		      SELECT
		      	fooCol1, siteID, fooCol3
		      FROM
		      FooTable
		      <cfqueryparam value="Data copied from #variables.siteDetailList[arguments.siteID]["name"]# - #dateFormat(now(),"DD/MM/YYYY")#" cfsqltype="varchar"/>
		</cfquery>

	</cffunction>
</cfcomponent>