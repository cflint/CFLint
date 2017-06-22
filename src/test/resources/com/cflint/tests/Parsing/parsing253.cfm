<cfcomponent>
  <cffunction name="foo">
    <cfscript>
      removeTagList = '<cfquery,/cfquery>|<cfoutput,/cfoutput>|<cfscript,/cfscript|<script,/script>|<title,/title>|<style,/style>|<iframe,/iframe>,<a,/a>|<img,/>';
    </cfscript>
  </cffunction>
</cfcomponent>