package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

/**
 * Class that represents a logical sentence
 * @author Dérick Welman
 */
public class LogicalSentenceNode extends Node{

	private ConditionalSentenceNode csn = null;
	private LogicalOperatorNode lon = null;
	
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
