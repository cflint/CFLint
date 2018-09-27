<cfcomponent>
	<cffunction name="foo">
<cfquery name="local.query" datasource="dsn">
    SELECT  *
    FROM    Table
    WHERE   column = '####foo####'
</cfquery>

	</cffunction>
</cfcomponent>