component{
 function updateBucket(
      required struct data,
      required string cmc,
      required string wca,
      required array lookUp
  )
  {
      data.district[ARGUMENTS.lookUp[idx]] += 1;
      data.cmc[cmc][ARGUMENTS.lookUp[idx]] += 1;
      data.wca[wca][ARGUMENTS.lookUp[idx]] += 1;
  }
}