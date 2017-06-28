<cfscript>
_ = new Underscore();
//If you can pronounce this you are Teh Uber Nerd
slowX = _.memoize(function(n) { sleep(400*n); return n; });
thisTime = getTickCount();
slowX(4);
anotherTime = getTickCount();
slowX(4);
finalTime = getTickCount();
writeOutput("Time diff after first call: #anotherTime-thisTime#, diff after second call: #finalTime-anotherTime#");
</cfscript>