package br.edu.ifsp.syntacticTree.interfaces;

import br.edu.ifsp.parser.Token;

public interface NodeChildren {

	public Token getFirstToken();
	public int getNumber();
	public void setNumber(int number);
}
