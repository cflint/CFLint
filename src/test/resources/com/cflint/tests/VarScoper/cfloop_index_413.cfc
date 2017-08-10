<cffunction name="foo" returntype="string" >
	<cfloop from="0" to="23" index="idx">
		<cfset xxx = NumberFormat(idx, "00")>
	</cfloop>
	<cfreturn ''/>
</cffunction>