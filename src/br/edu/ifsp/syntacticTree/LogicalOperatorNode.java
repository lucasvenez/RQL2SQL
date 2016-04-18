package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class LogicalOperatorNode extends Node {

	ConditionalSentenceNode csn1 = null;
	ConditionalSentenceNode csn2 = null;
	LogicalOperatorNode next = null;
	
	public LogicalOperatorNode(Token t, ConditionalSentenceNode csn1, ConditionalSentenceNode csn2) {
		super(t);
		this.csn1 = csn1;
		this.csn2 = csn2;
	}
	
	public void add(Token t, ConditionalSentenceNode csn){
		next = new LogicalOperatorNode(t, csn2, csn);
		csn2 = null;
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ConditionalSentenceNode getConditionalSentenceNode1(){
		return csn1;
	}
	
	public ConditionalSentenceNode getConditionalSentenceNode2(){
		return csn2;
	}
	
	public LogicalOperatorNode getNextLogicalOperatorNode(){
		return next;
	}

}
