<cfscript>
    function doSomething(id){
		switch (id){
			case -1:
				return "new";
			default:
				return "existing";
		}
    }
</cfscript>