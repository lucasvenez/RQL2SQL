package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class BinarySetNode extends Node {

	private ReadyOnlyOperationsNode roon1;
	private ReadyOnlyOperationsNode roon2;
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
	
	public void addFirstRelation(ReadyOnlyOperationsNode roon){
		this.roon1 = roon;
	}
	
	public void addFirstRelation(RelationNode rn){
		this.rn1 = rn;
	}
	
	public void addSecondRelation(ReadyOnlyOperationsNode roon){
		this.roon2 = roon;
	}
	
	public void addSecondRelation(RelationNode rn){
		this.rn2 = rn;
	}
	
	public ReadyOnlyOperationsNode getReadyOnlyOperationsNode1(){
		return roon1;
	}
	
	public ReadyOnlyOperationsNode getReadyOnlyOperationsNode2(){
		return roon2;
	}

	public RelationNode getRelationNode1(){
		return rn1;
	}

	public RelationNode getRelationNode2(){
		return rn2;
	}
}
