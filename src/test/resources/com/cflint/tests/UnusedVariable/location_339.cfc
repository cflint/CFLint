<cfcomponent>
	<cffunction name="foo">
		<cfset var sCoreURL = stContens.sCoreURL>
 	   <cflocation url="#sCoreURL#contens/index.cfm#sLoginParams#" addtoken="No">
	</cffunction>
</cfcomponent>
