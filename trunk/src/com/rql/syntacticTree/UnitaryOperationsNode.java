package com.rql.syntacticTree;

import com.rql.parser.Token;
import com.rql.syntacticTree.interfaces.ReadyOnlyOperationsNodeChildren;

public class UnitaryOperationsNode extends Node implements ReadyOnlyOperationsNodeChildren {

	private ListNode ln = null;
	private ReadyOnlyOperationsNode roon = null;
	private RelationNode rn = null;
	
	public UnitaryOperationsNode( ListNode listNode, ReadyOnlyOperationsNode roon ) {
		super(listNode != null ? listNode.getFirstToken() : roon.getFirstToken() );
		this.ln = listNode;
		this.roon = roon;
	}
	
	public UnitaryOperationsNode( RelationNode rn ) {
		super(rn.getFirstToken());
		this.rn = rn;
	}

	@Override
	public Token getFirstToken() {
		if ( rn != null )
			return rn.getFirstToken();
		else if( ln != null )
			return ln.getFirstToken();
		else
			return roon.getFirstToken();
	}
}
