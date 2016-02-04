package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class ToRenameAttributeNode extends Node{

	public ToRenameAttributeNode(Token position) {
		super(position);
	}

	@Override
	public Token getFirstToken() {
		return this.getPosition();
	}

}
