component {
  public string function getStringFromStruct(required struct LocalData, required string keyToCheck) {
    if(!isNull(LocalData)) {
      if(StructKeyExists(LocalData, keyToCheck)) {
        return LocalData[keyToCheck];
      }
    }
    return '';
  }
}