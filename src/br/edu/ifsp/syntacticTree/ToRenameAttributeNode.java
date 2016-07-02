package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

/**
 * Class that represents an attribute that need to be renamed on rename operation
 * @author Dérick Welman
 */
public class ToRenameAttributeNode extends Node{

	public ToRenameAttributeNode(Token position) {
		super(position);
	}

	@Override
	public Token getFirstToken() {
		return this.getPosition();
	}

}
