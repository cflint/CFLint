/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package treetool;

import java.io.FileReader;
import java.io.FileWriter;

import org.antlr.grammar.v3.ANTLRv3Lexer;
import org.antlr.grammar.v3.ANTLRv3Parser;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.TokenRewriteStream;

/**
 * Transform a grammar, adding tree construction code.
 * 
 * @author Yang Jiang (yang.jiang.z@gmail.com)
 */
public class GrammarTransformer {
	public static void main(String args[]) throws Exception {
		if (args.length != 2) {
			System.out.println("Please provide input filename and output filename");
			return;
		}
		
		CharStream input = null;
		input = new ANTLRFileStream(args[0]);
		
		ANTLRv3Lexer lex = new ANTLRv3Lexer(input);
		TokenRewriteStream tokens = new TokenRewriteStream(lex);
		ANTLRv3Parser g = new ANTLRv3Parser(tokens);
		g.grammarDef();
		
		String newContent = tokens.toString();
		
		FileReader r = new FileReader(args[0]);
		StringBuffer buf = new StringBuffer();
		char[] c = new char[1024];
		int len = 0;
		len = r.read(c);
		while (len > 0) {
			buf.append(c, 0, len);
			len = r.read(c);
		}
		
		r.close();
		String oldContent = buf.toString();
		int index = oldContent.indexOf("Lexer Section");
		oldContent = oldContent.substring(index);
		
		index = newContent.indexOf("Lexer Section");
		if (index == -1) {
			System.err
					.println("Can't find lexer part in the grammar. You have to mark the start lexer part with \"Lexer Section\" comment. ");
			return;
		}
		
		newContent = newContent.substring(0, index);
		newContent = newContent + oldContent;
		
		FileWriter writer = new FileWriter(args[1]);
		writer.write(newContent);
		writer.close();
	}
	
}
