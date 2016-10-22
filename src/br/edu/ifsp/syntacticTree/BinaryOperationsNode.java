package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.BinaryOperationsNodeChildren;
import br.edu.ifsp.syntacticTree.interfaces.ReadOnlyOperationsNodeChildren;
import br.edu.ifsp.syntacticTree.interfaces.UnitaryOperationsNodeChildren;

/**
 * Class that represents a binary operation
 * @author Dérick Welman
 */
public class BinaryOperationsNode extends Node implements ReadOnlyOperationsNodeChildren {

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
	
	/**
	 * Method that return the children binary operation
	 * @return BinaryOperationsNodeChildren
	 */
	public BinaryOperationsNodeChildren getBinaryOperationsNodeChildren(){
		return bonc;
	}
	
	/**
	 * Method that returns the children binary set
	 * @return
	 */
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
