package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.BinaryOperationsNodeChildren;

/**
 * Class that represents a division operation
 * @author Dérick Welman
 */
public class DivisionNode extends Node implements BinaryOperationsNodeChildren{
	
	public DivisionNode(Token position) {
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
