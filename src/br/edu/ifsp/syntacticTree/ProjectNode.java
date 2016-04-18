package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.RelationalQueryLanguageConstants;
import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.UnitaryOperationsNodeChildren;

public class ProjectNode extends Node implements RelationalQueryLanguageConstants, UnitaryOperationsNodeChildren{

	ListNode ln = null;
	
	public ProjectNode( ListNode ln ) {
		super( new Token( PROJECT_TOKEN ) );
		this.ln = ln;
	}

	@Override
	public Token getFirstToken() {
		return new Token( PROJECT_TOKEN );
	}
	
	public ListNode getProjectNodeList(){
		return this.ln;
	}
	
	@Override
	public int getNumber() {
		return this.number;
	}
	
	@Override
	public void setNumber(int number) {
		this.number = number;
	}
}
