// not compliant
component{
	function x(){
		sampleStruct.yetAnotherKey = "yetAnotherValue";
		//repeat
		sampleStruct.yetAnotherKey = "yetAnotherValue";
		sampleStruct.foo["abc"] = "yetAnotherValue";
		sampleStruct.foo().xyzzy["abc"] = "yetAnotherValue";
		sampleStruct.foo().bar = "yetAnotherValue";
		sampleStruct.foo() = "yetAnotherValue";
	}
}