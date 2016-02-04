package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class AttributeNode extends Node{

	public AttributeNode(Token position) {
		super(position);
	}

	@Override
	public Token getFirstToken() {
		return this.getPosition();
	}

}
