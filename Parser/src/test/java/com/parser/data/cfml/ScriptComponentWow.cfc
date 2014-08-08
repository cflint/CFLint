component output="false" persistent="false" {

	stringUtil = createObject("moshen.util.StringUtil");

	function generate(model,destdir,overwrite=false,createdirs=false) {
		variables.thisDir = getDirectoryFromPath(getMetaData(this).path);
		variables.baseDir = thisDir &"base/";
		variables.destDir = destdir;
		variables.model = model;
		variables._info = [];
		var action = (fileExists(destfile)) ? " (overwritten)" : " (created)";

		savecontent variable="renderedcontent" {
			model = duplicate(_model);
			metadata = duplicate(_model);
			include "/ram/#randName#";
		};
		
		recursiveDecentParse(dir=basedir,overwrite=overwrite,createdirs=createdirs);
		for(daform in model.getViews()) {
			processFile(model=daform, source=thisDir &"fragments/View.m.html",
						destfile=destDir&"views/#lcase(daForm.getPath())#.html",
						createdirs=createdirs, overwrite=overwrite);
/*
			processFile(model=daform, source=thisDir &"fragments/DeclarativeView.m.html",
						destfile=destDir&"views/#lcase(daForm.getPath())#Dec.html",
						createdirs=createdirs, overwrite=overwrite);
*/
		}
	}

	function info(required message) {
		arrayAppend(_info,message);
		return this;
	}
	function getInfo(message="") {
		return _info;
	}

}