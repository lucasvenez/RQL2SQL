package br.edu.ifsp.codeGeneration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import br.edu.ifsp.symbolTable.SymbolTable;
import br.edu.ifsp.syntacticTree.*;

public class CodeGenerator {

	File f;
	PrintWriter pw;
	String output = "";
	ArrayList<String> attributes = new ArrayList<String>();
	SymbolTable schema;
	int globalScope;

	/**
	 * Method used to initialize the generator
	 */
	public CodeGenerator(SymbolTable schema, String outputFile, String version) throws IOException {
		this.schema = schema;
		globalScope = 0;
		System.out.println(outputFile);
		f = new File(outputFile);
		// if (f.canWrite()) {
		if (!f.exists())
			f.createNewFile();
		pw = new PrintWriter(f);
		// } else {
		// System.out.println("Permission denied for output location");
		// }
		output = "/*" + version + "*/\r\n\r\n";
	}

	/**
	 * Method used to generate the SQL from analysis
	 * 
	 * @param root
	 */
	public void generate(ListNode root) {
		generateRelationalOperationsNodeList(root);
		System.out.println(output);
		pw.println(output);
		pw.close();
	}

	/*
	 * *********************************************************************
	 * Generate the output SQL from syntactic tree
	 **********************************************************************/

	public void generateRelationalOperationsNodeList(ListNode x) {
		if (x == null) {
			return;
		}
		generateRelationalOperationsNode((RelationalOperationsNode) x.getNode());
		output += ";\r\n";
		generateRelationalOperationsNodeList(x.getNext());
	}

	public void generateRelationalOperationsNode(RelationalOperationsNode x) {
		if (x == null)
			return;
		if (x.getNode() instanceof QueryNode)
			generateQueryNode((QueryNode) x.getNode());
	}

	public void generateQueryNode(QueryNode x) {
		if (x == null)
			return;
		if (x.getNode() instanceof ReadyOnlyOperationsNode) {
			globalScope++;
			generateReadyOnlyOperationsNode((ReadyOnlyOperationsNode) x.getNode(), true, globalScope);
		}
	}

	private void generateReadyOnlyOperationsNode(ReadyOnlyOperationsNode x, boolean first, int scope) {
		if (x == null)
			return;
		output += "SELECT DISTINCT ";
		if (x.getNode() instanceof UnitaryOperationsNode)
			generateUnitaryOperationsNode((UnitaryOperationsNode) x.getNode(), first, scope);
	}

	private void generateUnitaryOperationsNode(UnitaryOperationsNode x, boolean first, int scope) {
		if (x == null)
			return;
		if (x.getUnitaryOperationsChildrenNode() instanceof ProjectNode)
			generateProjectNode((ProjectNode) x.getUnitaryOperationsChildrenNode());
		else if (x.getUnitaryOperationsChildrenNode() instanceof RenameNode)
			generateRenameNode((RenameNode) x.getUnitaryOperationsChildrenNode(), scope);
		else
			output += "*";

		output += " FROM ";

		if (x.getRelationNode() != null)
			output += x.getRelationNode().getPosition().image;
		else {
			globalScope++;
			output += "(";
			generateReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode(), false, scope);
			output += ")";
		}

