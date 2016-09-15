component {

	public void function leakMemory(leak_modifier) {
	    leaky_variable = leak_modifier;
	    leakier_variable = leaky_variable * 2;
	
	    return leaky_variable * leakier_variable;
	}
	
	public void function poorlyWrittenFunction() {
	    writeDump('
	        <img style="width:100%;float:left;" src="#request.imagefolder#/#application.SiteInfo.site_foldername#/404.jpg">
	    ');
	}
}