package br.edu.ifsp.codeGeneration;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.edu.ifsp.symbolTable.Relation;
import br.edu.ifsp.symbolTable.SymbolTable;
import br.edu.ifsp.syntacticTree.*;

/**
 * Class responsible for translating the code
 * 
 * @author Dérick Welman
 */
public class CodeGenerator {
	private PrintWriter pw;
	private String output = "";
	private ArrayList<String> attributes = new ArrayList<String>();
	private SymbolTable schema;
	private int globalScope;

	/**
	 * Constructor used to initialize the generator
	 */
	public CodeGenerator(SymbolTable schema, String version) throws IOException {
		this.schema = schema;
		globalScope = 0;
		output = "";
	}

	/**
	 * Method used to generate the SQL from analysis
	 * 
	 * @param root
	 * @return String output
	 */
	public String generate(ListNode root) {
		generateRelationalOperationsNodeList(root);
		return output;
	}

	public void printOutput() {
		System.out.println(output);
	}

	public void exportOutput() {
		pw.println(output);
		pw.close();
	}

	/*
	 * *********************************************************************
	 * Generate the output SQL from syntactic tree
	 **********************************************************************/

	/**
	 * Method that analyzes a list of relational operations
	 * 
	 * @param ListNode
	 *            x - List of Relational Operations
	 */
	public void generateRelationalOperationsNodeList(ListNode x) {
		if (x == null) {
			return;
		}
		generateRelationalOperationsNode((RelationalOperationsNode) x.getNode());
		output += ";\r\n";
		generateRelationalOperationsNodeList(x.getNext());
	}

	/**
	 * Method that analyzes a RelationalOperationsNode
	 * 
	 * @param RelationalOperationsNode
	 *            x - Node that represents a relational operation
	 */
	public void generateRelationalOperationsNode(RelationalOperationsNode x) {
		if (x == null)
			return;
		if (x.getNode() instanceof QueryNode)
			generateQueryNode((QueryNode) x.getNode());
	}

	/**
	 * Method that analyzes a QueryNode
	 * 
	 * @param QueryNode
	 *            x - Node that represents a query
	 */
	public void generateQueryNode(QueryNode x) {
		if (x == null)
			return;
		if (x.getNode() instanceof ReadOnlyOperationsNode) {
			globalScope++;
			generateReadOnlyOperationsNode((ReadOnlyOperationsNode) x.getNode(), true);
		}
	}

	/**
	 * Method that analyzes a ReadOnlyOperationsNode
	 * 
	 * @param ReadOnlyOperationsNode
	 *            x - Node that represents a read only operation
	 * @param Boolean
	 *            first - verifies if it is the first relation of the query
	 */
	private void generateReadOnlyOperationsNode(ReadOnlyOperationsNode x, boolean first) {
		if (x == null)
			return;
		output += "SELECT DISTINCT ";
		if (x.getNode() instanceof UnitaryOperationsNode)
			generateUnitaryOperationsNode((UnitaryOperationsNode) x.getNode(), first, globalScope);
		if (x.getNode() instanceof BinaryOperationsNode)
			generateBinaryOperationsNode((BinaryOperationsNode) x.getNode(), first, globalScope);
	}

