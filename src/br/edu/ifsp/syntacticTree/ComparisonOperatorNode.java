package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class ComparisonOperatorNode extends Node {

	InstanceofSentenceNode isn1 = null;
	InstanceofSentenceNode isn2 = null;
	ComparisonOperatorNode next = null;
	
	public ComparisonOperatorNode(Token t, InstanceofSentenceNode isn1, InstanceofSentenceNode isn2) {
		super(t);
		this.isn1 = isn1;
		this.isn2 = isn2;
	}
	
	public void add(Token t, InstanceofSentenceNode isn){
		next = new ComparisonOperatorNode(t, isn2, isn);
		isn2 = null;
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public InstanceofSentenceNode getInstanceofSentenceNode1(){
		return isn1;
	}
	
	public InstanceofSentenceNode getInstanceofSentenceNode2(){
		return isn2;
	}
	
	public ComparisonOperatorNode getNextComparisonOperatorNode(){
		return next;
	}

}
