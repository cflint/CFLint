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
                    int en_start = endpos;
                    int en_end = endpos + 4;
                    while (sb.charAt(en_start-1)=='-'){
                        en_start--;
                    }
                    int st_start = pos;
                    int st_end = pos + 3;
                    while (sb.charAt(st_end+1)=='-'){
                        st_end++;
                    }
                    st_end++;
                    if(en_start<=st_end){
                        sb.replace(st_start, en_end, "<!--a-->");
                    }else {
                        sb.replace(en_start, en_end, "-->");
                        sb.replace(st_start, st_end, "<!--");
                    }
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
