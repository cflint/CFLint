/**
 * @hint test component
 */
component {

    /**
     * @hint demo function validate
     */
    public struct function validate( foo ) { //cflint ignore:ARG_TYPE_MISSING,ARG_DEFAULT_MISSING
        // validation logic

        if( isNull( arguments.foo ) ) {
            arguments.foo = '';
        }

        return {
            'foo' : arguments.foo,
        };
    }

}
