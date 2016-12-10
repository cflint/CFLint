package com.cflint.tools;

public class ObjectEquals {

	public static boolean equals(Object a, Object b){
		if(a==null){
			return b==null;
		}else{
			return a.equals(b);
		}
	}
}
