<cfloop list="#arguments.parseData#" delimiters="&" index="local.kvPair">
    <cfset local.keyStr = listFirst(local.kvPair, "=")>
    <cfset local.valStr = listLast(local.kvPair, "=", true)>
    // Do stuff
</cfloop>