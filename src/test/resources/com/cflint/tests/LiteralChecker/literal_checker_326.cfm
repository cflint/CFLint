<cfset variables.list1 = "123,456,789">
					<cfset variables.list2 = "abc,def,ghi">
					<cfset variables.string1 = "0123456789">
					<cfset variables.string2 = "abcdefghi">

					<cfif (listFind(variables.list1, left(variables.string1, 3), ",") && listFind(variables.list1, left(variables.string2, 3), ","))
        || (listFind(variables.list2, left(variables.string1, 3), ",") && listFind(variables.list2, left(variables.string2, 3), ","))
					>
						Test
					</cfif>