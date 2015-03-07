package com.cflint.integration;

import org.junit.Test;

import cfml.parsing.CFMLSource;

public class TestCFMLParse {
	
	@Test
	public void test() {
		final CFMLSource cfmlSource = new CFMLSource("<cfcomponent><cfset/></cfcomponent>");
		System.out.println(cfmlSource.getAllCFMLTags());
		System.out.println(cfmlSource.getAllElements());
		System.out.println(cfmlSource.getChildElements());
	}
}
