package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

/**
 * Class that represents a multiplication sentence
 * @author Dérick Welman
 */
public class MultiplicationSentenceNode extends Node{

	private FactorNode fn = null;
	private MultiplicationOperatorNode mon = null;
	
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
