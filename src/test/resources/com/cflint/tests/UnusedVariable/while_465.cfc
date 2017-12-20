component{
function safetext(text) {
    var obracket = find("<",theText);        
    var badTag = "";
    var nextStart = "";
        while(obracket){
            badTag = REFindNoCase();
            if(badTag.pos[1]){
                nextStart = badTag.pos[1] + badTag.len[1];
            }
            else{
                nextStart = obracket + 1;
            }
        }
    }
}
}