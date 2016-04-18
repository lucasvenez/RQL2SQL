package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.RelationalQueryLanguageConstants;
import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.UnitaryOperationsNodeChildren;

public class RenameNode extends Node implements RelationalQueryLanguageConstants, UnitaryOperationsNodeChildren{

	ListNode ln = null;
	
	public RenameNode( ListNode ln ) {
		super( new Token( RENAME_TOKEN ) );
		this.ln = ln;
	}

	@Override
	public Token getFirstToken() {
		return new Token( RENAME_TOKEN );
	}
	
	public ListNode getRenameSetNodeList(){
		return this.ln;
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
