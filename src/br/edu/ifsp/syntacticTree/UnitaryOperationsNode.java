package br.edu.ifsp.syntacticTree;

import br.edu.ifsp.parser.Token;
import br.edu.ifsp.syntacticTree.interfaces.ReadOnlyOperationsNodeChildren;
import br.edu.ifsp.syntacticTree.interfaces.UnitaryOperationsNodeChildren;

/**
 * Class that represents an unitary operation
 * @author Lucas Venezian, Dérick Welman
 */
public class UnitaryOperationsNode extends Node implements ReadOnlyOperationsNodeChildren {

	private UnitaryOperationsNodeChildren uonc = null;
	private ReadOnlyOperationsNode roon = null;
	private RelationNode rn = null;
	
	public UnitaryOperationsNode( UnitaryOperationsNodeChildren uonc, ReadOnlyOperationsNode roon ) {
		super(uonc.getFirstToken());
		this.uonc = uonc;
		this.roon = roon;
	}
	
	public UnitaryOperationsNode( RelationNode rn ) {
		super(rn.getFirstToken());
		this.rn = rn;
	}
	
	public UnitaryOperationsNode( UnitaryOperationsNodeChildren uonc, RelationNode rn){
		super(rn.getFirstToken());
		this.rn=rn;
		this.uonc = uonc;
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
	
	public ReadOnlyOperationsNode getReadOnlyOperationsNode(){
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
