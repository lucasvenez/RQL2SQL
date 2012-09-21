package com.rql.syntacticTree;

import com.rql.parser.Token;

public class ListNode extends Node {

	private Node node = null;
	
	private ListNode next = null;
	
	public ListNode( Node node ) {
		super(node.getPosition() );
		this.node = node;	
	}
	
	public ListNode( Node node, ListNode next ) {
		super( node.getPosition() );
		this.node = node;
		this.next = next;
	}
	
	public void add( Node node ) {
		
		if ( next == null ) next = new ListNode( node );
		else next.add( node );
	}
	
	public void setNode( Node node ) {
		this.node = node;
	}
	
	public Node getNode() {
		return this.node;
	}

	@Override
	public Token getFirstToken() {
		return node.getFirstToken();
	}
}
