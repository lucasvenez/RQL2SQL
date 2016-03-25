package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class ComparisonSentenceNode extends Node{

	InstanceofSentenceNode isn = null;
	ComparisonOperatorNode con = null;
	
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
