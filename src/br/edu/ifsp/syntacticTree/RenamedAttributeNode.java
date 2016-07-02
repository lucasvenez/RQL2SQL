package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

/**
 * Class that represents a renamed attribute of rename operation
 * @author Dérick Welman
 */
public class RenamedAttributeNode extends Node{

	public RenamedAttributeNode(Token position) {
		super(position);
	}

	@Override
	public Token getFirstToken() {
		return this.getPosition();
	}

}
