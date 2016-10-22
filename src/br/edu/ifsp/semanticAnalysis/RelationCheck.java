package br.edu.ifsp.semanticAnalysis;

import java.util.HashSet;
import java.util.Set;

import br.edu.ifsp.parser.RelationalQueryLanguageConstants;
import br.edu.ifsp.symbolTable.*;
import br.edu.ifsp.syntacticTree.*;

/**
 * Class responsible for semantic analysis
 * 
 * @author Dérick Welman
 */
public class RelationCheck implements RelationalQueryLanguageConstants {
	SymbolTable schema;
	int semanticErrors;
	int globalScope;
	String errorsDescription = "";
	Set<Integer> numberConstants = new HashSet<Integer>();

	/** Constructor that initializes the variables */
	public RelationCheck(SymbolTable st) {
		schema = st;
		semanticErrors = 0;
		globalScope = 0;

		// Fill the set of number constants in RQL
		numberConstants.add(RelationalQueryLanguageConstants.INTEGER);
		numberConstants.add(RelationalQueryLanguageConstants.DECIMAL);
		numberConstants.add(RelationalQueryLanguageConstants.BIN);
		numberConstants.add(RelationalQueryLanguageConstants.HEX);
		numberConstants.add(RelationalQueryLanguageConstants.OCT);
	}

	/** Method that resets the semantic error count */
	public void clearSemanticErrors() {
		semanticErrors = 0;
		errorsDescription = "";
	}

	/**
	 * Method that returns the semantic error count
	 * @return int - Semantic errors count
	 */
	public int getSemanticErrors() {
		return semanticErrors;
	}
	
	/**
	 * Method that returns the semantic errors description
	 * @return String - Semantic errors description
	 */
	public String getSemanticErrorsDescription() {
		return errorsDescription;
	}

	/**
	 * Method that adds a semantic error and prints it
	 * 
	 * @param String
	 *            message - Semantic error to be printed
	 */
	private void throwSemanticError(String message) {
		semanticErrors++;
		errorsDescription += message + "\n";
		//System.out.println(message);
	}

	/**
	 * Method that starts the semantic analysis
	 * 
	 * @param ListNode
	 *            root - The first node of syntactic tree
	 * @return int - The number of semantic errors
	 */
	public int semanticAnalysis(ListNode root) {
		if (root == null) {
			System.out.println("Nothing to be analysed");
			return 0;
		}
		System.out.println("Starting the semantic analysis");
		relationalOperationsNodeListCheck(root);
		System.out.println(getSemanticErrorsDescription());
		return semanticErrors;
	}

	/**
	 * Method that analyzes the RelationalOperationNode list
	 * 
	 * @param ListNode
	 *            x - ListNode that contains a RelationalQueryNode
	 */
	private void relationalOperationsNodeListCheck(ListNode x) {
		if (x == null) {
			return;
		}
		relationalOperationsNodeCheck((RelationalOperationsNode) x.getNode());
		relationalOperationsNodeListCheck(x.getNext());
	}

	/**
	 * Method that analyzes the RelationalOperationNode
	 * 
	 * @param RelationalOperationsNode
	 *            x - Node that represents a relational operation
	 */
	private void relationalOperationsNodeCheck(RelationalOperationsNode x) {
		if (x == null)
			return;
		if (x.getNode() instanceof QueryNode)
			queryNodeCheck((QueryNode) x.getNode());
	}

	/**
	 * Method that analyzes the QueryNode
	 * 
	 * @param QueryNode
	 *            x - Node that represents a query
	 */
	private void queryNodeCheck(QueryNode x) {
		if (x == null)
			return;
		if (x.getNode() instanceof ReadOnlyOperationsNode) {
			readyOnlyOperationsNodeCheck((ReadOnlyOperationsNode) x.getNode());
		}
	}

	/**
	 * Method that analyzes the ReadyOnlyOperationsNode
	 * 
	 * @param ReadOnlyOperationsNode
	 *            x - Node that represents a read only operation
	 */
	private void readyOnlyOperationsNodeCheck(ReadOnlyOperationsNode x) {
		if (x == null)
			return;
		globalScope++;
		schema.addRelation("temporaryRelation" + globalScope);
		if (x.getNode() instanceof UnitaryOperationsNode) {
			unitaryOperationsNodeCheck((UnitaryOperationsNode) x.getNode());
		} else {
			binaryOperationsNodeCheck((BinaryOperationsNode) x.getNode());
		}
	}

