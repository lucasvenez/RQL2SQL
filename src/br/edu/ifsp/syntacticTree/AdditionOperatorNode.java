package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class AdditionOperatorNode extends Node {

	MultiplicationSentenceNode msn1 = null;
	MultiplicationSentenceNode msn2 = null;
	AdditionOperatorNode next = null;
	
	public AdditionOperatorNode(Token t, MultiplicationSentenceNode msn1, MultiplicationSentenceNode msn2) {
		super(t);
		this.msn1 = msn1;
		this.msn2 = msn2;
	}
	
	public void add(Token t, MultiplicationSentenceNode msn){
		next = new AdditionOperatorNode(t, msn2, msn);
		msn2 = null;
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public MultiplicationSentenceNode getMultiplicationSentenceNode1(){
		return msn1;
	}
	
	public MultiplicationSentenceNode getMultiplicationSentenceNode2(){
		return msn2;
	}
	
	public AdditionOperatorNode getNextAdditionOperatorNode(){
		return next;
	}

}