	/**
	 * Method that analyzes a BinaryOperatioinsNode
	 * 
	 * @param BinaryOperationsNode
	 *            x - Node that represents a binary operation
	 * @param Boolean
	 *            first - Verifies if it is the first relation of the query
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
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
		output = output.substring(0, outputLength);
		globalScope++;
		binaryScopes[1] = globalScope;

		if (x.getBinaryOperationsNodeChildren() instanceof DivisionNode) {
			outputLength = output.length();
			generateSecondBinaryRelation(x.getBinarySetNode(), first);
			binaryRelation[1] = output.substring(outputLength, output.length());
			output = output.substring(0, outputLength);
			generateBinaryDivisionNode(x, scope, binaryScopes, binaryRelation, first);
			return;
		} else {
			output += binaryRelation[0] + " AS temporaryRelation" + binaryScopes[0];
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
			output += "SELECT DISTINCT * FROM ";
			generateSecondBinaryRelation(x.getBinarySetNode(), first);
		}
	}

	/**
	 * Method that analyzes a binary division
	 * 
	 * @param BinaryOperationsNode
	 *            x - Node that represents a binary operations
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @param int[]
	 *            binaryScopes - Indicates the scope of two operations that this
	 *            operations use
	 * @param String[]
	 *            binaryRelation - Contains the translate of binary relation
	 *            analysis
	 * @param boolean
	 *            first - Verifies if it is the first relation of the query
	 */
	private void generateBinaryDivisionNode(BinaryOperationsNode x, int scope, int binaryScopes[],
			String binaryRelation[], boolean first) {
		if (x == null)
			return;
		Relation relation1 = schema.getRelation("temporaryRelation" + binaryScopes[0]);
		Relation relation2 = schema.getRelation("temporaryRelation" + binaryScopes[1]);
		Set<String> intersection = new HashSet<String>();
		Set<String> exception = new HashSet<String>();

		for (String attribute : relation1.getAttributeNames()) {
			if (relation2.hasAttribute(attribute)) {
				intersection.add(attribute);// right
			} else {
				exception.add(attribute);// left
			}
		}

		output += "(SELECT DISTINCT ";
		for (String attribute : exception) {
			output += attribute + ", ";
		}

		int subdiv = 0;
		String originRelation0 = binaryRelation[0];
		if (binaryRelation[0].contains("temporaryRelation")) {
			binaryRelation[0] = originRelation0.substring(0, originRelation0.length() - 1) + "_subdiv" + subdiv++ + ")";
		}

		output = output.substring(0, output.length() - 2) + " FROM " + binaryRelation[0] + " AS temporaryRelation"
				+ scope + "_div1" + " WHERE ( SELECT COUNT(DISTINCT ";

		if (binaryRelation[0].contains("temporaryRelation")) {
			binaryRelation[0] = originRelation0.substring(0, originRelation0.length() - 1) + "_subdiv" + subdiv++ + ")";
		}

		for (String attribute : intersection) {
			output += attribute + ", ";
		}

		output = output.substring(0, output.length() - 2) + ") FROM " + binaryRelation[1] + ") = ( SELECT COUNT(*) FROM " + binaryRelation[0] + " AS temporaryRelation" + scope
				+ "_div2 WHERE ";

		for (String attribute : exception) {
			output += "temporaryRelation" + scope + "_div1." + attribute + " = temporaryRelation" + scope + "_div2."
					+ attribute + " AND ";
		}

		output = output.substring(0, output.length() - 5) + ")) AS temporaryRelation" + scope;
	}

	/**
	 * Method that analyzes a JoinNode
	 * 
	 * @param JoinNode
	 *            x - Node that represents a join operation
	 */
	private void generateJoinNode(JoinNode x) {
		if (x == null)
			return;
		if (x.getLogicalSentenceNode() == null)
			output += " NATURAL JOIN ";
		else
			output += " INNER JOIN ";
	}

	/**
	 * Method that analyzes the first relation of a BinarySetNode
	 * 
	 * @param BinarySetNode
	 *            x - Node that represents a binary set of relations
	 * @param boolean
	 *            first - Verifies if it is the first relation of the query
	 */
	private void generateFirstBinaryRelation(BinarySetNode x, boolean first) {
		if (x == null)
			return;
		if (x.getReadOnlyOperationsNode1() != null) {
			output += "(";
			generateReadOnlyOperationsNode(x.getReadOnlyOperationsNode1(), first);
			output += ")";
		} else
			output += x.getRelationNode1().getImage();
	}

	/**
	 * Method that analyzes the second relation of a BinarySetNode
	 * 
	 * @param BinarySetNode
	 *            x - Node that represents a binary set of relations
	 * @param boolean
	 *            first - Verifies if it is the first relation of the query
	 */
	private void generateSecondBinaryRelation(BinarySetNode x, boolean first) {
		if (x == null)
			return;
		if (x.getReadOnlyOperationsNode2() != null) {
			output += "(";
			generateReadOnlyOperationsNode(x.getReadOnlyOperationsNode2(), first);
			output += ") AS temporaryRelation" + globalScope;
		} else
			output += x.getRelationNode2().getImage();
	}

