package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.BinaryOperationsNodeChildren;
import br.edu.ifsp.syntacticTree.interfaces.ReadyOnlyOperationsNodeChildren;
import br.edu.ifsp.syntacticTree.interfaces.UnitaryOperationsNodeChildren;

public class BinaryOperationsNode extends Node implements ReadyOnlyOperationsNodeChildren {

	private BinaryOperationsNodeChildren bonc = null;
	private BinarySetNode bst = null;
	
	public BinaryOperationsNode( BinaryOperationsNodeChildren bonc, BinarySetNode bst ) {
		super(bonc.getFirstToken());
		this.bonc = bonc;
		this.bst = bst;
	}

	@Override
	public Token getFirstToken() {
		return bonc.getFirstToken();
	}
	
	public BinaryOperationsNodeChildren getBinaryOperationsNodeChildren(){
		return bonc;
	}
	
	public BinarySetNode getBinarySetNode(){
		return bst;
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