		if (x.getUnitaryOperationsChildrenNode() instanceof SelectNode)
			generateSelectNode((SelectNode) x.getUnitaryOperationsChildrenNode());
	}

	private void generateProjectNode(ProjectNode x) {
		if (x == null)
			return;
		generateAttributeNodeList(x.getProjectNodeList(), true);
	}

	private void generateAttributeNodeList(ListNode x, boolean first) {
		if (x == null)
			return;
		output += (first ? "" : ", ") + x.getNode().getPosition().image;
		generateAttributeNodeList(x.getNext(), false);
	}

	private void generateRenameNode(RenameNode x, int scope) {
		if (x == null)
			return;
		generateRenameSetNodeList(x.getRenameSetNodeList(), true, scope);
	}

	private void generateRenameSetNodeList(ListNode x, boolean first, int scope) {
		if (x == null)
			return;
		generateRenameSetNode((RenameSetNode) x.getNode(), first);
		if (x.getNext() == null) {
			generateRemaingAttributes(scope);
		} else {
			generateRenameSetNodeList(x.getNext(), false, scope);
		}
	}

	private void generateRemaingAttributes(int scope) {
		for (String attribute : schema.getRelation("temporaryRelation" + (scope + 1)).getAttributeNames()) {
			if (!attributes.contains(attribute))
				output += ", " + attribute;
		}
	}

	private void generateRenameSetNode(RenameSetNode x, boolean first) {
		output += (first ? "" : ", ") + x.getToRenameAttributeNode().getPosition().image + " AS "
				+ x.getRenamedAttributeNode().getPosition().image;
		attributes.add(x.getToRenameAttributeNode().getPosition().image);
	}

	private void generateSelectNode(SelectNode x) {
		if (x == null)
			return;
		output += " WHERE ";
		generateLogicalSentenceNode(x.getLogicalSentenceNode());
	}

	private void generateLogicalSentenceNode(LogicalSentenceNode x) {
		if (x == null)
			return;
		if (x.getConditionalSentenceNode() == null)
			generateLogicalOperatorNode(x.getLogicalOperatorNode());
		else
			generateConditionalSentenceNode(x.getConditionalSentenceNode());
	}

	private void generateLogicalOperatorNode(LogicalOperatorNode x) {
		if (x == null)
			return;
		generateConditionalSentenceNode(x.getConditionalSentenceNode1());
		output += " " + convertLogicalOperator(x.getPosition().image) + " ";
		if (x.getNextLogicalOperatorNode() == null)
			generateConditionalSentenceNode(x.getConditionalSentenceNode2());
		else
			generateLogicalOperatorNode(x.getNextLogicalOperatorNode());
	}

	private String convertLogicalOperator(String x) {
		switch (x) {
		case "^":
			return "AND";
		case "v":
			return "OR";
		case "_v_":
			return "XOR";
		default:
			return "";
		}
	}

	private void generateConditionalSentenceNode(ConditionalSentenceNode x) {
		if (x == null)
			return;
		generateComparisonSentenceNode(x.getComparisonSentenceNode());
		generateIfNodeList(x.getIfListNode());
	}

	private void generateIfNodeList(ListNode x) {
		if (x == null)
			return;
		generateIfNode((IfNode) x.getNode());
		generateIfNodeList(x.getNext());
	}

	private void generateIfNode(IfNode x) {
		if (x == null)
			return;
		// Não sei ainda como fica no SQL
	}

	private void generateComparisonSentenceNode(ComparisonSentenceNode x) {
		if (x == null)
			return;
		if (x.getInstanceofSentenceNode() == null)
			generateComparisonOperatorNode(x.getComparisonOperatorNode());
		else
			generateInstanceofSentenceNode(x.getInstanceofSentenceNode());
	}

	private void generateComparisonOperatorNode(ComparisonOperatorNode x) {
		if (x == null)
			return;
		generateInstanceofSentenceNode(x.getInstanceofSentenceNode1());
		output += " " + x.getPosition().image + " ";
		if (x.getNextComparisonOperatorNode() == null)
			generateInstanceofSentenceNode(x.getInstanceofSentenceNode2());
		else
			generateComparisonOperatorNode(x.getNextComparisonOperatorNode());
	}

	private void generateInstanceofSentenceNode(InstanceofSentenceNode x) {
		if (x == null)
			return;
		generateAdditionSentenceNode(x.getAdditionSentenceNode());
		if (x.getType() != null) {
			output += " instanceof " + x.getType().image;
		}
	}

	private void generateAdditionSentenceNode(AdditionSentenceNode x) {
		if (x == null)
			return;
		if (x.getMultiplicationSentenceNode() == null)
			generateAdditionOperatorNode(x.getAdditionOperatorNode());
		else
			generateMultiplicationSentenceNode(x.getMultiplicationSentenceNode());
	}

	private void generateAdditionOperatorNode(AdditionOperatorNode x) {
		if (x == null)
			return;
		generateMultiplicationSentenceNode(x.getMultiplicationSentenceNode1());
		output += " " + x.getPosition().image + " ";
		if (x.getNextAdditionOperatorNode() == null)
			generateMultiplicationSentenceNode(x.getMultiplicationSentenceNode2());
		else
			generateAdditionOperatorNode(x.getNextAdditionOperatorNode());
	}

	private void generateMultiplicationSentenceNode(MultiplicationSentenceNode x) {
		if (x == null)
			return;
		if (x.getFactorNode() == null)
			generateMultiplicationOperatorNode(x.getMultiplicationOperatorNode());
		else
			generateFactorNode(x.getFactorNode());
	}

	private void generateMultiplicationOperatorNode(MultiplicationOperatorNode x) {
		if (x == null)
			return;
		generateFactorNode(x.getFactorNode1());
		output += " " + x.getPosition().image + " ";
		if (x.getNextMultiplicationOperatorNode() == null)
			generateFactorNode(x.getFactorNode2());
		else
			generateMultiplicationOperatorNode(x.getNextMultiplicationOperatorNode());
	}

	private void generateFactorNode(FactorNode x) {
		if (x == null)
			return;
		if(x.getConditionalSentenceNode()==null)
			output += (x.getNot() == null ? "" : "!") + x.getPosition().image;
		else{
			output+= (x.getNot() == null ? "" : "! ") + "( ";
			generateConditionalSentenceNode(x.getConditionalSentenceNode());
			output+= " )";
		}
	}
}
