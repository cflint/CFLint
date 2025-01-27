package com.cflint.tools;

public class ObjectEquals {

    private ObjectEquals() {
        throw new IllegalStateException("ObjectEquals utility class");
    }

    
    /** 
     * @param a a
     * @param b b
     * @return boolean
     */
    public static boolean equals(final Object a, final Object b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }
}
