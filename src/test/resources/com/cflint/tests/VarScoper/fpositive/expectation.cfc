component{
  function foo(){
    var oExpectation = new Expectation( spec=this, assertions=this.$assert, mockbox=this.$mockbox );
    thread.closures = arguments.closures;
  } 
}