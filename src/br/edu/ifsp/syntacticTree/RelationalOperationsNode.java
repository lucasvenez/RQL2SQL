package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.RelationalOperationsNodeChildren;

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
