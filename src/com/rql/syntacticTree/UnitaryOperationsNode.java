package com.rql.syntacticTree;

import com.rql.parser.Token;
import com.rql.syntacticTree.interfaces.ReadyOnlyOperationsNodeChildren;

public class UnitaryOperationsNode extends Node implements ReadyOnlyOperationsNodeChildren {

	public UnitaryOperationsNode(Token position) {
		super(position);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
