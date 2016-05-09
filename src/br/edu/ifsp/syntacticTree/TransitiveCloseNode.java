package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.UnitaryOperationsNodeChildren;

public class TransitiveCloseNode extends Node implements UnitaryOperationsNodeChildren{

	public TransitiveCloseNode(Token t){
		super(t);
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
		this.number=number;
	}

}
