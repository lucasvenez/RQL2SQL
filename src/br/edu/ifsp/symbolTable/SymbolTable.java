/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.symbolTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.edu.ifsp.symbolTable.exceptions.UnexistentForeignKeyException;

/**
 * Class responsible for store the database structure
 * 
 * @author Dérick Welman
 */
public class SymbolTable {

	private Map<String, SchemaElement> relations = new HashMap<String, SchemaElement>();

	public SymbolTable(String definition) {
		buildSymbolTable(definition);
	}

	/**
	 * Method that adds a relation to schema
	 * 
	 * @param String
	 *            name - the name of relation
	 */
	public void addRelation(String name) {
		relations.put(name, new Relation());
	}

	/**
	 * Method that adds a relation to schema
	 * 
	 * @param String
	 *            name - the name of relation
	 * @param Relation
	 *            r - a relation to be copied
	 */
	public void addRelation(String name, Relation r) {
		relations.put(name, r);
	}

	/**
	 * Method that search a relation by name
	 * 
	 * @param String
	 *            name - name for search
	 * @return Relation
	 */
	public Relation getRelation(String name) {
		if (!relations.containsKey(name)) {
			addRelation(name);
		}
		return (Relation) relations.get(name);
	}

	/**
	 * Method that returns the scheme has requested relationship
	 * 
	 * @param String
	 *            name - name for search
	 * @return true if has the requested relation, else return false
	 */
	public boolean hasRelation(String name) {
		return relations.containsKey(name);
	}

	/**
	 * Method that replace the content of a relation with the content of other
	 * 
	 * @param String
	 *            name - Name of relation that will be replace
	 * @param Relation
	 *            r - The new relation
	 */
	public void replaceRelation(String name, Relation r) {
		relations.replace(name, r);
	}

	/**
	 * Method that print the relation names
	 */
	public void printRelations() {
		Set<String> relationNames = relations.keySet();
		for (String name : relationNames) {
			System.out.println(name);
		}
	}

	/**
	 * Method that returns a set with relation names
	 * 
	 * @return Set with relation names
	 */
	public Set<String> getRelationNames() {
		return relations.keySet();
	}

	/**
	 * Method that prints the symbol table, containing the relations and its
	 * attributes
	 */
	public void printTable() {
		for (String relation : this.getRelationNames()) {
			System.out.println(relation + ":");
			for (String attribute : this.getRelation(relation).getAttributeNames()) {
				System.out.println("-->" + attribute);
			}
		}
	}

	/**
	 * Method that prints the complete symbol table, containing the relations,
	 * its attributes and the its features
	 */
	public void printCompleteTable() {
		for (String relation : this.getRelationNames()) {
			System.out.println(relation + ":");
			for (String attribute : this.getRelation(relation).getAttributeNames()) {
				System.out.println("-->" + attribute);
				for (String feature : this.getRelation(relation).getAttribute(attribute).getFeatureNames()) {
					Object featureValue = this.getRelation(relation).getAttribute(attribute).getFeature(feature);
					System.out.println("---->" + feature + featureValue);
				}
			}
		}
	}

	/**
	 * Method that builds the symbol table based on a definition parameter
	 * 
	 * @param String
	 *            definition - The database construction code
	 * @return True if the definition is valid or false if contrary
	 */
	public boolean buildSymbolTable(String definition) {
		definition = definition.replaceAll(" ", "");
		String identifierRegex = "([a-zA-Z_][a-zA-Z1-9_]*)";
		String typeRegex = "(VARCHAR|CHAR|INT|INTEGER|DOUBLE|DECIMAL|RATIONAL|FLOAT|LONG|BLOB)(\u005c\u005c([1-9]{1,3}\u005c\u005c))?";
		String primaryRegex = "(PRIMARY\u005c\u005cs?KEY)?";
		String foreignRegex = "(REFERENCES\u005c\u005cs?" + identifierRegex + "." + identifierRegex + ")?";
		String attributeRegex = identifierRegex + "." + identifierRegex + ":" + typeRegex + primaryRegex + foreignRegex
				+ ";";
		Pattern pattern = Pattern.compile(attributeRegex);
		Matcher matcher = pattern.matcher(definition);

		System.out.println("Building symbol table from:\n"+definition);
		if (!definition.matches("(" + attributeRegex + ")+")) {
			System.out.println("An error was found in database definition.");
			return false;
		}

		while (matcher.find()) {
			String relation = matcher.group(1);
			String attribute = matcher.group(2);
			String type = matcher.group(3);

			Attribute newAttribute = new Attribute(attribute, type);

			int range = 0;
			if (matcher.group(4) != null) {
				range = Integer.parseInt(matcher.group(4).replace("(", "").replace(")", ""));
				newAttribute.addFeature("range", range);
			}

			boolean primary = false;
			if (matcher.group(5) != null) {
				primary = true;
				newAttribute.addFeature("primary", primary);
			}

			String referenceRelation = matcher.group(7);
			String referenceAttribute = matcher.group(8);

			if (referenceRelation != null) {
				// Checks the foreign key
				if (this.hasRelation(referenceRelation)
						&& this.getRelation(referenceRelation).hasAttribute(referenceAttribute)) {
					newAttribute.addFeature("reference", new Reference(referenceRelation, referenceAttribute));
				} else {
					// Throw a exception for unexistent foreign key
					throw new UnexistentForeignKeyException();
				}
			}

			System.out.println("  Making a new entry:");
			System.out.println("    Relation: " + relation);
			System.out.println("    Attribute: " + attribute);
			System.out.println("    Type: " + type);
			System.out.println("    Range: " + range);
			System.out.println("    Primary: " + primary);
			System.out.println("    Reference relation: " + referenceRelation);
			System.out.println("    Reference attribute: " + referenceAttribute);
			System.out.println("  Closing entry:");

			this.getRelation(relation).addAttribute(attribute, newAttribute);
		}
		System.out.println("Finished the construction of the symbol table");
		return true;
	}
}
