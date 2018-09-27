component{
package query function foo() {
    var sampleQuery = queryNew("valid");
    var sampleQueryOfQuery = new Query(SampleQuery="#sampleQuery#");
    sampleQueryOfQuery.setName("sampleQueryOfQuery");
    sampleQueryOfQuery.setDbType("query");
    sampleQueryOfQuery.setSql("
        SELECT  *
        FROM    SampleQuery
        WHERE   valid = 1
    ");

    return sampleQueryOfQuery.execute().getResult();
}
}