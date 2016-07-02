package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

/**
 * Class that represents an addition sentence
 * @author Dérick Welman
 */
public class AdditionSentenceNode extends Node{

	private MultiplicationSentenceNode msn = null;
	private AdditionOperatorNode aon = null;
	
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
	
	/**
	 * Method that returns the child multiplication sentence
	 * @return MultiplicationSentenceNode
	 */
	public MultiplicationSentenceNode getMultiplicationSentenceNode(){
		return msn;
	}
	
	/**
	 * Method that returns the child addition operator
	 * @return AdditionOperatorNode
	 */
	public AdditionOperatorNode getAdditionOperatorNode(){
		return aon;
	}

}
