package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.RelationalQueryLanguageConstants;
import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.UnitaryOperationsNodeChildren;

public class SelectNode extends Node implements RelationalQueryLanguageConstants, UnitaryOperationsNodeChildren{

	Token t = null;
	
	public SelectNode( Token t ) {
		super( new Token( SELECT_TOKEN ) );
		this.t = t;
	}

	@Override
	public Token getFirstToken() {
		return new Token( SELECT_TOKEN );
	}
	
	@Override
	public int getNumber() {
		return this.number;
	}

	@Override
	public void setNumber(int number) {
		this.number=number;
	}
}
