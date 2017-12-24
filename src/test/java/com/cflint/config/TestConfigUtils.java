package com.cflint.config;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TestConfigUtils {

    final String input1 = "{\r\n" + "			  \"rule\" : [ ],\r\n"
            + "			  \"excludes\" : [ ],\r\n" + "			  \"includes\" : [ {\r\n"
            + "			    \"code\" : \"FUNCTION_HINT_MISSING\"\r\n" + "			  } ],\r\n"
            + "			  \"inheritParent\" : false\r\n" + "			}";
    final String input2 = "{\r\n" + "			  \"excludes\" : [ ],\r\n"
            + "			  \"xyzzy\" : false,\r\n" + "			  \"inheritParent\" : false,\r\n"
            + "			}";

    @Test
    public void testUnmarshalJson() throws JsonParseException, JsonMappingException, IOException {
        CFLintConfig config = ConfigUtils.unmarshalJson(input1, CFLintConfig.class);
        assertFalse(config.isInheritParent());
    }

    @Test
    public void testUnmarshalJson_Unknowns() throws JsonParseException, JsonMappingException, IOException {
        CFLintConfig config = ConfigUtils.unmarshalJson(input1, CFLintConfig.class);
        assertFalse(config.isInheritParent());
    }
}
