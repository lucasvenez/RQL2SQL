/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.symbolTable;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Dérick Welman
 */
public class Relation implements SchemaElement {
	private HashMap<String, Attribute> attributes = new HashMap<>();

	/**
	 * Method that adds an attribute to schema
	 * 
	 * @param String
	 *            name - the name of attribute
	 */
	public void addAttribute(String name, Attribute attribute) {
		attributes.put(name, attribute);
	}

	/**
	 * Method that removes an attribute by name
	 * 
	 * @param String
	 *            name - Name of attribute to remove
	 */
	public void removeAttribute(String name) {
		System.out.println("REMOVING:" + name);
		// attributes.remove(name);
		attributes.remove(name, this.getAttribute(name));
	}

	/**
	 * Method that returns an attribute by name
	 * 
	 * @param String
	 *            name - Name for search attribute
	 * @return Attribute
	 */
	public Attribute getAttribute(String name) {
		return attributes.get(name);
	}

	/**
	 * Method that return if an attribute exist in relation
	 * 
	 * @param String
	 *            name - Name for search attribute
	 * @return True if relation has the attribute or false if it has'nt
	 */
	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	/**
	 * Method that rename an attribute
	 * 
	 * @param String
	 *            name - Name for search attribute
	 * @param String
	 *            newName - New name for attribute
	 */
	public void renameAttribute(String name, String newName) {
		Attribute temp = attributes.get(name);
		attributes.remove(name);
		attributes.put(newName, temp);
	}

	/**
	 * Method that prints the relation attributes
	 */
	public void printAttributes() {
		Set<String> attributeNames = attributes.keySet();
		for (String name : attributeNames) {
			System.out.println(name);
		}
	}

	/**
	 * Method that returns a set with the attribute names
	 * 
	 * @return Set with the attribute names
	 */
	public Set<String> getAttributeNames() {
		return attributes.keySet();
	}

	/**
	 * Method that returns the number o attributes in relation
	 * 
	 * @return Int with number of attributes
	 */
	public int getNumberOfAttributes() {
		return attributes.size();
	}
}
