<cfcomponent>
	<cffunction name="foo">
		<cfset var fooQry=""/>
<CFquery name="users" datasource="dsn">
SELECT user
FROM users
where user_id = <CFqueryparam value="#user_id#" cfsqltype="CF_SQL_NUMERIC">
</CFquery>

	</cffunction>
</cfcomponent>