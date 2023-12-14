component {
  public string function getStringFromStruct(required struct shelfLevelX,required string keyToCheck) {
    if(!isNull(shelfLevelX )) {
      if(StructKeyExists(shelfLevelX , keyToCheck)) {
        return shelfLevelX [keyToCheck];
      }
    }
    return '';
  }
}