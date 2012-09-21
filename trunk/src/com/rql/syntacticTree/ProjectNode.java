package com.rql.syntacticTree;

import com.rql.parser.RelationalQueryLanguageConstants;
import com.rql.parser.Token;

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
