component{
  function foo(){
    var thisBase = getRowBaseFromMap(baseDataMap, row);
    for( var v in baseVars ){
      ar.Append(thisBase.KeyExists(v.shortname) ? thisBase[v.shortname] : '');
    }
  } 
}