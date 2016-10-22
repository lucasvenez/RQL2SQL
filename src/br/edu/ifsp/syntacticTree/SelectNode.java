package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.RelationalQueryLanguageConstants;
import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.UnitaryOperationsNodeChildren;

/**
 * Class that represents a selection operation
 * @author Dérick Welman
 */
public class SelectNode extends Node implements RelationalQueryLanguageConstants, UnitaryOperationsNodeChildren {

	private LogicalSentenceNode lsn = null;

	public SelectNode(LogicalSentenceNode lsn) {
		super(new Token(SELECT_TOKEN));
		this.lsn = lsn;
	}

	@Override
	public Token getFirstToken() {
		return new Token(SELECT_TOKEN);
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
