package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.BinaryOperationsNodeChildren;

/**
 * Class that represents an intersection operation
 * @author Dérick Welman
 */
public class IntersectionNode extends Node implements BinaryOperationsNodeChildren{
	
	public IntersectionNode(Token position) {
		super(position);
	}

	@Override
	public Token getFirstToken() {
		return this.getPosition();
	}

	@Override
	public int getNumber() {
		return this.number;
	}

	@Override
	public void setNumber(int number) {
		this.number = number;
	}

}
