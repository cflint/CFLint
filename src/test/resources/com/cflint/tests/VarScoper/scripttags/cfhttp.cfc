component{
    /**
     * Some function
     * @call The value of the call
     */
    public void function foo(
        required string call
    )
    {
        cfhttp(url="#variables.host#", port="#variables.port#", method="GET", timeout="30", result="httpResponse") {
            cfhttpparam(type="url", name="call", value="#arguments.call#");
        }
    }
}