package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;

public class RenameSetNode extends Node{

	private ToRenameAttributeNode tran;
	private RenamedAttributeNode ran;
	
	public RenameSetNode(Token toRename, Token renamed) {
		super(toRename);
		tran = new ToRenameAttributeNode(toRename);
		ran = new RenamedAttributeNode(renamed);
		
	}

	@Override
	public Token getFirstToken() {
		return this.getPosition();
	}
	
	public ToRenameAttributeNode getToRenameAttributeNode(){
		return tran;
	}
	
	public RenamedAttributeNode getRenamedAttributeNode(){
		return ran;
	}

}
