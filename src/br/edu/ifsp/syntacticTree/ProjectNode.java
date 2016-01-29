package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.RelationalQueryLanguageConstants;
import br.edu.ifsp.parser.Token;

public class ProjectNode extends Node implements RelationalQueryLanguageConstants{

	ListNode ln = null;
	
	public ProjectNode( ListNode ln ) {
		super( new Token( PROJECT_TOKEN ) );
		this.ln = ln;
	}

	@Override
	public Token getFirstToken() {
		return new Token( PROJECT_TOKEN );
	}
}
