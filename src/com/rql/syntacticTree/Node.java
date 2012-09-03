package com.rql.syntacticTree;

import com.rql.parser.Token;

public abstract class Node {

   private Token position;
   
   public Node( Token position ) {
	   this.position = position;
   }
   
   public Token getPosition() {
	   return this.position;
   }
   
   public void setPosition( Token position ) {
	   this.position = position;
   }
   
   public abstract Token getFirstToken();
}