package br.edu.ifsp.codeGeneration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import br.edu.ifsp.symbolTable.Relation;
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
			generateReadyOnlyOperationsNode((ReadyOnlyOperationsNode) x.getNode(), true);
		}
	}

	private void generateReadyOnlyOperationsNode(ReadyOnlyOperationsNode x, boolean first) {
		if (x == null)
			return;
		output += "SELECT DISTINCT ";
		if (x.getNode() instanceof UnitaryOperationsNode)
			generateUnitaryOperationsNode((UnitaryOperationsNode) x.getNode(), first, globalScope);
		if (x.getNode() instanceof BinaryOperationsNode)
			generateBinaryOperationsNode((BinaryOperationsNode) x.getNode(), first, globalScope);
	}

	private void generateBinaryOperationsNode(BinaryOperationsNode x, boolean first, int scope) {
		if (x == null)
			return;
		output += "* FROM ";

		int binaryScopes[] = new int[2];
		String binaryRelation[] = new String[2];

		globalScope++;
		binaryScopes[0] = globalScope;
		int outputLength = output.length();
		generateFirstBinaryRelation(x.getBinarySetNode(), first);
		binaryRelation[0] = output.substring(outputLength, output.length());
		globalScope++;
		binaryScopes[1] = globalScope;

		if (x.getBinaryOperationsNodeChildren() instanceof DivisionNode) {
			outputLength = output.length();
			generateSecondBinaryRelation(x.getBinarySetNode(), first);
			binaryRelation[1] = output.substring(outputLength, output.length());
			// Clear the line to write the division code
			output = output.substring(0, output.lastIndexOf("\n"));
			generateBinaryDivisionNode(x, scope, binaryScopes, binaryRelation, first);
			return;
		}

		if (x.getBinaryOperationsNodeChildren() instanceof UnionNode)
			output += " UNION ";
		if (x.getBinaryOperationsNodeChildren() instanceof IntersectionNode)
			output += " INTERSECT ";
		if (x.getBinaryOperationsNodeChildren() instanceof DifferenceNode)
			output += " EXCEPT ";
		if (x.getBinaryOperationsNodeChildren() instanceof JoinNode) {
			generateJoinNode((JoinNode) x.getBinaryOperationsNodeChildren());
			generateSecondBinaryRelation(x.getBinarySetNode(), first);
			if (((JoinNode) x.getBinaryOperationsNodeChildren()).getLogicalSentenceNode() != null)
				output += " ON ";
			generateLogicalSentenceNode(((JoinNode) x.getBinaryOperationsNodeChildren()).getLogicalSentenceNode());
		} else if (x.getBinaryOperationsNodeChildren() instanceof CrossJoinNode) {
			output += " CROSS JOIN ";
			generateSecondBinaryRelation(x.getBinarySetNode(), first);
		} else {
			output += "SELECT DINSTINC * FROM ";
			generateSecondBinaryRelation(x.getBinarySetNode(), first);
		}
	}

	private void generateBinaryDivisionNode(BinaryOperationsNode x, int scope, int binaryScopes[],
			String binaryRelation[], boolean first) {
		if (x == null)
			return;
		Relation relation1 = schema.getRelation("temporaryRelation" + binaryScopes[0]);
		Relation relation2 = schema.getRelation("temporaryRelation" + binaryScopes[1]);
		Set<String> intersection = new HashSet<String>();
		Set<String> exception = new HashSet<String>();
		
		//schema.printTable();
		
		for (String attribute : relation1.getAttributeNames()) {
			if (relation2.hasAttribute(attribute)) {
				intersection.add(attribute);// direita
			} else {
				exception.add(attribute);// esquerda
			}
		}

		output += "SELECT DISTINCT ";
		for (String attribute : exception) {
			output += attribute + ", ";
		}
		
		output = output.substring(0, output.length() - 2) + " FROM " + binaryRelation[0] + " AS temporaryRelation1"
				+ " WHERE ( SELECT COUNT(DISTINCT ";

		System.out.println("Intersection"+intersection.size());
		for (String attribute : intersection) {
			output += attribute + ", ";
		}

		output = output.substring(0, output.length() - 2) + ") FROM " + binaryRelation[1]
				+ ") = ( SELECT COUNT(*) FROM " + binaryRelation[0] + " AS temporaryRelation2 WHERE ";
		
		for (String attribute : exception) {
			output += "temporaryRelation1." + attribute + " = temporaryRelation2." + attribute + " AND ";
		}
		
		output = output.substring(0, output.length()-5) + ")";
	}

	private void generateJoinNode(JoinNode x) {
		if (x == null)
			return;
		if (x.getLogicalSentenceNode() == null)
			output += " NATURAL JOIN ";
		else
			output += " INNER JOIN ";
	}

	private void generateFirstBinaryRelation(BinarySetNode x, boolean first) {
		if (x == null)
			return;
		if (x.getReadyOnlyOperationsNode1() != null) {
			output += "(";
			generateReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode1(), first);
			output += ")";
		} else
			output += x.getRelationNode1().getImage();
	}

	private void generateSecondBinaryRelation(BinarySetNode x, boolean first) {
		if (x == null)
			return;
		if (x.getReadyOnlyOperationsNode2() != null) {
			output += "(";
			generateReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode2(), first);
			output += ")";
		} else
			output += x.getRelationNode2().getImage();
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

		if (x.getRelationNode() != null) {
			if (first)
				output += x.getRelationNode().getPosition().image;
			else {
				output = output.substring(0, output.length() - 23) + x.getRelationNode().getPosition().image;
			}
		} else {
			globalScope++;
			output += "(";
			generateReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode(), false);
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
		if (x.getConditionalSentenceNode() == null)
			output += (x.getNot() == null ? "" : "!") + x.getPosition().image;
		else {
			output += (x.getNot() == null ? "" : "! ") + "( ";
			generateConditionalSentenceNode(x.getConditionalSentenceNode());
			output += " )";
		}
	}
}
