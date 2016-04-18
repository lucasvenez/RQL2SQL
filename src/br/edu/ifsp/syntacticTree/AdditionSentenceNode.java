package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class AdditionSentenceNode extends Node{

	MultiplicationSentenceNode msn = null;
	AdditionOperatorNode aon = null;
	
	public AdditionSentenceNode(MultiplicationSentenceNode msn){
		super(msn.getFirstToken());
		this.msn=msn;
	}
	
	public AdditionSentenceNode(AdditionOperatorNode aon){
		super(aon.getFirstToken());
		this.aon=aon;
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public MultiplicationSentenceNode getMultiplicationSentenceNode(){
		return msn;
	}
	
	public AdditionOperatorNode getAdditionOperatorNode(){
		return aon;
	}

}
