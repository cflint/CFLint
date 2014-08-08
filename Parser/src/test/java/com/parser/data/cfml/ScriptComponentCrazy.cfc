component output="false" persistent="false" {

	stringUtil = createObject("moshen.util.StringUtil");

	function generate(model,destdir,overwrite=false,createdirs=false) {
		variables.thisDir = getDirectoryFromPath(getMetaData(this).path);
		variables.baseDir = thisDir &"base/";
		variables.destDir = destdir;
		variables.model = model;
		variables._info = [];
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
	};
	function getInfo(message="") {
		return info;
	};

	function recursiveDecentParse(dir,overwrite=false,createdirs=false) {
		directory name="dir" directory=dir action="list";
		loop query="dir" {
			var relpath = replaceNoCase(directory & "/",baseDir,"");
			if(type == "File") {
				var source = directory & "/" & name;
				var destfile = destdir & relpath & name;
				processFile(model=model, source=source, destfile=destfile, createdirs=createdirs, overwrite=overwrite);
			}
			else if(type == "Dir") {
				if(createdirs) {
					var parentdir = getDirectoryFromPath(destdir & relpath);
					if(!directoryExists(parentdir)) {
						directory action="create" directory=parentdir;
						info("created target dir:" & parentdir);
					}
				}
				recursiveDecentParse(dir=directory & "/" & name, overwrite=overwrite, createdirs=createdirs);
			}
		}
	}

	private void function processFile(model, source, destfile, createdirs=false, overwrite=false, objectname = "") {
		var cftemplate = new moshen.util.cftemplate();
		var templatepath = source;
		var _model = duplicate(arguments.model);
		var destfile = replaceNoCase(destfile,".m.",".");
		destfile = replaceNoCase(destfile,".t.",".");
		var parentdir = getDirectoryFromPath(destfile);
		if(fileExists(destfile) && !overwrite) {
			info("Overwrite is false, skipping as file exists:" & destfile);
		} else if(!directoryExists(parentdir) && !createdirs) {
			throw(type="moshen.model2text.access.error",message="Can not create directory #parentdir#.  Set createdirs argument to true.");
		} else {
			if(createdirs) {
				if(!directoryExists(parentdir)) {
					directory action="create" directory=parentdir;
					info("created target dir:" & parentdir);
				}
			}
			var action = (fileExists(destfile)) ? " (overwritten)" : " (created)";
			info("Using template:" & templatepath & " for " & destfile & action);

			if(findNoCase(".m.",templatepath)) {
				info("Using template:" & templatepath & " for " & destfile & action);
				var renderedcontent = "";
				var incfile = fileRead(templatepath);
				var randName = "fine#hash(now())#.cfm";
				fileWrite("/ram/#randName#",incfile);
				try{
					savecontent variable="renderedcontent"{
						model = duplicate(_model);
						metadata = duplicate(_model);
						include "/ram/#randName#";
					}
					fileDelete("/ram/#randName#");
					fileWrite(destfile,renderedcontent);
					pagePoolClear();
				} catch (any e) {
					for(tag in e.tagcontext) {
						if(tag.template.startsWith("ram:")) {
							tag.template = templatepath;
						}
					}
					throw(object=e);
				}
			}
			else if(findNoCase(".t.",templatepath)) {
				var args = StructNew();
				args.Metadata = duplicate(_model);
				args.TemplateFilePath = templatepath;
				args.DestinationFilePath = destfile;
				if (objectname gt "") {
					args.DestinationFilePath = replace(args.DestinationFilePath, "/object/", "/#lcase(objectname)#/");
				}
				cftemplate.generate(ArgumentCollection = args);
			} else {
				info("Copying " & templatepath & " to " & destfile & action);
			}
		}
	}

	function propertiesAsStruct(object) {
		var props = {};
		for(prop in object.getProperties()) {
			props[prop.getName()] = prop.getValue();
		}
		return props;
	}

	function getReqsAndFuncs(thingWithWidgets) {
		var reqsStruct = {};
		for(widget in thingWithWidgets.getWidgets()) {
			var reqFunkName = listLast(widget.getDojoType(),".");
			if(arrayLen(structFindValue(reqsStruct,reqFunkName)) != 0) {
				reqFunkName = reqFunkName & "2";
			}
			var dojoPath = replace(widget.getDojoType(),".","/","all");
			if(!StructKeyExists(reqsStruct,dojoPath)) {
				reqsStruct[dojoPath] = reqFunkName;
			}
			var recur = getReqsAndFuncs(widget);
			for(var req in recur) {
				reqsStruct[req] = recur[req];
			}
		}
		return reqsStruct;
	}

	function widgetDivs(thingWithWidgets,tablevel=0) {
		var divs = "";
		var widget = "";
		var tabs = "";
		for(var x = 0; x <= tablevel; x++) {
			tabs &= chr(9);
		}

		for(widget in thingWithWidgets.getWidgets()) {
			divs &= tabs;
			divs &= '<#widget.getDomType()# id="#widget.getId()#">#chr(13)#';
			if(widget.getDomContent("") != "") {
				divs &= tabs;
				divs &= widget.getDomContent() & chr(13);
				divs &= tabs;
			}
			divs &= widgetDivs(widget,tablevel+1);
			divs &= tabs;
			divs &= "</#widget.getDomType()#>#chr(13)#";
		}
		return divs;
	}

	function getWidgetsAsJS(thingWithWidgets,isSubwidget=false) {
		var widgets = "";
		var widget = "";
		for(widget in thingWithWidgets.getWidgets()) {
			widgets &= 'var #widget.getId()# = new #widget.getDojoType()#(#stringUtil.toJSON(propertiesAsStruct(widget))#, "#widget.getId()#");' & chr(13)&chr(10);
			for(func in widget.getFunctions() ) {
				var params = "";
				for(param in func.getParameters()) {
					params = listAppend(params,param.getName());
				}
				widgets &= "#widget.getId()#.#func.getName()# = function(#params#){#func.getBody()#};" & chr(13)&chr(10);
			}
			if(widget.getDojoType() == "dojox.mvc.Group"){
				// hack until mvc output widgets work right from code
				widgets &= "#widget.getId()#.startup();" & chr(13)&chr(10);
			}
			for(connect in widget.getConnects() ) {
				widgets &= "dojo.connect(#connect.getObj()#,#connect.getEvent()#,#connect.getContext()#,#connect.getMethod()#,#connect.getDontFix()#);" & chr(13)&chr(10);
			}
			widgets &= getWidgetsAsJS(widget,true);
			if(!isSubwidget || listFind("dojox.mvc.Output,dojox.mvc.Group,dojox.mvc.Repeat,app.widget.FormattedOutput,dijit.form.ValidationTextBox,dijit.form.FilteringSelect,dijit.form.DateTextBox,dijit.form.NumberTextBox,dijit.form.CheckBox",widget.getDojoType())){
				if(!isObject(widget.findPropertyByName("for",""))) {
					widgets &= "#widget.getId()#.startup();" & chr(13)&chr(10);
				}
			}
			/*
			widgets &= 'new #widget.getDojoType()#(#serializeJSON(propertiesAsStruct(widget))#);' & chr(13)&chr(10);
			widgets &= getWidgetsAsJS(widget,true);
			if(isSubwidget){
				widgets &= '#thingWithWidgets.getId()#.addChild(#widget.getId()#);' & chr(13)&chr(10);
			} else {
				widgets &= 'document.body.appendChild(#thingWithWidgets.getId()#.domNode);' & chr(13)&chr(10);
			}
			*/
		}
		return widgets;
	}

	function getWidgetsDeclarative(thingWithWidgets,tablevel=0) {
		var divs = "";
		var tabs = "";
		for(var x = 0; x <= tablevel; x++) {
			tabs &= chr(9);
		}
		for(widget in thingWithWidgets.getWidgets()) {
			var dataprops = stringUtil.toJSON(propertiesAsStruct(widget));
			dataprops = trim(replace(dataprops,'"',"'","all"));
			dataprops = mid(dataprops,2,len(dataprops)-2);
			divs &= tabs;
			divs &= '<div id="#widget.getId()#" data-dojo-id="#widget.getId()#" data-dojo-type="#widget.getDojoType()#" data-dojo-props="#dataprops#">#chr(13)#';
			for(func in widget.getFunctions() ) {
				var params = "";
				for(param in func.getParameters()) {
					params = listAppend(params,param.getName());
				}
				divs &= tabs;
				divs &= '<script type="dojo/method" data-dojo-event="#func.getName()#" data-dojo-args="#params#">'& chr(13);
				divs &= tabs;
				divs &=func.getBody() & chr(13);
				divs &= tabs;
				divs &= '</script>' & chr(13);
			}
			for(connect in widget.getConnects() ) {
				divs &= tabs;
				divs &= '<script type="dojo/connect" data-dojo-event=#connect.getEvent()# data-dojo-args="e">' & chr(13)&chr(10);
				divs &= tabs;
				divs &= "funk = " & connect.getMethod() & "; funk(e);" & chr(13)&chr(10);
				divs &= tabs;
				divs &= '</script>' & chr(13);
			}
			divs &= getWidgetsDeclarative(widget,tablevel+1);
			divs &= tabs;
			divs &= "</div>#chr(13)#";
		}
		return divs;
	}

}