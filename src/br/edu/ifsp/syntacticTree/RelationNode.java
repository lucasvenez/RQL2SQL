package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class RelationNode extends Node {

	Token identifier = null;
	
	public RelationNode( Token token ) {
		super(token);
		identifier = token;
	}

	@Override
	public Token getFirstToken() {
		return identifier;
	}
}
