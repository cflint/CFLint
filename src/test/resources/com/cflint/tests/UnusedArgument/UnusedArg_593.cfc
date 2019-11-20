component{
/**
 * Some function hint
 * @call The value of call
 */
public void function foo(
    required string call
)
{
    var bar = "";
    cfhttp(url="#variables.host#", port="22", method="GET", timeout="30", result="local.httpResponse") {
        cfhttpparam(type="url", name="call", value="#arguments.call#");
        cfhttpparam(type="url", name="bar", value="#local.bar#");
    }
}
}