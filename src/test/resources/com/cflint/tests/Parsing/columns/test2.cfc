/**
 * Test hint
 */
component {
    /**
     * Converts date to formatted string-representation of date
     * @dateObj The date object to format
     * @formatStr The format string mask
     */
    public string function formatDate(
        required date dateObj,
        string formatStr="mm/dd/yyyy"
    )
    {
        return dateFormat(arguments.dateObj, arguments.formatStr);
    }
}