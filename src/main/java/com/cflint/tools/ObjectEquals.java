package com.cflint.tools;

public class ObjectEquals {

    public static boolean equals(final Object a, final Object b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }
}
