package com.cfbugs.tools;

import java.lang.reflect.Field;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFUnaryExpression;

public class CFTool {

	public static CFExpression sub(CFUnaryExpression expression){
		Field f;
		try {
			f = CFUnaryExpression.class.getDeclaredField("sub");
			f.setAccessible(true);
			return (CFExpression)(f.get(expression));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return null;
	}
}
