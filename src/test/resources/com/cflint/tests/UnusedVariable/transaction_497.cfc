component{
 function foo(){
   var foo = "";
    transaction {
    foo = 1;
    bar(foo);
   }
 }
}