<cfcomponent>
  <cffunction name="foo">
    <cftry>
    <cfcatch name="e">
        <cfset e.additional.luceeDocsPageId = 1/>
    </cfcatch>
    </cftry>
  </cffunction>
</cfcomponent>