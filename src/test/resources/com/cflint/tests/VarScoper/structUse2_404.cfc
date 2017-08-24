<cfcomponent>
	<cffunction name="foo">
		<cfset var streetname='123'/>
		<cfset tokenizedAddress["streetName"] = streetName>
		<cfset tokenizedAddress["streetNumber"] = 456>
		<cfscript>
			tokenizedAddress["streetName"] = streetName;
			tokenizedAddress2["streetNumber"] = 456;
		</cfscript>
	</cffunction>
</cfcomponent>