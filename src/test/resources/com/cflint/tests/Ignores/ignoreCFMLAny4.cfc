<cfcomponent>

    <cffunction name="testFunction" displayname="" access="public" output="false" returntype="void" hint="">

        <cfset var commentsSelect = "">

        <!---
        @CFLintIgnore CFQUERYPARAM_REQ
        --->
        <cfquery name="commentsSelect" datasource="#arguments.siteDomain#com" cachedwithin="#createTimeSpan(0,0,5,0)#">
            SELECT
                M.firstName
            FROM #application.linkedServerName#.schema.dbo.Comment C WITH (NOLOCK)
            LEFT OUTER JOIN something SM WITH (NOLOCK)
                ON C.memberID = SM.memberID
            INNER JOIN somethingelse m
                ON m.memberID = sm.memberid
            LEFT OUTER JOIN #application.linkedServerName#.schema.dbo.Author A WITH (NOLOCK)
                ON C.aID = A.aID
            WHERE
                eID = 34 AND
                moderated = 1
            ORDER BY
                cID
        </cfquery>
    </cffunction>

</cfcomponent>