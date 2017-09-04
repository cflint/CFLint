package com.cflint.tools;

import org.antlr.v4.runtime.Token;

import com.cflint.plugins.Context;

import cfml.CFSCRIPTLexer;

public class PrecedingCommentReader {

    public static final String CFC_DEFAULT_EXTENSION = ".cfc";
    public static final String CFM_DEFAULT_EXTENSION = ".cfm";

    private PrecedingCommentReader() {
        throw new IllegalStateException("PrecedingCommentReader utility class");
    }

    public static String getMultiLine(final Context context, final Token token) {
        Iterable<Token> tokens = context.beforeTokens(token);
        for (Token currentTok : tokens) {
            if (currentTok.getChannel() == Token.HIDDEN_CHANNEL && currentTok.getType() == CFSCRIPTLexer.ML_COMMENT) {
                String mlText = currentTok.getText();
                return mlText == null ? null : mlText.trim();
            } else if (currentTok.getChannel() == Token.HIDDEN_CHANNEL && currentTok.getType() == CFSCRIPTLexer.NEWLINE) {
            } else if (currentTok.getLine() < token.getLine()) {
                break;
            }
        }
        return null;
    }
}