	/**
	 * Method that analyzes the BinaryOperationsNode
	 * 
	 * @param BinaryOperationsNode
	 *            x - Node that represents a binary operation
	 */
	private void binaryOperationsNodeCheck(BinaryOperationsNode x) {
		if (x == null)
			return;
		int scope = globalScope;
		int binaryScopes[] = binarySetNodeCheck(x.getBinarySetNode());
		if (x.getBinaryOperationsNodeChildren() instanceof JoinNode)
			joinNodeCheck((JoinNode) x.getBinaryOperationsNodeChildren(), scope, binaryScopes);
		if (x.getBinaryOperationsNodeChildren() instanceof CrossJoinNode)
			crossJoinNodeCheck((CrossJoinNode) x.getBinaryOperationsNodeChildren(), scope, binaryScopes);
		if (x.getBinaryOperationsNodeChildren() instanceof UnionNode)
			unionNodeCheck((UnionNode) x.getBinaryOperationsNodeChildren(), scope, binaryScopes);
		if (x.getBinaryOperationsNodeChildren() instanceof IntersectionNode)
			intersectionNodeCheck((IntersectionNode) x.getBinaryOperationsNodeChildren(), scope, binaryScopes);
		if (x.getBinaryOperationsNodeChildren() instanceof DifferenceNode)
			differenceNodeCheck((DifferenceNode) x.getBinaryOperationsNodeChildren(), scope, binaryScopes);
		if (x.getBinaryOperationsNodeChildren() instanceof DivisionNode)
			divisionNodeCheck((DivisionNode) x.getBinaryOperationsNodeChildren(), scope, binaryScopes);
	}

	/**
	 * Method that analyzes the BinarySetNode
	 * 
	 * @param BinarySetNode
	 *            x - Node that represents a binary set
	 */
	private int[] binarySetNodeCheck(BinarySetNode x) {
		if (x == null)
			return null;
		int binaryScopes[] = new int[2];

		binaryScopes[0] = globalScope + 1;
		if (x.getReadyOnlyOperationsNode1() != null) {
			readyOnlyOperationsNodeCheck(x.getReadyOnlyOperationsNode1());
		} else {
			globalScope++;
			schema.addRelation("temporaryRelation" + globalScope);
			relationNodeCheck(x.getRelationNode1());
		}

		binaryScopes[1] = globalScope + 1;
		if (x.getReadyOnlyOperationsNode2() != null) {
			readyOnlyOperationsNodeCheck(x.getReadyOnlyOperationsNode2());
		} else {
			globalScope++;
			schema.addRelation("temporaryRelation" + globalScope);
			relationNodeCheck(x.getRelationNode2());
		}
		return binaryScopes;
	}

