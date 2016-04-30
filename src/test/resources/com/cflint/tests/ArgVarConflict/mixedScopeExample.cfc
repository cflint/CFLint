<cfcomponent>

<cffunction name="loadPageChildren" displayname="" access="public" output="false" returntype="void" hint="">
<cfargument name="xml" type="xml" required="yes" hint="">

<cfscript>
var qryChildren = querynew('id,label,threshold,type,refid,multiple,iterative,alt,next,previous');
var pageChildrenAry = arraynew(1);
var i = {};
var p = {};
var stcA = '';
var pagesAry = xmlSearch(xml,"/Manual/PageSequence//Page");
var id = '';
variables.instance.pagechildren = {};

</cfscript>
</cffunction>
</cfcomponent>