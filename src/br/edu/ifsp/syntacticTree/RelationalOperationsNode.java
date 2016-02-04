package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.RelationalOperationsNodeChildren;

public class RelationalOperationsNode extends Node {

	private RelationalOperationsNodeChildren node;
	
	public RelationalOperationsNode( QueryNode queryNode ) {
		super( queryNode.getFirstToken() );
		this.node = queryNode;
	}

	@Override
	public Token getFirstToken() {
		return this.node.getFirstToken();
	}
	
	public RelationalOperationsNodeChildren getNode(){
		return this.node;
	}
}
