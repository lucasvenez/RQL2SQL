package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class InstanceofSentenceNode extends Node {

	AdditionSentenceNode asn = null;
	Token type = null;

	public InstanceofSentenceNode(AdditionSentenceNode asn) {
		super(asn.getFirstToken());
		this.asn = asn;
	}

	public InstanceofSentenceNode(AdditionSentenceNode asn, Token type) {
		super(asn.getFirstToken());
		this.asn = asn;
		this.type=type;
	}
	
	public AdditionSentenceNode getAdditionSentenceNode(){
		return asn;
	}
	
	public Token getType(){
		return type;
	}

	@Override
	public Token getFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
