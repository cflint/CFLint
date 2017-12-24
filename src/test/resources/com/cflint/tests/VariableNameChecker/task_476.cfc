<cfcomponent>
<cffunction name="loadHTML" access="public" returntype="struct" output="false">
	<cfargument name="iBooking" type="numeric" required="true">
	<cfargument name="iLang" type="numeric" required="true">

	<cfset LOCAL.struResult = {
		bSuccess = false, bCaught = false, catch = {},
		arrHTMLHead = [],
		arrHTML = [],
		arrHTMLFoot = []
	}>

	<cfreturn LOCAL.struResult>
</cffunction>
</cfcomponent>