/**
 * Test hint
 */
component {
	/**
	 * Converts date to formatted string-representation of date
	 * @dateObj The date object to format
	 */
	public string function formatDate(
		required date dateObj
	)
	{
		return dateFormat(arguments.dateObj, "mm/dd/yyyy");
	}
}