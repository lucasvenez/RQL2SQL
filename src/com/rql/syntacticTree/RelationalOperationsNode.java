package com.rql.syntacticTree;

import com.rql.parser.Token;
import com.rql.syntacticTree.interfaces.RelationalOperationsNodeChildren;

public class RelationalOperationsNode extends Node {

	private RelationalOperationsNodeChildren node;
	
	public RelationalOperationsNode( QueryNode queryNode ) {
		super( queryNode.getFirstToken() );
	}

	@Override
	public Token getFirstToken() {
		return this.node.getFirstToken();
	}
}
