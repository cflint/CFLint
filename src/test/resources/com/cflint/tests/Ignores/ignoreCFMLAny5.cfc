<cfcomponent namespace="something" displayname="something" output="no" hint="">
<cffunction name="init" access="public" returntype="something" hint="constructor" output="no">

    <cfset var siteSelect = "">

    <cfquery name="siteSelect" datasource="#variables.DSN#">
        SELECT
        	siteID
        FROM
        	somthing WITH (NOLOCK)
        WHERE
        	isLive = 1
    </cfquery>

    <cfset variables.siteDetailList = {}>

    <cfloop query="siteSelect">
        <cfscript>
            variables.siteDetailList[siteID] = {"siteName"="hello"}; // cflint ignore:MISSING_VAR
        </cfscript>
    </cfloop>

    <cfreturn this/>
</cffunction>
</cfcomponent>