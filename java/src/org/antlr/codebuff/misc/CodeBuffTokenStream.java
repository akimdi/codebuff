package org.antlr.codebuff.misc;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

import java.util.ArrayList;
import java.util.List;

/** Override to fix bug in LB() */
public class CodeBuffTokenStream extends CommonTokenStream {
	public CodeBuffTokenStream(TokenSource tokenSource) {
		super(tokenSource);
	}

	@Override
	protected Token LB(int k) {
		if ( k==0 || (p-k)<0 ) return null;

		int i = p;
		int n = 1;
		// find k good tokens looking backwards
		while ( i>=1 && n<=k ) {
			// skip off-channel tokens
			i = previousTokenOnChannel(i - 1, channel);
			n++;
		}
		if ( i<0 ) return null;
		return tokens.get(i);
	}

	public List<Token> getRealTokens(int from, int to) {
		List<Token> real = new ArrayList<Token>();
		for (int i=from; i<=to; i++) {
			Token t = tokens.get(i);
			if ( t.getChannel()!=Lexer.DEFAULT_TOKEN_CHANNEL ) real.add(t);
		}
		if ( real.size()==0 ) return null;
		return real;
	}
}