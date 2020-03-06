<cfcomponent displayname="mycomponent">
    <cffunction name="foo" returntype="void">
    <cfargument name="myvar" type="string" required="true">
        <cfscript>
            var id = arguments.myvar;
            var name = myvar;
            var id2 = arguments.myvar;
        </cfscript>
    </cffunction>
</cfcomponent>