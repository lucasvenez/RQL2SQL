package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class FactorNode extends Node{

	Token not = null;
	ConditionalSentenceNode csn = null;
	
	
	public FactorNode(Token not, Token t) {
		super(t);
		this.not = not;
	}
	
	public FactorNode(Token not, ConditionalSentenceNode csn){
		super(csn.getFirstToken());
		this.csn = csn;
		this.not = not;
	}
	
	public ConditionalSentenceNode getConditionalSentenceNode(){
		return csn;
	}
	
	public Token getNot(){
		return not;
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
