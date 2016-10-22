package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

/**
 * Class that represents a comparison sentence
 * @author Dérick Welman
 */
public class ComparisonSentenceNode extends Node{

	private InstanceofSentenceNode isn = null;
	private ComparisonOperatorNode con = null;
	
	public ComparisonSentenceNode(InstanceofSentenceNode isn){
		super(isn.getFirstToken());
		this.isn=isn;
	}
	
	public ComparisonSentenceNode(ComparisonOperatorNode con){
		super(con.getFirstToken());
		this.con=con;
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public InstanceofSentenceNode getInstanceofSentenceNode(){
		return isn;
	}
	
	public ComparisonOperatorNode getComparisonOperatorNode(){
		return con;
	}

}
