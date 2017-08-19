component {
	private any function call( required string url ){
		var service = new http(
			  url       = arguments.url
			, method    = "GET"
			, charset   = "utf-8"
			, timeout   = variables.config.timeout
			, throwonerror = "false"
		);
	}
} 