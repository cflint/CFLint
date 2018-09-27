component{
    function foo(){
      var myQry = new Query(); // new query object     
      myQry.setSQL("select * from app.books where bookid = #bookid# and id = #id#"); 
    }
}