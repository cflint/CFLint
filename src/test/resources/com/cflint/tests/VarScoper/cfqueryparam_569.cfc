<cfcomponent>
  <cffunction name="foo">
    <cfquery name="qry">
    or l1.googleID in (<cfqueryparam list="true" value="#value=arguments.placeIDs#">)
    </cfquery>
  </cffunction>
</cfcomponent>