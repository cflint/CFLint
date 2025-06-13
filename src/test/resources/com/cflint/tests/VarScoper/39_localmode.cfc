<cfscript>


    function localModeFunction() LocalMode="modern" {
        // This variable is local to the function
        localVar = "I am a local variable";
        return localVar;
    }
    function localModeFunction1() LocalMode="true" {
        // This variable is local to the function
        localVar = "I am a local variable";
        return localVar;
    }
    function localModeFunction2() LocalMode="#true#" {
        // This variable is local to the function
        localVar = "I am a local variable";
        return localVar;
    }
    function localModeFunction3() LocalMode=false {
        // This variable is local to the function
        var localVar = "I am a local variable";
        return localVar;
    }
    function localModeFunction4() LocalMode=false {
        // This variable is local to the function
        localVar = "I am a local variable";
        return localVar;
    }
</cfscript>