	/**
	 * Method that analyzes a UnitaryOperationsNode
	 * 
	 * @param UnitaryOperationsNode
	 *            x - Node that represents a unitary operation
	 * @param boolean
	 *            first - Verifies if it is the first relation of the query
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
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
			if (x.getUnitaryOperationsChildrenNode() instanceof TransitiveCloseNode)
				generateTransitiveCloseNode((TransitiveCloseNode) x.getUnitaryOperationsChildrenNode(), globalScope,
						x.getRelationNode().getPosition().image);
			else if (first)
				output += x.getRelationNode().getPosition().image;
			else {
				output = output.substring(0, output.length() - 23) + x.getRelationNode().getPosition().image;
			}

		} else {
			globalScope++;
			output += "(";

			int outputLength = output.length();
			boolean onlyRelation = false;
			generateReadOnlyOperationsNode(x.getReadOnlyOperationsNode(), false);
			if (x.getUnitaryOperationsChildrenNode() instanceof TransitiveCloseNode) {
				String relation = output.substring(outputLength, output.length());
				output = output.substring(0, outputLength);
				generateTransitiveCloseNode((TransitiveCloseNode) x.getUnitaryOperationsChildrenNode(), globalScope,
						relation);
			}
			if (!output.substring(outputLength).startsWith("SELECT")) {
				String relation = output.substring(outputLength, output.length());
				output = output.substring(0, outputLength - 1);
				output += relation;
				onlyRelation = true;
			}
			output += (onlyRelation ? "" : ")") + (x.getUnitaryOperationsChildrenNode() instanceof TransitiveCloseNode
					? "" : " AS temporaryRelation" + (scope + 1));

		}

		if (x.getUnitaryOperationsChildrenNode() instanceof SelectNode)
			generateSelectNode((SelectNode) x.getUnitaryOperationsChildrenNode());
	}

	/**
	 * Method that analyzes a TransitiveCloseNode
	 * 
	 * @param TransitiveCloseNode
	 *            x - Node that represents a transitive closure operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @param String
	 *            scopeRelation - Contains the relation previous analyzed
	 */
	private void generateTransitiveCloseNode(TransitiveCloseNode x, int scope, String scopeRelation) {
		if (x == null)
			return;
		int localScope = 0;
		Relation relation = schema.getRelation("temporaryRelation" + scope);
		String attributes[] = new String[relation.getAttributeNames().size()];
		attributes = relation.getAttributeNames().toArray(attributes);
		String leftAttribute = attributes[0];
		String rightAttribute = attributes[1];
		String alias = "";

		if (scopeRelation.startsWith("SELECT")) {
			scopeRelation = "(" + scopeRelation + ")";
			alias = "AS temporaryRelation" + (scope + 1) + "_" + localScope;
		}
		output += "(SELECT DISTINCT * FROM " + scopeRelation + alias + " UNION SELECT temporaryRelation" + scope + "_1."
				+ rightAttribute + ", temporaryRelation" + scope + "_2." + leftAttribute + " FROM " + scopeRelation
				+ " AS temporaryRelation" + scope + "_1 INNER JOIN " + scopeRelation + " AS temporaryRelation" + scope
				+ "_2 ON temporaryRelation" + scope + "_1." + leftAttribute + " = temporaryRelation" + scope + "_2."
				+ rightAttribute + " WHERE temporaryRelation" + scope + "_2." + leftAttribute
				+ " IS NOT NULL) AS temporaryRelation" + scope;
	}

	/**
	 * Method that analyzes a ProjectNode
	 * 
	 * @param ProjectNode
	 *            x - Node that represents a projection operation
	 */
	private void generateProjectNode(ProjectNode x) {
		if (x == null)
			return;
		generateAttributeNodeList(x.getProjectNodeList(), true);
	}

	/**
	 * Method that analyzes a list of projection attributes
	 * 
	 * @param ListNode
	 *            x - A list of AttributeNodeList
	 * @param boolean
	 *            first - Verifies if it is the first attribute of the project
	 *            operation
	 */
	private void generateAttributeNodeList(ListNode x, boolean first) {
		if (x == null)
			return;
		output += (first ? "" : ", ") + x.getNode().getPosition().image;
		generateAttributeNodeList(x.getNext(), false);
	}

	/**
	 * Method that analyzes a RenameNode
	 * 
	 * @param RenameNode
	 *            x - Node that represents a rename operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
	private void generateRenameNode(RenameNode x, int scope) {
		if (x == null)
			return;
		generateRenameSetNodeList(x.getRenameSetNodeList(), true, scope);
	}

	/**
	 * Method that analyzes a list of RenameSetNode
	 * 
	 * @param ListNode
	 *            x - Node that represents a list of rename attributes set
	 * @param boolean
	 *            first - Verifies if it is the first attribute of the rename
	 *            operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
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

	/**
	 * Method that adds the rest of attributes not renamed for translation
	 * 
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
	private void generateRemaingAttributes(int scope) {
		for (String attribute : schema.getRelation("temporaryRelation" + (scope + 1)).getAttributeNames()) {
			if (!attributes.contains(attribute))
				output += ", " + attribute;
		}
	}

	/**
	 * Method that analyzes a RenameSetNode
	 * 
	 * @param RenameSetNode
	 *            x - Node that represents a set of attributes to be renamed
	 * @param boolean
	 *            first - Verifies if it is the first attribute of the rename
	 *            operation
	 */
	private void generateRenameSetNode(RenameSetNode x, boolean first) {
		output += (first ? "" : ", ") + x.getToRenameAttributeNode().getPosition().image + " AS "
				+ x.getRenamedAttributeNode().getPosition().image;
		attributes.add(x.getToRenameAttributeNode().getPosition().image);
	}

