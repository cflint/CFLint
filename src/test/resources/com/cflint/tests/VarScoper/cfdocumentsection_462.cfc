<cfcomponent>
<!--- TEST: Do not flag cfdocumentsection/name --->
<cffunction name="createPDF" returntype="any" access="public" output="false"> 
    <cfdocument format="pdf" bookmark="yes"> 
        <cfdocumentsection name="section1"> 
        <!--- Insert some HTML content here. ---> 
        </cfdocumentsection> 
    </cfdocument> 
</cffunction>
</cfcomponent>