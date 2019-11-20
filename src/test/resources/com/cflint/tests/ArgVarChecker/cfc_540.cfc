component{
	property name="name" type="string";
	/**
	* This is a hint.
	* @name A name
	*/
	public string function sayHello(required string name) {
	    return "Hello, " & arguments.name;
	}
}