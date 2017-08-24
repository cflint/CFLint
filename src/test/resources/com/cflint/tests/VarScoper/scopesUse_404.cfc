<cfcomponent>
	<cffunction name="foo">
		<cfset var streetname='123'/>
		<cfset local.streetName = streetName>
		<cfset application.streetNumber = 456>
		<cfscript>
			local.streetName = streetName;
			application.streetNumber = 456;
		</cfscript>
	</cffunction>
</cfcomponent>