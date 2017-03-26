component{
  private Boolean function regexCheck(
        required string regex,
        required string toCheck
  )
  {
        var myRet = false;
        myRet = (refind(ARGUMENTS.regex, ARGUMENTS.toCheck)) ? True : False;
        return myRet;
  }
}