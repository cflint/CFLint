<!--- contains comparison operations that don't 'do' anything --->
<cfscript>
x = {};
x.x = 1;
x.x == 2;
if (x.x == 2){
  x=123;
}
x.x EQ 6;
b1 = x == y;
b2 = x EQ y;
</cfscript>