<cfcomponent>
<cffunction name="tag">
<cfquery name="variables.getTransactions" datasource="dsn">
	SELECT	*
	FROM	Transactions
</cfquery>
<cfquery name="variables.getTransactionsTransactions" datasource="dsn">
	SELECT	*
	FROM	Transactions
</cfquery>
</cffunction>
</cfcomponent>