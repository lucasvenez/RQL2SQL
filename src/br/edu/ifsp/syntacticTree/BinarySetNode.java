package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

/**
 * Class that represents a relation binary set
 * @author Dérick Welman
 */
public class BinarySetNode extends Node {

	private ReadOnlyOperationsNode roon1;
	private ReadOnlyOperationsNode roon2;
	private RelationNode rn1;
	private RelationNode rn2;

	public BinarySetNode() {
		super(new Token());
	}

	@Override
	public Token getFirstToken() {
		if (roon1 != null)
			return roon1.getFirstToken();
		else
			return rn1.getFirstToken();
	}
	
	/**
	 * Method that add the first relation of set
	 * @param ReadOnlyOperationsNode roon
	 */
	public void addFirstRelation(ReadOnlyOperationsNode roon){
		this.roon1 = roon;
	}
	
	/**
	 * Method that add the first relation of set
	 * @param RelationNode rn
	 */
	public void addFirstRelation(RelationNode rn){
		this.rn1 = rn;
	}
	
	/**
	 * Method that add the second relation of set
	 * @param ReadOnlyOperationsNode roon
	 */
	public void addSecondRelation(ReadOnlyOperationsNode roon){
		this.roon2 = roon;
	}
	
	/**
	 * Method that add the second relation of set
	 * @param RelationNode rn
	 */
	public void addSecondRelation(RelationNode rn){
		this.rn2 = rn;
	}
	
	/**
	 * Method that returns the first relation of set
	 * @return ReadOnlyOperationsNode
	 */
	public ReadOnlyOperationsNode getReadOnlyOperationsNode1(){
		return roon1;
	}
	
	/**
	 * Method that returns the second relation of set
	 * @return ReadOnlyOperationsNode
	 */
	public ReadOnlyOperationsNode getReadOnlyOperationsNode2(){
		return roon2;
	}

	/**
	 * Method that returns the first relation of set
	 * @return RelationNode
	 */
	public RelationNode getRelationNode1(){
		return rn1;
	}
	
	/**
	 * Method that returns the second relation of set
	 * @return RelationNode
	 */
	public RelationNode getRelationNode2(){
		return rn2;
	}
}
