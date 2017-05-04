<cfcomponent> 
<cffunction name="myFunction" hint="My function"> 
<cftry> 
<!--- do something ---> 
<cfcatch type="any"> 
<cfreturn "Error: #CFCATCH.Message#"> 
</cfcatch> 
</cftry> 
<cfreturn "Success"> 
</cffunction> 
<cfcomponent>