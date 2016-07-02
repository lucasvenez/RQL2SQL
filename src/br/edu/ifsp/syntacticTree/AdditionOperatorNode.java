package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

/**
 * Class that represents an addition operator
 * @author Dérick Welman
 */
public class AdditionOperatorNode extends Node {

	private MultiplicationSentenceNode msn1 = null;
	private MultiplicationSentenceNode msn2 = null;
	private AdditionOperatorNode next = null;
	
	public AdditionOperatorNode(Token t, MultiplicationSentenceNode msn1, MultiplicationSentenceNode msn2) {
		super(t);
		this.msn1 = msn1;
		this.msn2 = msn2;
	}
	
	/**
	 * Method that add the child multiplication sentence
	 * @param Token t - Position of occurrence in input file
	 * @param msn - Multiplication Sentence Node
	 */
	public void add(Token t, MultiplicationSentenceNode msn){
		next = new AdditionOperatorNode(t, msn2, msn);
		msn2 = null;
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Method that returns the first child multiplication sentence
	 * @return MultiplicationSentenceNode
	 */
	public MultiplicationSentenceNode getMultiplicationSentenceNode1(){
		return msn1;
	}
	
	/**
	 * Method that returns the second child multiplication sentence
	 * @return MultiplicationSentenceNode
	 */
	public MultiplicationSentenceNode getMultiplicationSentenceNode2(){
		return msn2;
	}
	
	/**
	 * Method that returns the child addition operator
	 * @return MultiplicationSentenceNode
	 */
	public AdditionOperatorNode getNextAdditionOperatorNode(){
		return next;
	}

}
