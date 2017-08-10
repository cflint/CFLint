package com.cflint.tools;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TestCFMLTagInfo {

    private CFMLTagInfo tagInfo;
    @Before
    public void setUp() throws JsonParseException, JsonMappingException, IOException{
        tagInfo = new CFMLTagInfo();
    }
    @Test
    public void testCffeedAction(){
        assertTrue(tagInfo.isAssignmentAttribute("cffeed", "action"));
    }
    @Test
    public void testCfloopIndex(){
        assertTrue(tagInfo.isAssignmentAttribute("cfloop", "index"));
    }
    @Test
    public void testCfloopIndexCase(){
        assertTrue(tagInfo.isAssignmentAttribute("CFloop", "Index"));
    }
    @Test
    public void testCfloopBar(){
        assertFalse(tagInfo.isAssignmentAttribute("cfloop", "bar"));
    }
    @Test
    public void testFooBar(){
        assertFalse(tagInfo.isAssignmentAttribute("foo", "bar"));
    }
}
