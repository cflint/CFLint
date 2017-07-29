<cfcomponent>
  <cffunction name="foo">
    <cfinvokeargument name="x" value="#someVar# & '_' & #someOtherVar#">
  </cffunction>
</cfcomponent>