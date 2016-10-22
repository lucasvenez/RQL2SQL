package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

/**
 * Class that represents a comparison operator
 * @author Dérick Welman
 */
public class ComparisonOperatorNode extends Node {

	private InstanceofSentenceNode isn1 = null;
	private InstanceofSentenceNode isn2 = null;
	private ComparisonOperatorNode next = null;
	
	public ComparisonOperatorNode(Token t, InstanceofSentenceNode isn1, InstanceofSentenceNode isn2) {
		super(t);
		this.isn1 = isn1;
		this.isn2 = isn2;
	}
	
	/**
	 * Method that adds a instanceof sentence
	 * @param Token t - Position of occurrence on input
	 * @param InstanceofSentenceNode isn
	 */
	public void add(Token t, InstanceofSentenceNode isn){
		next = new ComparisonOperatorNode(t, isn2, isn);
		isn2 = null;
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Method that returns the first instanceof sentence
	 * @return InstanceofSentenceNode
	 */
	public InstanceofSentenceNode getInstanceofSentenceNode1(){
		return isn1;
	}
	
	/**
	 * Method that returns the second instanceof sentence
	 * @return InstanceofSentenceNode
	 */
	public InstanceofSentenceNode getInstanceofSentenceNode2(){
		return isn2;
	}
	
	/**
	 * Method that returns the comparison operator
	 * @return ComparisonOperatorNode
	 */
	public ComparisonOperatorNode getNextComparisonOperatorNode(){
		return next;
	}

}
