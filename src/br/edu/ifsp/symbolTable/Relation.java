/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.symbolTable;

import java.util.HashMap;

/**
 *
 * @author Derick
 */
public class Relation implements SchemaElement{
    HashMap<String, Attribute> attributes = new HashMap<>();
    
    public void addAttribute(String name, Attribute attribute){
        attributes.put(name, attribute);
    }
    
    public Attribute getAttribute(String name){
        return attributes.get(name);
    }
    
    public boolean hasAttribute(String name){
        return attributes.containsKey(name);
    }
}
