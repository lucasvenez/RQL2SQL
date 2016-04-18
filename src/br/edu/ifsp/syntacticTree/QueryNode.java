package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.QueryNodeChildren;
import br.edu.ifsp.syntacticTree.interfaces.RelationalOperationsNodeChildren;

public class QueryNode extends Node implements RelationalOperationsNodeChildren{

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

	@Override
	public int getNumber() {
		return this.number;
	}

	@Override
	public void setNumber(int number) {
		this.number=number;
	}
}
