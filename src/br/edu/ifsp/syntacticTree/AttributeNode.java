package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

/**
 * Class that represents an projection attribute
 * @author Dérick Welman
 */
public class AttributeNode extends Node{

	public AttributeNode(Token position) {
		super(position);
	}

	@Override
	public Token getFirstToken() {
		return this.getPosition();
	}

}
