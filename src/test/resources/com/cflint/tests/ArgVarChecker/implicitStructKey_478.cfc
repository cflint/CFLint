<cfcomponent displayname="mycomponent">
    <cffunction name="myfunction" returntype="void">
    <cfargument name="myvariable" type="string" required="true">
        <cfscript>
            var mystructure = {myvariable = arguments.myvariable};
        </cfscript>
    </cffunction>
</cfcomponent>