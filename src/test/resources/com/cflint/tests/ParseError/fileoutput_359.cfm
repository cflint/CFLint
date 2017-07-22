<cfcomponent>
  <cffunction name="foo">
    <cffile action="write" file="#LCase(local.destination)##rc.attachment_cvhtml_filename#.html" charset="utf-8" output='<!DOCTYPE html><html lang="en"><head><meta charset="utf-8" /></head><body>#local.stepCV#</body>'>
  </cffunction>
</cfcomponent>