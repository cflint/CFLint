package com.cflint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.cflint.plugins.core.ValidName;

public class TestValidNames {

	private ValidName name;

	@Before
	public void setUp() {
		name = new ValidName(3, 20, 3);
	}

	@Test
	public void testInvalid() {
		assertFalse(name.isInvalid("camelCase"));
		assertFalse(name.isInvalid("product_id"));
		assertFalse(name.isInvalid("size"));
		assertFalse(name.isInvalid("lowercase"));
		assertFalse(name.isInvalid("UPPERCASE"));
		assertFalse(name.isInvalid("99_bottles_of_beer"));

		assertTrue(name.isInvalid("UppperCaseCamel"));
		assertTrue(name.isInvalid("endsInNumber99"));
	}

	@Test
	public void testValidChars() {
		assertTrue(name.validChars("camelCase"));
		assertTrue(name.validChars("product_id"));
		assertTrue(name.validChars("99_bottles_of_beer"));
		
		assertFalse(name.validChars("no$items"));
		assertFalse(name.validChars("no-items"));
		assertFalse(name.validChars("no+items"));
	}

	@Test
	public void testIsCamelCaseLower() {
		assertTrue(name.isCamelCaseLower("product"));
		assertTrue(name.isCamelCaseLower("camelCase"));
		assertTrue(name.isCamelCaseLower("product4G"));
		assertTrue(name.isCamelCaseLower("productID"));
		assertTrue(name.isCamelCaseLower("requestViaHTTPS"));
		assertTrue(name.isCamelCaseLower("myURLRequest"));

		assertFalse(name.isCamelCaseLower("product_id"));
		assertFalse(name.isCamelCaseLower("UpperCase"));
		assertFalse(name.isCamelCaseLower("Uppercase"));
		assertFalse(name.isCamelCaseLower("ProductID"));
		assertFalse(name.isCamelCaseLower("PRODUCTID"));
	}

	@Test
	public void testIsCamelCaseUpper() {
		assertTrue(name.isCamelCaseUpper("Product"));
		assertTrue(name.isCamelCaseUpper("PamelCase"));
		assertTrue(name.isCamelCaseUpper("Product4G"));
		assertTrue(name.isCamelCaseUpper("ProductID"));
		assertTrue(name.isCamelCaseUpper("RequestViaHTTPS"));
		assertTrue(name.isCamelCaseUpper("MyURLRequest"));

		assertFalse(name.isCamelCaseUpper("product"));
		assertFalse(name.isCamelCaseUpper("Product_id"));
		assertFalse(name.isCamelCaseUpper("Product_Id"));
		assertFalse(name.isCamelCaseUpper("PRODUCTID"));
	}

	@Test
	public void testUsesUnderscores() {
		assertTrue(name.usesUnderscores("product_name"));
		assertTrue(name.usesUnderscores("_atstart"));
		assertTrue(name.usesUnderscores("atend_"));
		assertTrue(name.usesUnderscores("lots_of_under_scores"));

		assertFalse(name.usesUnderscores("productName"));
		assertFalse(name.usesUnderscores("productname"));
	}

	@Test
	public void testEndsInNumber() {
		assertTrue(name.endsInNumber("endsInNumber99"));
		assertTrue(name.endsInNumber("temp2"));

		assertFalse(name.endsInNumber("productName"));
		assertFalse(name.endsInNumber("phone4G"));
		assertFalse(name.endsInNumber("99beers"));
	}

	@Test
	public void testTooShort() {
		assertTrue(name.tooShort("a"));
		assertTrue(name.tooShort("to"));

		assertFalse(name.tooShort("the"));
		assertFalse(name.tooShort("chars"));
		assertFalse(name.tooShort("averylongname"));
	}

	@Test
	public void testTooLong() {
		assertTrue(name.tooLong("123456789012345678901"));
		assertTrue(name.tooLong("aVeryLongNameThatIsTooLong"));

		assertFalse(name.tooLong("shortName"));
		assertFalse(name.tooLong("short_name"));
		assertFalse(name.tooLong("12345678901234567890"));
	}


	@Test
	public void testTooWordy() {
		assertTrue(name.tooWordy("thisIsAVeryWordyName"));
		assertTrue(name.tooWordy("alsoFarTooWordy"));

		assertFalse(name.tooWordy("ProductName"));
		assertFalse(name.tooWordy("aSimpleName"));
		assertFalse(name.tooWordy("simple"));
	}

	@Test
	public void testIsTemporary() {
		assertTrue(name.isTemporary("temp"));
		assertTrue(name.isTemporary("tmp"));
		assertTrue(name.isTemporary("myVar"));
		assertTrue(name.isTemporary("aFunc"));
		assertTrue(name.isTemporary("nameObj"));
		assertTrue(name.isTemporary("name_obj"));
		assertTrue(name.isTemporary("tmp_array"));

		assertFalse(name.isTemporary("temperature"));
		assertFalse(name.isTemporary("productName"));
		assertFalse(name.isTemporary("first_name"));
	}

	@Test
	public void testHasPrefixOrPostfix() {
		assertTrue(name.hasPrefixOrPostfix("stName"));
		assertTrue(name.hasPrefixOrPostfix("oPerson"));
		assertTrue(name.hasPrefixOrPostfix("qGet"));
		assertTrue(name.hasPrefixOrPostfix("nameStr"));
		assertTrue(name.hasPrefixOrPostfix("bValid"));

		assertFalse(name.isTemporary("temperature"));
		assertFalse(name.isTemporary("strength"));
		assertFalse(name.isTemporary("argument"));
		assertFalse(name.isTemporary("first_name"));
	}
}
