package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class MultiplicationSentenceNode extends Node{

	FactorNode fn = null;
	MultiplicationOperatorNode mon = null;
	
	public MultiplicationSentenceNode(FactorNode fn){
		super(fn.getFirstToken());
		this.fn=fn;
	}
	
	public MultiplicationSentenceNode(MultiplicationOperatorNode mon){
		super(mon.getFirstToken());
		this.mon=mon;
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public FactorNode getFactorNode(){
		return fn;
	}
	
	public MultiplicationOperatorNode getMultiplicationOperatorNode(){
		return mon;
	}

}
