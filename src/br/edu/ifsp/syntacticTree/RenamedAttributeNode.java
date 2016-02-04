package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class RenamedAttributeNode extends Node{

	public RenamedAttributeNode(Token position) {
		super(position);
	}

	@Override
	public Token getFirstToken() {
		return this.getPosition();
	}

}
