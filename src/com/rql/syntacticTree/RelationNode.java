package com.rql.syntacticTree;

import com.rql.parser.Token;

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
