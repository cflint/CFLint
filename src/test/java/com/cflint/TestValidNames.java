package com.cflint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.cflint.plugins.core.ValidName;

public class TestValidNames {

    private ValidName name;

    @Before
    public void setUp() throws Exception {
        name = new ValidName(3, 20, 3);
    }

    @Test
    public void testInvalid() {
        assertFalse(name.isInvalid("camelCase","CamelCase"));
        assertFalse(name.isInvalid("product_id","CamelCase"));
        assertFalse(name.isInvalid("size","CamelCase"));
        assertFalse(name.isInvalid("lowercase","CamelCase"));
        assertFalse(name.isInvalid("UPPERCASE","CamelCase"));
        assertFalse(name.isInvalid("99_bottles_of_beer","CamelCase"));

        assertTrue(name.isInvalid("UppperCaseCamel","CamelCase"));
        assertFalse(name.isInvalid("endsInNumber99","CamelCase"));
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
        assertTrue(name.isCamelCase("product"));
        assertTrue(name.isCamelCase("camelCase"));
        assertTrue(name.isCamelCase("product4G"));
        assertTrue(name.isCamelCase("productID"));
        assertTrue(name.isCamelCase("requestViaHTTPS"));
        assertTrue(name.isCamelCase("myURLRequest"));

        assertFalse(name.isCamelCase("product_id"));
        assertFalse(name.isCamelCase("UpperCase"));
        assertFalse(name.isCamelCase("Uppercase"));
        assertFalse(name.isCamelCase("ProductID"));
        assertFalse(name.isCamelCase("PRODUCTID"));
    }

    @Test
    public void testIsCamelCaseUpper() {
        assertTrue(name.isPascalCase("Product"));
        assertTrue(name.isPascalCase("PamelCase"));
        assertTrue(name.isPascalCase("Product4G"));
        assertTrue(name.isPascalCase("ProductID"));
        assertTrue(name.isPascalCase("RequestViaHTTPS"));
        assertTrue(name.isPascalCase("MyURLRequest"));

        assertFalse(name.isPascalCase("product"));
        assertFalse(name.isPascalCase("Product_id"));
        assertFalse(name.isPascalCase("Product_Id"));
        assertFalse(name.isPascalCase("PRODUCTID"));
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
