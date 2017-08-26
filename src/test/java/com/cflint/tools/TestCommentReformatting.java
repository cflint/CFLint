package com.cflint.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestCommentReformatting {

    @Test
    public void testBasic() {
        String newText = CommentReformatting.wrap("<!---A<!---B--->C--->");
        assertEquals("<!---A<!--B-->C--->", newText);
    }

    @Test
    public void testBasic2() {
        String newText = CommentReformatting.wrap("<!---A<!---B--->C<!---D--->E--->");
        assertEquals("<!---A<!--B-->C<!--D-->E--->", newText);
    }

    @Test
    public void test() {
        String newText = CommentReformatting.wrap("<!---A<!---B--->C<!---D<!---E--->F--->--->");
        assertEquals("<!---A<!--B-->C<!--D<!--E-->F-->--->", newText);
    }

    @Test
    public void testDeep() {
        String newText = CommentReformatting.wrap("<!---AC<!---D<!---<!---E--->--->F--->--->");
        assertEquals("<!---AC<!--D<!--<!--E-->-->F-->--->", newText);
    }
}
