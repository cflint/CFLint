package com.cflint.tools;

import java.util.Stack;

public class CommentReformatting {

    private static final String COMMENT_TEXT = "<!---";
    private static final String COMMENT_END_TEXT = "--->";

    private CommentReformatting() {
        throw new IllegalStateException("CommentReformatting utility class");
    }

    public static String wrap(final String value) {
        Stack<Integer> stack = new Stack<>();
        StringBuilder sb = new StringBuilder(value);
        int pos = sb.indexOf(COMMENT_TEXT);
        while (pos >= 0) {
            int endpos = sb.indexOf(COMMENT_END_TEXT, pos);
            int nextpos = sb.indexOf(COMMENT_TEXT, pos + 1);
            if ((endpos > 0 && endpos < nextpos) || nextpos < 0) {
                if (!stack.isEmpty()) {
                    sb.replace(endpos, endpos + 4, "-->");
                    sb.replace(pos, pos + 5, "<!--");
                    pos = stack.pop();
                    continue;
                }
            } else if (nextpos > 0 && nextpos < endpos) { // Nested
                stack.push(pos);
            }
            pos = sb.indexOf(COMMENT_TEXT, pos + 1);
        }
        return sb.toString();
    }

}