	/**
	 * Method that analyzes a SelectNode
	 * 
	 * @param SelectNode
	 *            x - Node that represents a selection operation
	 */
	private void generateSelectNode(SelectNode x) {
		if (x == null)
			return;
		output += " WHERE ";
		generateLogicalSentenceNode(x.getLogicalSentenceNode());
	}

	/**
	 * Method that analyzes a LogicalSentenceNode
	 * 
	 * @param LogicalSentenceNode
	 *            x - Node that represents a logical sentence
	 */
	private void generateLogicalSentenceNode(LogicalSentenceNode x) {
		if (x == null)
			return;
		if (x.getConditionalSentenceNode() == null)
			generateLogicalOperatorNode(x.getLogicalOperatorNode());
		else
			generateConditionalSentenceNode(x.getConditionalSentenceNode());
	}

	/**
	 * Method that analyzes a LogicalOperatorNode
	 * 
	 * @param LogicalOperatorNode
	 *            x - Node that represents a logical operator
	 */
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

	/**
	 * Method that converts the RQL to SQL logical operators
	 * 
	 * @param String
	 *            x - String that contains the logical operator
	 */
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

	/**
	 * Method that analyzes a ConditionalSentenceNode
	 * 
	 * @param ConditionalSentenceNode
	 *            x - Node that represents a conditional sentence
	 */
	private void generateConditionalSentenceNode(ConditionalSentenceNode x) {
		if (x == null)
			return;
		generateComparisonSentenceNode(x.getComparisonSentenceNode());
		generateIfNodeList(x.getIfListNode());
	}

	/**
	 * Method that analyzes a list of IfNodes
	 * 
	 * @param ListNode
	 *            x - Node that represents a list of IfNodes
	 */
	private void generateIfNodeList(ListNode x) {
		if (x == null)
			return;
		generateIfNode((IfNode) x.getNode());
		generateIfNodeList(x.getNext());
	}

	/**
	 * Method that analyzes a IfNode NOT IMPLEMENTED YET
	 * 
	 * @param IfNode
	 *            x - Node that represents a conditional test (if)
	 */
	private void generateIfNode(IfNode x) {
		if (x == null)
			return;
		// NOT IMPLEMENTED YET
	}

	/**
	 * Method that analyzes a ComparisonSentenceNode
	 * 
	 * @param ComparisonSentenceNode
	 *            x - Node that represents a comparison sentence
	 */
	private void generateComparisonSentenceNode(ComparisonSentenceNode x) {
		if (x == null)
			return;
		if (x.getInstanceofSentenceNode() == null)
			generateComparisonOperatorNode(x.getComparisonOperatorNode());
		else
			generateInstanceofSentenceNode(x.getInstanceofSentenceNode());
	}

	/**
	 * Method that analyzes a ComparisonOperatorNode
	 * 
	 * @param ComparisonOperatorNode
	 *            x - Node that represents a comparison operator
	 */
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

	/**
	 * Method that analyzes a InstanceofSentenceNode NOT IMPLEMENTED YET
	 * 
	 * @param InstanceofSentenceNode
	 *            x - Node that represents a instanceof sentence
	 */
	private void generateInstanceofSentenceNode(InstanceofSentenceNode x) {
		if (x == null)
			return;
		generateAdditionSentenceNode(x.getAdditionSentenceNode());
	}

	/**
	 * Method that analyzes a AdditionSentenceNode
	 * 
	 * @param AdditionSentenceNode
	 *            x - Node that represents a addition sentence
	 */
	private void generateAdditionSentenceNode(AdditionSentenceNode x) {
		if (x == null)
			return;
		if (x.getMultiplicationSentenceNode() == null)
			generateAdditionOperatorNode(x.getAdditionOperatorNode());
		else
			generateMultiplicationSentenceNode(x.getMultiplicationSentenceNode());
	}

	/**
	 * Method that analyzes a AdditionOperatorNode
	 * 
	 * @param AdditionOperator
	 *            x - Node that represents a addition operator
	 */
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

	/**
	 * Method that analyzes a MultiplicationSentenceNode
	 * 
	 * @param MultiplicationSentenceNode
	 *            x - Node that represents a multiplication sentence
	 */
	private void generateMultiplicationSentenceNode(MultiplicationSentenceNode x) {
		if (x == null)
			return;
		if (x.getFactorNode() == null)
			generateMultiplicationOperatorNode(x.getMultiplicationOperatorNode());
		else
			generateFactorNode(x.getFactorNode());
	}

	/**
	 * Method that analyzes a MultiplicationOperatorNode
	 * 
	 * @param ultiplicationOperatorNode
	 *            x - Node that represents a multiplication operator
	 */
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

	/**
	 * Method that analyzes a FactorNode
	 * 
	 * @param FactorNode
	 *            x - Node that represents a RQL factor
	 */
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
