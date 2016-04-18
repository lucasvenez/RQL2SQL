package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class LogicalSentenceNode extends Node{

	ConditionalSentenceNode csn = null;
	LogicalOperatorNode lon = null;
	
	public LogicalSentenceNode(ConditionalSentenceNode csn){
		super(csn.getFirstToken());
		this.csn=csn;
	}
	
	public LogicalSentenceNode(LogicalOperatorNode lon){
		super(lon.getFirstToken());
		this.lon=lon;
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ConditionalSentenceNode getConditionalSentenceNode(){
		return csn;
	}
	
	public LogicalOperatorNode getLogicalOperatorNode(){
		return lon;
	}

}
