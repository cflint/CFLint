<cfcomponent>
	<cffunction name="foo">
		<cfset var fooQry=""/>
        <cfquery name="fooQry" datasource="#arguments.siteDomain#com" cachedwithin="#createTimeSpan(0,0,5,0)#">
            SELECT
                M.firstName
                <!--- @CFLintIgnore CFQUERYPARAM_REQ --->
            FROM #application.linkedServerName#.schema.dbo.Comment C WITH (NOLOCK)
            LEFT OUTER JOIN something SM WITH (NOLOCK)
                ON C.memberID = SM.memberID
            INNER JOIN somethingelse m
                ON m.memberID = sm.memberid
            LEFT OUTER JOIN #application.linkedServerName#.schema.dbo.FooTable A WITH (NOLOCK)
                ON C.aID = A.aID
                AND C.bar = #magicVal# <!--- 
                		@CFLintIgnore CFQUERYPARAM_REQ --->
            WHERE 
            <!---
        @CFLintIgnore CFQUERYPARAM_REQ
        --->
                eID = #arguments.someNumber# AND
                moderated = 1
            ORDER BY
                cID
        </cfquery>

	</cffunction>
</cfcomponent>