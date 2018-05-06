component{
  function foo(resourceUri){
    var httpRequest = new HTTP(
        method = "get",
        url = ("http://api.foo.com" & resourceUri),
        charset = "utf-8"
            );
  } 
}