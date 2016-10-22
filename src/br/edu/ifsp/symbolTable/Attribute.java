/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.symbolTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Dérick Welman
 */
public class Attribute {

	private Map<String, Object> features = new HashMap<String, Object>();

	/**
	 * Constructor for Atrribute
	 * @param String name - the name of attribute
	 * @param String type - the type of attribute
	 */
	public Attribute(String name, String type) {
		features.put("name", name);
		features.put("type", type);
	}

	/**
	 * Method that returns the name of attribute
	 * @return String name of attribute
	 */
	public String getName() {
		return (String) features.get("name");
	}

	/**
	 * Method that returns the type of attribute
	 * @return String type of attribute
	 */
	public String getType() {
		if (features.containsKey("type"))
			return (String) features.get("type");
		else
			return "null";
	}

	/**
	 * Method that adds a feature to attribute
	 * @param String name - the name of feature
	 * @param Object value - the value for feature
	 */
	public void addFeature(String name, Object value) {
		features.put(name, value);
	}

	/**
	 * Method that returns if is primary key
	 * @return true if find primary key in features or false if don't find
	 */
	public boolean isPrimaryKey() {
		return features.containsKey("primary");
	}

	/**
	 * Method that returns if is foreign key
	 * @return true if find reference in features or false if don't find
	 */
	public boolean isForeignKey() {
		return features.containsKey("reference");
	}

	/**
	 * Method that returns the value of a feature
	 * @param String name - name for search the feature
	 * @return Object containing the value
	 */
	public Object getFeature(String name) {
		return features.get(name);
	}

	/**
	 * Method that returns if exist a feature
	 * @param String name - Name for search the feature
	 * @return True if has the feature and false if has'nt
	 */
	public boolean hasFeature(String name) {
		return features.containsKey(name);
	}
	
	/**
	 * Method that return a set with the names of features
	 * @return Set with name of current attribute features
	 */
	public Set<String> getFeatureNames(){
		return features.keySet();
	}
}
