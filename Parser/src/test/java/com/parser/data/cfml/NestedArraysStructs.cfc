component output="false" persistent="false" {
 
	function package() {
		var cfdict = createOBject("java","com.dictionary.main.DictionaryManager");
		cfdict.initDictionaries();
		var attrs = cfdict.getDictionaryByVersion("ColdFusion9").getElementAttributes("cfproperty").iterator();
		var cfpropertyAttributes = [];
		var datatypes = new CFDataTypes().package().datatypes;
		while(attrs.hasNext()) {
			var attr = attrs.next();
			var cfpropertyAttribute = {};
			cfpropertyAttribute["name"] = attr.getName();
			cfpropertyAttribute["etype"] = "E" & attr.getType();
			cfpropertyAttribute["lowerBound"] = (attr.isRequired()) ? 1 : 0 ;
			cfpropertyAttribute["upperBound"] = 1;
			arrayAppend(cfpropertyAttributes,cfpropertyAttribute);
		}
		// add generic attributes for random metadata
		arrayAppend(cfpropertyAttributes,{name="attributes", reference:"Attribute",containment:true, lowerBound:0,upperBound:-1});
		cfpropertyAttribute = {};
		cfpropertyAttribute["name"] = "id";
		cfpropertyAttribute["etype"] = "EString";
		cfpropertyAttribute["lowerBound"] = 0;
		cfpropertyAttribute["upperBound"] = 1;
		arrayAppend(cfpropertyAttributes,cfpropertyAttribute);
		
		var package = {
			name:"cfcPackage",
			datatypes:datatypes,
			classes : [
				{ name:"CFCModel",
					features : [
						{name="package", etype:"EString", lowerBound:0,upperBound:1},
						{name="cfcs", reference:"ORMEntity",containment:true, lowerBound:0,upperBound:-1}
					]
				},
				{ name:"ORMEntity",
					features : [
						{name="name", etype:"String", lowerBound:0,upperBound:1},
						{name="extends", etype:"EString", lowerBound:0,upperBound:1},
						{name="table", etype:"EString", lowerBound:0,upperBound:1},
						{name="accessors", etype:"EBoolean", lowerBound:0,upperBound:1},
						{name="persistent", etype:"EBoolean", lowerBound:0,upperBound:1},
						{name="alias", etype:"EString", lowerBound:0,upperBound:1},
						{name="bindingname", etype:"EString", lowerBound:0,upperBound:1},
						{name="displayname", etype:"EString", lowerBound:0,upperBound:1},
						{name="hint", etype:"EString", lowerBound:0,upperBound:1},
						{name="mappedSuperClass", etype:"EBoolean", lowerBound:0,upperBound:1},
						{name="namespace", etype:"EString", lowerBound:0,upperBound:1},
						{name="output", etype:"EBoolean", lowerBound:0,upperBound:1},
						{name="serializable", etype:"EBoolean", lowerBound:0,upperBound:1},
						{name="porttypename", etype:"EString", lowerBound:0,upperBound:1},
						{name="serviceaddress", etype:"EString", lowerBound:0,upperBound:1},
						{name="serviceportname", etype:"EString", lowerBound:0,upperBound:1},
						{name="wsdlfile", etype:"EString", lowerBound:0,upperBound:1},
						{name="style", etype:"styleENum", lowerBound:0,upperBound:1},
						{name="attributes", reference:"Attribute",  containment:true, lowerBound:0,upperBound:-1},
						{name="variables", reference:"variable",  containment:true, lowerBound:0,upperBound:-1},
						{name="properties", reference:"property", containment:true, lowerBound:0,upperBound:-1},
						{name="functions", reference:"function", containment:true, lowerBound:0,upperBound:-1}
					]
				},
				{ name:"fileinfo",
					features : [
						{name="path", etype:"EString"}
					]
				},
				{ name:"variable",
					features : [
						{name="name", etype:"EString"},
						{name="value", etype:"EString"}
					]
				},
				{ name:"Attribute",
					features : [
						{name="name", etype:"EString"},
						{name="value", etype:"EString"}
					]
				},
				{ name:"Parameter",
					features : [
						{name="name", etype:"EString"},
						{name="value", etype:"EString"},
						{name="default", etype:"EString"},
						{name="required", etype:"EBoolean"}
					]
				},
				{ name:"property",
					features : cfpropertyAttributes
				},
				{ name:"function",
					features : [
						{name="name", etype:"EString"},
						{name="parameters", reference:"Parameter",containment:true, lowerBound:0,upperBound:-1},
						{name="attributes", reference:"Attribute",containment:true, lowerBound:0,upperBound:-1},
						{name="body", etype:"EString"}
					]
				}
			],
			enums: [
				{ name:"styleENum", etype="EString",
					literals : [
						{name:"standard",literal:"standard"},
						{name:"awesome",literal:"awesome"}
					]
				}
			]
		};
		return package;
	}

	function saveMetaModel(filepath) {
		var ECore = createObject("component","moshen.ecore.Ecore");
		//grab cfproperty attrs from cfml dict
		var epackage = ecore.load(package());
		ECore.saveMetaModel(epackage,filepath);
	}

}