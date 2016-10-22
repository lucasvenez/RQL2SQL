package br.edu.ifsp.symbolTable;

/**
 * Class responsible for store the foreign key reference
 * @author Dérick Welman
 */
public class Reference {
	String relation;
	String attribute;

	/**
	 * Constructor
	 * @param String relation - the relation referenced
	 * @param String attribute - the attribute referenced
	 */
	public Reference(String relation, String attribute) {
		this.relation = relation;
		this.attribute = attribute;
	}
}
