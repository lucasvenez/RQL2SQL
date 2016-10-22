package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.QueryNodeChildren;
import br.edu.ifsp.syntacticTree.interfaces.ReadOnlyOperationsNodeChildren;

/**
 * Class that represents a read only operatioin
 * @author Lucas Venezian, Dérick Welman
 */
public class ReadOnlyOperationsNode extends Node implements QueryNodeChildren {

	private ReadOnlyOperationsNodeChildren node;
	
	public ReadOnlyOperationsNode(ReadOnlyOperationsNodeChildren node) {
		super(node.getFirstToken());
		this.node = node;
	}

	@Override
	public Token getFirstToken() {
		return this.node.getFirstToken();
	}
	
	public ReadOnlyOperationsNodeChildren getNode(){
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
