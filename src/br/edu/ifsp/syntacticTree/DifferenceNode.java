package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.BinaryOperationsNodeChildren;

/**
 * Class that represents a difference operation
 * @author Dérick Welman
 */
public class DifferenceNode extends Node implements BinaryOperationsNodeChildren{
	
	public DifferenceNode(Token position) {
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
