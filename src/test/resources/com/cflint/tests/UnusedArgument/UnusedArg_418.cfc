<cfcomponent>
<cffunction name="lookup" access="public" output="false" returntype="void">
<cfargument name="domain" type="string" required="true" />
<cfset var details = "">
<cfquery name="details" datasource="#request.datasource#">
select *
from domains
where domain = <cfqueryparam value="#domain#" cfsqltype="CF_SQL_VARCHAR">
</cfquery>
</cffunction>
</cfcomponent>