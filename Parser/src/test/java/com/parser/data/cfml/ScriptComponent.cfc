component output="false" {

	string function headSpin(required anything, area="elavator") {
		var toot = "se5ee6yye67tutuityit69t9imfuihki";
		return funk;
	};

	public struct function swipe(required anything, String whereAt="room") {
		var awesome = "sdd";
	};

	function babyCrawl(required struct kind, area="elavator") {
		var superfast = "yep";
	};
	
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

};