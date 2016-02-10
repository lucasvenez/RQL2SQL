package br.edu.ifsp.semanticAnalysis;

import br.edu.ifsp.symbolTable.*;
import br.edu.ifsp.syntacticTree.*;
import br.edu.ifsp.syntacticTree.interfaces.*;

public class RelationCheck {
	SymbolTable schema;
	int semanticErrors;
	int globalScope;

	public RelationCheck(SymbolTable st) {
		schema = st;
		semanticErrors = 0;
		globalScope = 0;
	}

	public int getSemanticErrors(){
		return semanticErrors;
	}
	
	public int semanticAnalysis(ListNode root){
		if (root == null) {
			System.out.println("Nothing to be analysed");
			return 0;
		}
		System.out.println("Starting the semantic analysis");
		relationalOperationsNodeListCheck(root);
		return semanticErrors;
	}
	
	public void relationalOperationsNodeListCheck(ListNode x) {
		if (x == null) {
			return;
		}
		relationalOperationsNodeCheck((RelationalOperationsNode) x.getNode());
		relationalOperationsNodeListCheck(x.getNext());
	}

	public void relationalOperationsNodeCheck(RelationalOperationsNode x) {
		if (x == null)
			return;
		if (x.getNode() instanceof QueryNode)
			queryNodeCheck((QueryNode) x.getNode());
	}

	public void queryNodeCheck(QueryNode x) {
		if (x == null)
			return;
		if (x.getNode() instanceof ReadyOnlyOperationsNode) {
			globalScope++;
			readyOnlyOperationsNodeCheck((ReadyOnlyOperationsNode) x.getNode(), globalScope);

		}
	}

	public void readyOnlyOperationsNodeCheck(ReadyOnlyOperationsNode x, int scope) {
		if (x == null)
			return;
		if (x.getNode() instanceof UnitaryOperationsNode)
			schema.addRelation("temporaryRelation" + scope);
		unitaryOperationsNodeCheck((UnitaryOperationsNode) x.getNode(), scope);
	}

	public void unitaryOperationsNodeCheck(UnitaryOperationsNode x, int scope) {
		if (x == null)
			return;

		if (x.getRelationNode() != null) {
			relationNodeCheck(x.getRelationNode(), scope);
		} else {
			globalScope++;
			readyOnlyOperationsNodeCheck(x.getReadyOnlyOperationsNode(), globalScope);
		}

		if (x.getUnitaryOperationsChildrenNode() instanceof ProjectNode)
			projectNodeCheck((ProjectNode) x.getUnitaryOperationsChildrenNode(), scope);
		if (x.getUnitaryOperationsChildrenNode() instanceof RenameNode)
			renameNodeCheck((RenameNode) x.getUnitaryOperationsChildrenNode(), scope);

	}

	public void projectNodeCheck(ProjectNode x, int scope) {
		if (x == null)
			return;
		attributeNodeListCheck(x.getProjectNodeList(), scope);
	}

	public void attributeNodeListCheck(ListNode x, int scope) {
		if (x == null)
			return;
		attributeNodeCheck((AttributeNode) x.getNode(), scope);
		attributeNodeListCheck(x.getNext(), scope);
	}

	public void attributeNodeCheck(AttributeNode x, int scope) {
		if (x == null)
			return;
		Relation r = schema.getRelation("temporaryRelation" + (scope + 1));
		if (r.hasAttribute(x.getPosition().image)) {
			schema.getRelation("temporaryRelation" + scope).addAttribute(x.getPosition().image,
					r.getAttribute(x.getPosition().image));
		} else {
			semanticErrors++;
			System.out.println("Attribute does not exist: \"" + x.getPosition().image
					+ "\" at the line " + x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
		}
	}

	// public void SelectNodeCheck(){
	// }

	public void renameNodeCheck(RenameNode x, int scope) {
		if (x == null)
			return;
		renameSetNodeListCheck(x.getRenameSetNodeList(), scope);
	}
	
	public void renameSetNodeListCheck(ListNode x, int scope){
		if (x == null)
			return;
		renameSetNodeCheck((RenameSetNode) x.getNode(), scope);
		renameSetNodeListCheck(x.getNext(), scope);
	}
	
	public void renameSetNodeCheck(RenameSetNode x, int scope){
		if (x == null)
			return;
		Relation r = schema.getRelation("temporaryRelation" + (scope + 1));
		String toRename = x.getToRenameAttributeNode().getPosition().image;
		String renamed = x.getRenamedAttributeNode().getPosition().image;
		if (r.hasAttribute(toRename)){
			schema.getRelation("temporaryRelation"+scope).renameAttribute(toRename, renamed);
		}else{
			semanticErrors++;
			System.out.println("Attribute does not exist: \"" + x.getToRenameAttributeNode().getPosition().image
					+ "\" at the line " + x.getToRenameAttributeNode().getPosition().beginLine + ", column " + x.getToRenameAttributeNode().getPosition().beginColumn);
		}
		
	}

	public void relationNodeCheck(RelationNode x, int scope) {
		if (x == null)
			return;
		if (schema.hasRelation(x.getPosition().image)) {
			schema.replaceRelation("temporaryRelation" + scope, schema.getRelation(x.getPosition().image));
		} else {
			semanticErrors++;
			System.out.println("Relation does not exist: \"" + x.getPosition().image + "\" at the line "
					+ x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
		}
	}

}
