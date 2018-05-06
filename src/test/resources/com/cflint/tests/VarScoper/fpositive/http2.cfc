component{
  function foo(resourceUri){
    var httpRequest = new Http(method=arguments.method, url=variables.ENDPOINT & arguments.resourceUri, charset="utf-8", timeout=arguments.timeout, throwOnError=false, username=variables.something, password=variables.somethingelse, getAsBinary="never");
  } 
}