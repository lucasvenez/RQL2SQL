package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.QueryNodeChildren;

public class QueryNode extends Node {

	private QueryNodeChildren node = null;
	
	public QueryNode( QueryNodeChildren node ) {
		super( node.getFirstToken() );
		this.node = node;
	}

	public QueryNodeChildren getNode() {
		return this.node;
	}

	public void setNode( QueryNodeChildren node) {
		this.node = node;
	}

	@Override
	public Token getFirstToken() {
		return this.node.getFirstToken();
	}
}
