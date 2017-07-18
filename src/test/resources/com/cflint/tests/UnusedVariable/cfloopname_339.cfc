<cfcomponent>
	<cffunction name="foo">
		<cfset var iIdx = "">
		<cfloop list="#arguments.lID#" index="iIdx">...</cfloop>
		<cfset var ifolder = 0>
		<cfloop collection="#attributes.foldermapping#" item="ifolder">...</cfloop>
	</cffunction>
</cfcomponent>
