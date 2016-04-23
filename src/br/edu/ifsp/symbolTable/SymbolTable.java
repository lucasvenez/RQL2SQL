/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.symbolTable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

/**
 *
 * @author Derick
 */
public class SymbolTable {

    private Map<String, SchemaElement> relations = new HashMap<String, SchemaElement>();

    public void addRelation(String name) {
        relations.put(name, new Relation());
    }
    
    public void addRelation(String name, Relation r) {
        relations.put(name, r);
    }

    public Relation getRelation(String name) {
        if (!relations.containsKey(name)) {
            addRelation(name);
        }
        return (Relation) relations.get(name);
    }

    public boolean hasRelation(String name) {
        return relations.containsKey(name);
    }
    
    public void replaceRelation(String name, Relation r){
    	relations.replace(name, r);
    }
    
    public void printRelations(){
    	Set<String> relationNames = relations.keySet();
    	for(String name : relationNames){
    		System.out.println(name);
    	}
    }
    
    public Set<String> getRelationNames(){
		return relations.keySet();
	}
    
    public void printTable(){
    	for(String relation : this.getRelationNames()){
    		System.out.println(relation+":");
    		for(String attribute : this.getRelation(relation).getAttributeNames()){
    			System.out.println("  -->"+attribute);
    		}
    	}
    }
}
