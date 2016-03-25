package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class MultiplicationOperatorNode extends Node {

	FactorNode fn1 = null;
	FactorNode fn2 = null;
	MultiplicationOperatorNode next = null;
	
	public MultiplicationOperatorNode(Token t, FactorNode fn1, FactorNode fn2) {
		super(t);
		this.fn1 = fn1;
		this.fn2 = fn2;
	}
	
	public void add(Token t, FactorNode fn){
		next = new MultiplicationOperatorNode(t, fn2, fn);
		fn2 = null;
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public FactorNode getFactorNode1(){
		return fn1;
	}
	
	public FactorNode getFactorNode2(){
		return fn2;
	}
	
	public MultiplicationOperatorNode getNextMultiplicationOperatorNode(){
		return next;
	}

}
