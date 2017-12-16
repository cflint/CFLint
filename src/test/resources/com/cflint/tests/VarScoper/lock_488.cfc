component {
function test () {
        var test = 1;
        var r = {};
        lock name="mylock" type="exclusive" timeout="20" throwontimeout=true {
            r.test = test;
        }
    } 
}