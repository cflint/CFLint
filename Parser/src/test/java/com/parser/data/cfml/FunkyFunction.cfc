component {
	function wee() {
		connectWidget(addButton,"onClick","null",'function(e){
	   		var item = {#modelobject.getIdentifier()#:"",isNew:true};
	   		console.log(item);
	   		modelApp.setModel("#modelobject.getName()#",item,"#modelobject.getIdentifier()#",function(ob){
			#addOnclick# });
			}');
	}

}