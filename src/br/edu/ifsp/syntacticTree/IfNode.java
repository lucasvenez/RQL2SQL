package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class IfNode extends Node {

	ComparisonSentenceNode csn1 = null;
	ComparisonSentenceNode csn2 = null;

	public IfNode(Token t, ComparisonSentenceNode csn1, ComparisonSentenceNode csn2) {
		super(t);
		this.csn1 = csn1;
		this.csn2 = csn2;
	}

	@Override
	public Token getFirstToken() {
		return null;
	}

	public ComparisonSentenceNode getComparisonSentenceNode1(){
		return csn1;
	}
	
	public ComparisonSentenceNode getComparisonSentenceNode2(){
		return csn2;
	}

}
