/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.symbolTable;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Derick
 */
public class Attribute {
	
	private Map<String, Object> features = new HashMap<String, Object>();

    public Attribute(String name, String type) {
    	features.put("name", name);
    	features.put("type", type);
    }

    public String getName() {
        return (String) features.get("name");
    }
    
    public String getType() {
        return (String) features.get("type");
    }
    
    public void addFeature(String name, Object value){
    	features.put(name, value);
    }
    
    public boolean isPrimaryKey(){
    	return features.containsKey("primary");
    }
    
    public boolean isForeignKey(){
    	return features.containsKey("reference");
    }
    
    public Object getFeature(String name){
    	return features.get(name);
    }
    
    public boolean hasFeature(String name){
    	return features.containsKey(name);
    }
    
}
