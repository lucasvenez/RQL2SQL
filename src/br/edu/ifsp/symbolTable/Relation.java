/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.symbolTable;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Derick
 */
public class Relation implements SchemaElement {
	HashMap<String, Attribute> attributes = new HashMap<>();

	public void addAttribute(String name, Attribute attribute) {
		attributes.put(name, attribute);
	}
	
	public void removeAttribute(String name){
		System.out.println("REMOVING:"+name);
		//attributes.remove(name);
		attributes.remove(name, this.getAttribute(name));
	}

	public Attribute getAttribute(String name) {
		return attributes.get(name);
	}

	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}
	
	public void renameAttribute(String name, String newName){
		Attribute temp = attributes.get(name);
		attributes.remove(name);
		attributes.put(newName, temp);
	}
	
	public void printAttributes(){
    	Set<String> attributeNames = attributes.keySet();
    	for(String name : attributeNames){
    		System.out.println(name);
    	}
    }
	
	public Set<String> getAttributeNames(){
		return attributes.keySet();
	}
	
	public int getNumberOfAttributes(){
		return attributes.size();
	}
}
