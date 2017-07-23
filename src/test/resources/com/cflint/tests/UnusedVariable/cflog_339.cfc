<cfcomponent>
	<cffunction name="foo">
		<cfset var sSecureFolder = expandPath("/#variables.stContens.applicationname#/_foo/baz/")>
		<cflog file="content_errors" text="Please check the folder #sSecureFolder#! #cfcatch.message#">
	</cffunction>
</cfcomponent>
