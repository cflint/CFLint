<cfcomponent>
	<cffunction name="foo">
		<cfset var sRememberUserCookieKey = getRememberUserLoginCookieName()>
   		<cfcookie name="#sRememberUserCookieKey#" value="#sCookieValue#" expires="#sExpirePeriod#">
	</cffunction>
</cfcomponent>
