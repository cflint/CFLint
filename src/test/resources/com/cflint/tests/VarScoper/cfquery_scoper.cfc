<cfcomponent>
	<cffunction name="foo">
		<cfquery name="local.something" datasource="#variables.DSN#">
		      SELECT
		      	fooCol1, siteID, fooCol3
		      FROM
		      FooTable
		</cfquery>
		
		<cfloop query="local.something">
		    <cfscript>
		        siteID = 123;
		    </cfscript>
		</cfloop>
		<cfset siteID=1/>
	</cffunction>
</cfcomponent>