<cfcomponent>
	<cffunction name="foo">
		<cfset var lLoginURLParams = stContens.security.lLoginURLParams>
    	<cfloop list="#lLoginURLParams#" index="sParam">
	</cffunction>
</cfcomponent>