	/**
	 * Method that analyzes the UnionNode
	 * 
	 * @param UnionNode
	 *            x - Node that represents a union operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @param int[]
	 *            binaryScopes - Indicates the scope of two operations that this
	 *            operations use
	 */
	private void unionNodeCheck(UnionNode x, int scope, int[] binaryScopes) {
		if (x == null)
			return;
		Relation relation1 = schema.getRelation("temporaryRelation" + (binaryScopes[0]));
		Relation relation2 = schema.getRelation("temporaryRelation" + (binaryScopes[1]));
		if (relation1.getNumberOfAttributes() != relation2.getNumberOfAttributes()) {
			throwSemanticError(
					"\tfor the union operation, the relations must have the same number of attributes : At the line "
							+ x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
		} else {
			schema.replaceRelation("temporaryRelation" + scope, relation1);
		}
	}

	/**
	 * Method that analyzes the IntersectionNode
	 * 
	 * @param IntersectionNode
	 *            x - Node that represents a intersection operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @param int[]
	 *            binaryScopes - Indicates the scope of two operations that this
	 *            operations use
	 */
	private void intersectionNodeCheck(IntersectionNode x, int scope, int[] binaryScopes) {
		if (x == null)
			return;
		Relation relation1 = schema.getRelation("temporaryRelation" + (binaryScopes[0]));
		Relation relation2 = schema.getRelation("temporaryRelation" + (binaryScopes[1]));
		if (relation1.getNumberOfAttributes() != relation2.getNumberOfAttributes()) {
			throwSemanticError(
					"\tfor the intersection operation, the relations must have the same number of attributes : At the line "
							+ x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
		} else {
			schema.replaceRelation("temporaryRelation" + scope, relation1);
		}
	}

	/**
	 * Method that analyzes the DifferenceNode
	 * 
	 * @param DifferenceNode
	 *            x - Node that represents a difference operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @param int[]
	 *            binaryScopes - Indicates the scope of two operations that this
	 *            operations use
	 */
	private void differenceNodeCheck(DifferenceNode x, int scope, int[] binaryScopes) {
		if (x == null)
			return;
		Relation relation1 = schema.getRelation("temporaryRelation" + (binaryScopes[0]));
		Relation relation2 = schema.getRelation("temporaryRelation" + (binaryScopes[1]));
		if (relation1.getNumberOfAttributes() != relation2.getNumberOfAttributes()) {
			throwSemanticError(
					"\tfor the difference operation, the relations must have the same number of attributes: At the line "
							+ x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
		} else {
			schema.replaceRelation("temporaryRelation" + scope, relation1);
		}
	}

	/**
	 * Method that analyzes the JoinNode
	 * 
	 * @param JoinNode
	 *            x - Node that represents a join operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @param int[]
	 *            binaryScopes - Indicates the scope of two operations that this
	 *            operations use
	 */
	private void joinNodeCheck(JoinNode x, int scope, int[] binaryScopes) {
		if (x == null)
			return;
		Relation relation1 = schema.getRelation("temporaryRelation" + (binaryScopes[0]));
		Relation relation2 = schema.getRelation("temporaryRelation" + (binaryScopes[1]));
		Relation join = schema.getRelation("temporaryRelation" + scope);
		// Add the attributes of the first relation
		for (String s : relation1.getAttributeNames()) {
			join.addAttribute(s, relation1.getAttribute(s));
		}
		// Add the attributes of the second relation
		for (String s : relation2.getAttributeNames()) {
			join.addAttribute(s, relation2.getAttribute(s));
		}
		if (x.getLogicalSentenceNode() != null)
			logicalSentenceNodeCheck(x.getLogicalSentenceNode(), scope);
		else {
			boolean valid = false;
			for (String a1 : relation1.getAttributeNames()) {
				for (String a2 : relation2.getAttributeNames()) {
					if (a1.equals(a2)) {
						valid = true;
					}
				}
			}
			if (!valid)
				throwSemanticError("Invalid natural join: At the line " + x.getPosition().beginLine + ", column "
						+ x.getPosition().beginColumn);
		}
	}

	/**
	 * Method that analyzes the CrossJoinNode
	 * 
	 * @param CrossJoinNode
	 *            x - Node that represents a cross join operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @param int[]
	 *            binaryScopes - Indicates the scope of two operations that this
	 *            operations use
	 */
	private void crossJoinNodeCheck(CrossJoinNode x, int scope, int[] binaryScopes) {
		if (x == null)
			return;
		Relation relation1 = schema.getRelation("temporaryRelation" + (binaryScopes[0]));
		Relation relation2 = schema.getRelation("temporaryRelation" + (binaryScopes[1]));
		Relation join = schema.getRelation("temporaryRelation" + scope);
		// Add the attributes of the first relation
		for (String s : relation1.getAttributeNames()) {
			join.addAttribute(s, relation1.getAttribute(s));
		}
		// Add the attributes of the second relation
		for (String s : relation2.getAttributeNames()) {
			join.addAttribute(s, relation2.getAttribute(s));
		}
	}

	/**
	 * Method that analyzes the DivisionNode
	 * 
	 * @param DivisionNode
	 *            x - Node that represents a division operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @param int[]
	 *            binaryScopes - Indicates the scope of two operations that this
	 *            operations use
	 */
	private void divisionNodeCheck(DivisionNode x, int scope, int[] binaryScopes) {
		if (x == null)
			return;
		Relation relation1 = schema.getRelation("temporaryRelation" + (binaryScopes[0]));
		Relation relation2 = schema.getRelation("temporaryRelation" + (binaryScopes[1]));
		Relation division = schema.getRelation("temporaryRelation" + scope);

		if (relation1.getNumberOfAttributes() > relation2.getNumberOfAttributes()) {
			Set<String> relation1Attributes = relation1.getAttributeNames();
			Set<String> relation2Attributes = relation2.getAttributeNames();
			for (String attribute : relation2Attributes) {
				if (!relation1.hasAttribute(attribute)) {
					throwSemanticError("\tThe divisor of the operation must be a subset of the dividend: \""
							+ x.getPosition().image + "\" at the line " + x.getPosition().beginLine + ", column "
							+ x.getPosition().beginColumn);
					break;
				}
			}
			for (String attribute : relation1Attributes) {
				if (!relation2Attributes.contains(attribute))
					division.addAttribute(attribute, relation1.getAttribute(attribute));
			}
		} else {
			throwSemanticError("\tThe divisor of the operation must be a subset of the dividend: \""
					+ x.getPosition().image + "\" at the line " + x.getPosition().beginLine + ", column "
					+ x.getPosition().beginColumn);
		}
	}

	/**
	 * Method that analyzes the UnitaryOperationsNode
	 * 
	 * @param UnitaryOperationsNode
	 *            x - Node that represents a unitary operation
	 */
	private void unitaryOperationsNodeCheck(UnitaryOperationsNode x) {
		if (x == null)
			return;
		int scope = globalScope;
		if (x.getRelationNode() != null) {
			relationNodeCheck(x.getRelationNode());
		} else {
			readyOnlyOperationsNodeCheck(x.getReadyOnlyOperationsNode());
		}
		if (x.getUnitaryOperationsChildrenNode() instanceof ProjectNode)
			projectNodeCheck((ProjectNode) x.getUnitaryOperationsChildrenNode(), scope);
		else if (x.getUnitaryOperationsChildrenNode() instanceof RenameNode)
			renameNodeCheck((RenameNode) x.getUnitaryOperationsChildrenNode(), scope);
		else if (x.getUnitaryOperationsChildrenNode() instanceof SelectNode) {
			selectNodeCheck((SelectNode) x.getUnitaryOperationsChildrenNode(), scope);
		} else if (x.getUnitaryOperationsChildrenNode() instanceof TransitiveCloseNode)
			if (x.getRelationNode() == null)
				transitiveCloseNodeCheck((TransitiveCloseNode) x.getUnitaryOperationsChildrenNode(), (scope + 1));
			else
				transitiveCloseNodeCheck((TransitiveCloseNode) x.getUnitaryOperationsChildrenNode(), scope);
	}

	/**
	 * Method that analyzes the TransitiveCloseNode
	 * 
	 * @param TransitiveCloseNode
	 *            x - Node that represents a transitive closure operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
	private void transitiveCloseNodeCheck(TransitiveCloseNode x, int scope) {
		if (x == null)
			return;
		Relation r = schema.getRelation("temporaryRelation" + scope);
		if (r.getNumberOfAttributes() != 2) {
			throwSemanticError("\tTransitive closure requires a binary relation: At the line "
					+ x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
		}
	}

	/**
	 * Method that analyzes the ProjectNode
	 * 
	 * @param ProjectNode
	 *            x - Node that represents a projection operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
	private void projectNodeCheck(ProjectNode x, int scope) {
		if (x == null)
			return;
		attributeNodeListCheck(x.getProjectNodeList(), scope);
	}

	/**
	 * Method that analyzes the list of projection attributes
	 * 
	 * @param ListNode
	 *            x - Node that represents a list of AttributeNodes
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
	private void attributeNodeListCheck(ListNode x, int scope) {
		if (x == null)
			return;
		attributeNodeCheck((AttributeNode) x.getNode(), scope);
		attributeNodeListCheck(x.getNext(), scope);
	}

	/**
	 * Method that analyzes the AttributeNode
	 * 
	 * @param AttributeNode
	 *            x - Node that represents a AttributeNode
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
	private void attributeNodeCheck(AttributeNode x, int scope) {
		if (x == null)
			return;
		Relation r = schema.getRelation("temporaryRelation" + (scope + 1));
		if (r.hasAttribute(x.getPosition().image)) {
			schema.getRelation("temporaryRelation" + scope).addAttribute(x.getPosition().image,
					r.getAttribute(x.getPosition().image));
		} else {
			throwSemanticError("\tAttribute does not exist: \"" + x.getPosition().image + "\" at the line "
					+ x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
		}
	}

	/**
	 * Method that analyzes the SelectNode
	 * 
	 * @param SelectNode
	 *            x - Node that represents a selection operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
	private void selectNodeCheck(SelectNode x, int scope) {
		if (x == null)
			return;
		logicalSentenceNodeCheck(x.getLogicalSentenceNode(), scope);
		schema.addRelation("temporaryRelation" + scope, schema.getRelation("temporaryRelation" + (scope+1)));
	}

	/**
	 * Method that analyzes the LogicalSentenceNode
	 * 
	 * @param LogicalSentenceNode
	 *            x - Node that represents a logical sentence
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int logicalSentenceNodeCheck(LogicalSentenceNode x, int scope) {
		if (x == null)
			return 0;
		if (x.getConditionalSentenceNode() != null)
			return conditionalSentenceNodeCheck(x.getConditionalSentenceNode(), scope);
		else
			return logicalOperatorNodeCheck(x.getLogicalOperatorNode(), scope);
	}

	/**
	 * Method that analyzes the LogicalOperatorNode
	 * 
	 * @param LogicalOperatorNode
	 *            x - Node that represents a logical operator
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int logicalOperatorNodeCheck(LogicalOperatorNode x, int scope) {
		if (x == null)
			return 0;
		int kind1, kind2;
		kind1 = conditionalSentenceNodeCheck(x.getConditionalSentenceNode1(), scope);
		if (x.getNextLogicalOperatorNode() == null)
			kind2 = conditionalSentenceNodeCheck(x.getConditionalSentenceNode2(), scope);
		else
			kind2 = logicalOperatorNodeCheck(x.getNextLogicalOperatorNode(), scope);
		int trueConstant = RelationalQueryLanguageConstants.TRUE;
		int falseConstant = RelationalQueryLanguageConstants.FALSE;
		if ((kind1 != trueConstant && kind1 != falseConstant) || (kind2 != trueConstant && kind2 != falseConstant)) {
			throwSemanticError("\tLogical operations requires two boolean values: At the line "
					+ x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
		}
		return kind1;
	}

	/**
	 * Method that analyzes the ConditionalSentenceNode
	 * 
	 * @param ConditionalSentenceNode
	 *            x - Node that represents a conditional sentence
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int conditionalSentenceNodeCheck(ConditionalSentenceNode x, int scope) {
		if (x == null)
			return 0;
		int kind1 = comparisonSentenceNodeCheck(x.getComparisonSentenceNode(), scope);
		ifNodeListCheck(x.getIfListNode(), scope);
		return kind1;
	}

	/**
	 * Method that analyzes the list of IfNodes
	 * 
	 * @param ListNode
	 *            x - Node that represents a list of IfNodes
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int ifNodeListCheck(ListNode x, int scope) {
		if (x == null)
			return 0;
		int kind1 = ifNodeCheck((IfNode) x.getNode(), scope);
		ifNodeListCheck(x.getNext(), scope);
		return kind1;
	}

	/**
	 * Method that analyzes the list of IfNodes
	 * 
	 * @param ListNode
	 *            x - Node that represents a list of IfNodes
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int ifNodeCheck(IfNode x, int scope) {
		if (x == null)
			return 0;
		int kind1 = comparisonSentenceNodeCheck(x.getComparisonSentenceNode1(), scope);
		comparisonSentenceNodeCheck(x.getComparisonSentenceNode2(), scope);
		return kind1;
	}

	/**
	 * Method that analyzes the list of ComparisonSentenceNode
	 * 
	 * @param ComparisonSentenceNode
	 *            x - Node that represents a comparison sentence
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int comparisonSentenceNodeCheck(ComparisonSentenceNode x, int scope) {
		if (x == null)
			return 0;
		if (x.getInstanceofSentenceNode() != null)
			return instanceofSentenceNodeCheck(x.getInstanceofSentenceNode(), scope);
		else
			return comparisonOperatorNodeCheck(x.getComparisonOperatorNode(), scope);
	}

	/**
	 * Method that analyzes the list of ComparisonOperatorNode
	 * 
	 * @param ComparisonOperatorNode
	 *            x - Node that represents a comparison operator
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int comparisonOperatorNodeCheck(ComparisonOperatorNode x, int scope) {
		if (x == null)
			return 0;
		int kind1, kind2;
		kind1 = instanceofSentenceNodeCheck(x.getInstanceofSentenceNode1(), scope);
		if (x.getNextComparisonOperatorNode() == null)
			kind2 = instanceofSentenceNodeCheck(x.getInstanceofSentenceNode2(), scope);
		else
			kind2 = comparisonOperatorNodeCheck(x.getNextComparisonOperatorNode(), scope);
		if (x.getPosition().kind != RelationalQueryLanguageConstants.EQUALS
				&& x.getPosition().kind != RelationalQueryLanguageConstants.NOT_EQUALS)
			if (!numberConstants.contains(kind1) || !numberConstants.contains(kind2)) {
				throwSemanticError("\tSize comparators requires two numeric operators : At the line "
						+ x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
			}
		return RelationalQueryLanguageConstants.TRUE;
	}

	/**
	 * Method that analyzes the list of InstanceofSentenceNode
	 * 
	 * @param InstanceofSentenceNode
	 *            x - Node that represents a instanceof sentence
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int instanceofSentenceNodeCheck(InstanceofSentenceNode x, int scope) {
		if (x == null)
			return 0;
		if (x.getType() == null)
			return additionSentenceNodeCheck(x.getAdditionSentenceNode(), scope);
		else
			return RelationalQueryLanguageConstants.TRUE;
	}

	/**
	 * Method that analyzes the list of AdditionSentenceNode
	 * 
	 * @param AdditionSentenceNode
	 *            x - Node that represents a addition sentence
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int additionSentenceNodeCheck(AdditionSentenceNode x, int scope) {
		if (x == null)
			return 0;
		if (x.getMultiplicationSentenceNode() != null)
			return multiplicationSentenceNodeCheck(x.getMultiplicationSentenceNode(), scope);
		else
			return additionOperatorNodeCheck(x.getAdditionOperatorNode(), scope);
	}

	/**
	 * Method that analyzes the list of AdditionOperator
	 * 
	 * @param AdditionOperatorNode
	 *            x - Node that represents a addition operator
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int additionOperatorNodeCheck(AdditionOperatorNode x, int scope) {
		if (x == null)
			return 0;
		int kind1, kind2;
		kind1 = multiplicationSentenceNodeCheck(x.getMultiplicationSentenceNode1(), scope);
		if (x.getNextAdditionOperatorNode() == null)
			kind2 = multiplicationSentenceNodeCheck(x.getMultiplicationSentenceNode2(), scope);
		else
			kind2 = additionOperatorNodeCheck(x.getNextAdditionOperatorNode(), scope);
		if (!numberConstants.contains(kind1) || !numberConstants.contains(kind2)) {
			throwSemanticError("\tAddition or subtraction requires two numeric operators : At the line "
					+ x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
		}
		return kind1;
	}

	/**
	 * Method that analyzes the list of MultiplicationSentenceNode
	 * 
	 * @param MultiplicationSentenceNode
	 *            x - Node that represents a multiplication sentence
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int multiplicationSentenceNodeCheck(MultiplicationSentenceNode x, int scope) {
		if (x == null)
			return 0;
		if (x.getFactorNode() != null)
			return factorNodeCheck(x.getFactorNode(), scope);
		else
			return multiplicationOperatorNode(x.getMultiplicationOperatorNode(), scope);
	}

	/**
	 * Method that analyzes the list of MultiplicationOperatorNode
	 * 
	 * @param MultiplicationOperatorNode
	 *            x - Node that represents a multiplication operator
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int multiplicationOperatorNode(MultiplicationOperatorNode x, int scope) {
		if (x == null)
			return 0;
		int kind1, kind2;
		kind1 = factorNodeCheck(x.getFactorNode1(), scope);
		if (x.getNextMultiplicationOperatorNode() == null) {
			kind2 = factorNodeCheck(x.getFactorNode2(), scope);
		} else
			kind2 = multiplicationOperatorNode(x.getNextMultiplicationOperatorNode(), scope);
		if (!numberConstants.contains(kind1) || !numberConstants.contains(kind2)) {
			throwSemanticError(
					"\tMultiplication, division, power or MOD operation requires two numeric operators : At the line "
							+ x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
		}
		return kind1;
	}

	/**
	 * Method that analyzes the list of FactorNode
	 * 
	 * @param FactorNode
	 *            x - Node that represents a factor
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 * @return int - The type returned by the children operations
	 */
	private int factorNodeCheck(FactorNode x, int scope) {
		if (x == null)
			return 0;
		if (x.getConditionalSentenceNode() != null)
			return conditionalSentenceNodeCheck(x.getConditionalSentenceNode(), scope);
		else {
			if (x.getPosition().kind == RelationalQueryLanguageConstants.IDENTIFIER) {
				String type;
				if (!schema.getRelation("temporaryRelation" + (scope + 1)).hasAttribute(x.getPosition().image)) {
					throwSemanticError("\tAttribute does not exist: \"" + x.getPosition().image + "\" at the line "
							+ x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
					type = "";
				}else{
					Attribute attribute = schema.getRelation("temporaryRelation" + (scope + 1))
							.getAttribute(x.getPosition().image);
					type = attribute.getType();
				}
				return symbolTableTypeConvert(type);
			}
		}
		return x.getPosition().kind;
	}

	/**
	 * Method that convert the symbol table types to Relational Query Constants
	 * 
	 * @param String
	 *            type - type to be converted
	 * @return int - the number of type constant
	 */
	private int symbolTableTypeConvert(String type) {
		if (type.equals("VARCHAR") || type.equals("VARCHAR"))
			return RelationalQueryLanguageConstants.STRING;
		else if (type.equals("INT") || type.equals("INTEGER")){
			return RelationalQueryLanguageConstants.INTEGER;
		}else if (type.equals("DOUBLE") || type.equals("DECIMAL") || type.equals("FLOAT") || type.equals("LONG")
				|| type.equals("BLOB"))
			return RelationalQueryLanguageConstants.DECIMAL;
		else
			return 0;
	}

	/**
	 * Method that analyzes the RenameNode
	 * 
	 * @param RenameNode
	 *            x - Node that represents a rename operation
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
	private void renameNodeCheck(RenameNode x, int scope) {
		if (x == null)
			return;
		renameSetNodeListCheck(x.getRenameSetNodeList(), scope);
	}

	/**
	 * Method that analyzes the list of rename attributes set
	 * 
	 * @param ListNode
	 *            x - Node that represents a list of AttributeSetNode
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
	private void renameSetNodeListCheck(ListNode x, int scope) {
		if (x == null)
			return;
		renameSetNodeCheck((RenameSetNode) x.getNode(), scope);
		renameSetNodeListCheck(x.getNext(), scope);
	}

	/**
	 * Method that analyzes the RenameSetNode
	 * 
	 * @param RenameSetNode
	 *            x - Node that represents a set with the old and new name for
	 *            the attribute
	 * @param int
	 *            scope - Indicates the scope of the relationship that this node
	 *            belongs
	 */
	private void renameSetNodeCheck(RenameSetNode x, int scope) {
		if (x == null)
			return;
		Relation r = schema.getRelation("temporaryRelation" + (scope + 1));
		String toRename = x.getToRenameAttributeNode().getPosition().image;
		String renamed = x.getRenamedAttributeNode().getPosition().image;
		if (r.hasAttribute(toRename)) {
			schema.getRelation("temporaryRelation" + scope).addAttribute(renamed, r.getAttribute(toRename));
		} else {
			throwSemanticError("\tAttribute does not exist: \"" + x.getToRenameAttributeNode().getPosition().image
					+ "\" at the line " + x.getToRenameAttributeNode().getPosition().beginLine + ", column "
					+ x.getToRenameAttributeNode().getPosition().beginColumn);
		}

	}

	/**
	 * Method that analyzes the RelationNode
	 * 
	 * @param RelationNode
	 *            x - Node that represents a relation operation
	 */
	private void relationNodeCheck(RelationNode x) {
		if (x == null)
			return;
		if (schema.hasRelation(x.getPosition().image)) {
			// System.out.println("DEFINING: temporaryRelation" + globalScope +
			// " AS " + x.getPosition().image);
			schema.replaceRelation("temporaryRelation" + globalScope, schema.getRelation(x.getPosition().image));
		} else {
			throwSemanticError("\tRelation does not exist: \"" + x.getPosition().image + "\" at the line "
					+ x.getPosition().beginLine + ", column " + x.getPosition().beginColumn);
		}
	}

}
