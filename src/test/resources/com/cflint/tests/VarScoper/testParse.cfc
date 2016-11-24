component hint="testing" accessors = "true" {

  /**
  * @hint "This is a test"
  * @thing "a hint?""
  */
  public numeric function save( required component thing ) {

    var sql = "INSERT INTO thingTbl(
      thingId,
      name) VALUES (
      :thingId,
      :name";

    var params = {
      thingId = { "value" = thing.getID(), "cfsqltype" = "CF_SQL_INTEGER" },
      name = { "value" = thing.getName(), "cfsqltype" = "CF_SQL_VARCHAR", null = !len( thing.getName() ) }
    };

    QueryExecute( sql, params );

    return thing.getID();
  }

}