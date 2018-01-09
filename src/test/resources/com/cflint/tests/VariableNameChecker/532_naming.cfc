component {

	public tagCFC function init() {
		
if (urlScope.keyExists('id2') && isNumeric(urlScope.id2)) {
    route &= '/:id2';
}
	}
}