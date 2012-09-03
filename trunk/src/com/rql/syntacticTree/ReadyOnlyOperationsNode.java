package com.rql.syntacticTree;

import com.rql.parser.Token;
import com.rql.syntacticTree.interfaces.QueryNodeChildren;
import com.rql.syntacticTree.interfaces.ReadyOnlyOperationsNodeChildren;

public class ReadyOnlyOperationsNode extends Node implements QueryNodeChildren {

	private ReadyOnlyOperationsNodeChildren node;
	
	public ReadyOnlyOperationsNode(ReadyOnlyOperationsNodeChildren node) {
		super(node.getFirstToken());
		this.node = node;
	}

	@Override
	public Token getFirstToken() {
		return this.node.getFirstToken();
	}
}
