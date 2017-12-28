<cfscript>
    string function getTemplatetoInclude(){
        return "foo.cfm";
    }

    // passes cflint
    include "#getTemplatetoInclude()#";
    
    // fails with java exception 
    include getTemplatetoInclude();
</cfscript> 