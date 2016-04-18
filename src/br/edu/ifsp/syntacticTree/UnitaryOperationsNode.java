package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.ReadyOnlyOperationsNodeChildren;
import br.edu.ifsp.syntacticTree.interfaces.UnitaryOperationsNodeChildren;

public class UnitaryOperationsNode extends Node implements ReadyOnlyOperationsNodeChildren {

	private UnitaryOperationsNodeChildren uonc = null;
	private ReadyOnlyOperationsNode roon = null;
	private RelationNode rn = null;
	
	public UnitaryOperationsNode( UnitaryOperationsNodeChildren uonc, ReadyOnlyOperationsNode roon ) {
		super(uonc.getFirstToken());
		this.uonc = uonc;
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
		else if( uonc != null )
			return uonc.getFirstToken();
		else
			return roon.getFirstToken();
	}
	
	public UnitaryOperationsNodeChildren getUnitaryOperationsChildrenNode(){
		return this.uonc;
	}
	
	public ReadyOnlyOperationsNode getReadyOnlyOperationsNode(){
		return this.roon;
	}
	
	public RelationNode getRelationNode(){
		return this.rn;
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
