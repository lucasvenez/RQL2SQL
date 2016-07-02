package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

/**
 * Class that represents a conditional sentence
 * @author Dérick Welman
 */
public class ConditionalSentenceNode extends Node{

	private ComparisonSentenceNode csn = null;
	private ListNode ln = null;
	
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
