package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.QueryNodeChildren;
import br.edu.ifsp.syntacticTree.interfaces.ReadyOnlyOperationsNodeChildren;

public class ReadyOnlyOperationsNode extends Node implements QueryNodeChildren {

	private ReadyOnlyOperationsNodeChildren node;
	
	public ReadyOnlyOperationsNode(ReadyOnlyOperationsNodeChildren node) {
		super(node.getFirstToken());
		this.node = node;
	}

	@Override
	public Token getFirstToken() {
		return this.node.getFirstToken();
	}
	
	public ReadyOnlyOperationsNodeChildren getNode(){
		return this.node;
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
