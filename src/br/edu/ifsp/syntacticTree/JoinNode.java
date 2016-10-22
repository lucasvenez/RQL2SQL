package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.BinaryOperationsNodeChildren;

/**
 * Class that represents a join operation
 * @author Dérick Welman
 */
public class JoinNode extends Node implements BinaryOperationsNodeChildren{
	
	private LogicalSentenceNode lsn = null;
	
	public JoinNode(Token position) {
		super(position);
	}
	
	public JoinNode(Token position, LogicalSentenceNode lsn) {
		super(position);
		this.lsn = lsn;
	}

	@Override
	public Token getFirstToken() {
		return this.getPosition();
	}

	@Override
	public int getNumber() {
		return this.number;
	}
	
	@Override
	public void setNumber(int number) {
		this.number = number;
	}
	
	public LogicalSentenceNode getLogicalSentenceNode(){
		return lsn;
	}

}
