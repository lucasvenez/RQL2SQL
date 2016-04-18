package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class ConditionalSentenceNode extends Node{

	ComparisonSentenceNode csn = null;
	ListNode ln = null;
	
	public ConditionalSentenceNode(ComparisonSentenceNode csn) {
		super(csn.getFirstToken());
		this.csn=csn;
	}
	
	public ConditionalSentenceNode(ComparisonSentenceNode csn, ListNode ln) {
		super(csn.getFirstToken());
		this.csn=csn;
		this.ln = ln;
	}
	
	public ComparisonSentenceNode getComparisonSentenceNode(){
		return csn;
	}
	
	public ListNode getIfListNode(){
		return ln;
	}

	@Override
	public Token getFirstToken() {
		return null;
	}

}
