<!---
@CFLintIgnore CFQUERYPARAM_REQ
--->
<cfcomponent>

    <!---
    @CFLintIgnore CFQUERYPARAM_REQ
    --->
    <cffunction name="testFunction" displayname="" access="public" output="false" returntype="void" hint="">

        <cfset var commentsSelect = "">

        <!---
        @CFLintIgnore CFQUERYPARAM_REQ
        --->
        <cfquery name="commentsSelect" datasource="#arguments.siteDomain#com" cachedwithin="#createTimeSpan(0,0,5,0)#"> <!--- @CFLintIgnore CFQUERYPARAM_REQ --->
            <!---
            @CFLintIgnore CFQUERYPARAM_REQ
            --->
            SELECT
                M.firstName
            <!---
            @CFLintIgnore CFQUERYPARAM_REQ
            --->
            FROM #application.linkedServerName#.schema.dbo.Comment C WITH (NOLOCK) <!--- @CFLintIgnore CFQUERYPARAM_REQ --->
            LEFT OUTER JOIN something SM WITH (NOLOCK)
                ON C.memberID = SM.memberID
            INNER JOIN somethingelse m
                ON m.memberID = sm.memberid
            <!---
            @CFLintIgnore CFQUERYPARAM_REQ
            --->
            LEFT OUTER JOIN #application.linkedServerName#.schema.dbo.Author A WITH (NOLOCK) <!--- @CFLintIgnore CFQUERYPARAM_REQ --->
                ON C.aID = A.aID
            WHERE
                eID = 34 AND
                moderated = 1
            ORDER BY
                cID
        </cfquery>
    </cffunction>

</cfcomponent>