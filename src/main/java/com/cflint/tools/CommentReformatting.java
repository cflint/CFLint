package com.cflint.tools;

import java.util.Stack;

public class CommentReformatting {
	
	public static String wrap(String value) {
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
			} else if (nextpos > 0 && nextpos < endpos) {// Nested
				stack.push(pos);
			}
			pos = sb.indexOf(COMMENT_TEXT, pos + 1);
		}
		return sb.toString();
	}
	
	static String COMMENT_TEXT = "<!---";
	static String COMMENT_END_TEXT = "--->";
}
