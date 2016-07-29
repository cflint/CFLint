component {
	property string username;
	property string password;

	public tagCFC function init(required string username, required string password) {
	    someVar = '';
		return this;
	}
}