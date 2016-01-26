/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rql.symbolTable;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Derick
 */
public class SymbolTable {

    private Map<String, SchemaElement> relations = new HashMap<String, SchemaElement>();

    private void addRelation(String name) {
        relations.put(name, new Relation());
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
}
