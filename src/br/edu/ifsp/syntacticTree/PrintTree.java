package br.edu.ifsp.syntacticTree;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFileChooser;

/**
 * Class responsible for print the syntactic tree
 * 
 * @author Dérick Welman
 */
public class PrintTree {
	private int kk;

	/**
	 * Method used to initialize the node count
	 */
	public PrintTree() {
		kk = 1; // initialize the node count
	}

	/**
	 * Method used to print on standard output the syntactic tree analysis
	 * 
	 * @param ListNode x - Root of syntactic tree
	 */
	public void printRoot(ListNode x) {
		if (x == null) {
			System.out.println("Empty syntctic tree. Nothing to be printed");
		} else {
			System.out.println("\nPrinting the syntactic analysis:");
			if (kk == 1)
				numberRelationalOperationsNodeList(x);
			printRelationalOperationsNodeList(x);
			System.out.println();
		}
	}

	/**
	 * Method used to print the .Dot GraphViz extension on a external file
	 * 
	 * @param ListNode x - Root of syntactic tree
	 */
	public void exportDotTree(ListNode x) throws IOException {
		if (x == null) {
			System.out.println("Empty syntctic tree. Nothing to be printed");
		} else {
			if (kk == 1)
				numberRelationalOperationsNodeList(x);

			JFileChooser file = new JFileChooser();
			file.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int i = file.showSaveDialog(null);
			if (i == 1) {

			} else {
				File arquivo = file.getSelectedFile();
				PrintWriter fw = new PrintWriter(arquivo);
				fw.write("digraph RQL{" + toGraphRelationalOperationsNodeList(x) + "\n}");
				fw.close();
				arquivo.createNewFile();
			}
		}
	}

	/**
	 * Method used to print the .Dot GraphViz extension on default output
	 * 
	 * @param ListNode x - Root of syntactic tree
	 */
	public void printDotTree(ListNode x){
		if (x == null) {
			System.out.println("Empty syntctic tree. Nothing to be printed");
		} else {
			if (kk == 1)
				numberRelationalOperationsNodeList(x);
			System.out.println("digraph RQL{" + toGraphRelationalOperationsNodeList(x) + "\n}");
		}
	}

	/*
	 * *******************************************************************
	 * Number the tree nodes
	 *********************************************************************/

	private void numberRelationalOperationsNodeList(ListNode x) {
		if (x == null)
			return;
		x.number = kk++;
		numberRelationalOperationsNode((RelationalOperationsNode) x.getNode());
		numberRelationalOperationsNodeList(x.getNext());
	}

	private void numberRelationalOperationsNode(RelationalOperationsNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getNode() instanceof QueryNode)
			numberQueryNode((QueryNode) x.getNode());
	}

	private void numberQueryNode(QueryNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getNode() instanceof ReadOnlyOperationsNode)
			numberReadyOnlyOperationsNode((ReadOnlyOperationsNode) x.getNode());
	}

	private void numberReadyOnlyOperationsNode(ReadOnlyOperationsNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getNode() instanceof UnitaryOperationsNode)
			numberUnitaryOperationsNode((UnitaryOperationsNode) x.getNode());
		if (x.getNode() instanceof BinaryOperationsNode)
			numberBinaryOperationsNode((BinaryOperationsNode) x.getNode());
	}

	private void numberBinaryOperationsNode(BinaryOperationsNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getBinaryOperationsNodeChildren() instanceof UnionNode)
			numberUnionNode((UnionNode) x.getBinaryOperationsNodeChildren());
		if (x.getBinaryOperationsNodeChildren() instanceof IntersectionNode)
			numberIntersectionNode((IntersectionNode) x.getBinaryOperationsNodeChildren());
		if (x.getBinaryOperationsNodeChildren() instanceof DifferenceNode)
			numberDifferenceNode((DifferenceNode) x.getBinaryOperationsNodeChildren());
		if (x.getBinaryOperationsNodeChildren() instanceof DivisionNode)
			numberDivisionNode((DivisionNode) x.getBinaryOperationsNodeChildren());
		if (x.getBinaryOperationsNodeChildren() instanceof JoinNode)
			numberJoinNode((JoinNode) x.getBinaryOperationsNodeChildren());
		if (x.getBinaryOperationsNodeChildren() instanceof CrossJoinNode)
			numberCrossJoinNode((CrossJoinNode) x.getBinaryOperationsNodeChildren());
		numberBinarySetNode(x.getBinarySetNode());
	}

	private void numberBinarySetNode(BinarySetNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getReadyOnlyOperationsNode1() != null)
			numberReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode1());
		if (x.getReadyOnlyOperationsNode2() != null)
			numberReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode2());
	}

	private void numberUnionNode(UnionNode x) {
		if (x == null)
			return;
		x.number = kk++;
	}

	private void numberIntersectionNode(IntersectionNode x) {
		if (x == null)
			return;
		x.number = kk++;
	}

	private void numberDifferenceNode(DifferenceNode x) {
		if (x == null)
			return;
		x.number = kk++;
	}

	private void numberDivisionNode(DivisionNode x) {
		if (x == null)
			return;
		x.number = kk++;
	}

	private void numberCrossJoinNode(CrossJoinNode x) {
		if (x == null)
			return;
		x.number = kk++;
	}

	private void numberJoinNode(JoinNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getLogicalSentenceNode() != null) {
			numberLogicalSentenceNode(x.getLogicalSentenceNode());
		}
	}

	private void numberUnitaryOperationsNode(UnitaryOperationsNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getUnitaryOperationsChildrenNode() instanceof ProjectNode)
			numberProjectNode((ProjectNode) x.getUnitaryOperationsChildrenNode());
		else if (x.getUnitaryOperationsChildrenNode() instanceof RenameNode)
			numberRenameNode((RenameNode) x.getUnitaryOperationsChildrenNode());
		else if (x.getUnitaryOperationsChildrenNode() instanceof SelectNode)
			numberSelectNode((SelectNode) x.getUnitaryOperationsChildrenNode());
		else if (x.getUnitaryOperationsChildrenNode() instanceof TransitiveCloseNode)
			numberTransitiveCloseNode((TransitiveCloseNode) x.getUnitaryOperationsChildrenNode());
		numberReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode());
	}

	private void numberTransitiveCloseNode(TransitiveCloseNode x) {
		if (x == null)
			return;
		x.number = kk++;
	}

	private void numberProjectNode(ProjectNode x) {
		if (x == null)
			return;
		x.number = kk++;
		numberAttributeNodeList(x.getProjectNodeList());
	}

	private void numberAttributeNodeList(ListNode x) {
		if (x == null)
			return;
		x.number = kk++;
		numberAttributeNodeList(x.getNext());
	}

	private void numberSelectNode(SelectNode x) {
		if (x == null)
			return;
		x.number = kk++;
		numberLogicalSentenceNode(x.getLogicalSentenceNode());
	}

	private void numberLogicalSentenceNode(LogicalSentenceNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getLogicalOperatorNode() == null)
			numberConditionalSentenceNode(x.getConditionalSentenceNode());
		else
			numberLogicalOperatorNode(x.getLogicalOperatorNode());
	}

	private void numberLogicalOperatorNode(LogicalOperatorNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getNextLogicalOperatorNode() == null) {
			numberConditionalSentenceNode(x.getConditionalSentenceNode1());
			numberConditionalSentenceNode(x.getConditionalSentenceNode2());
		} else {
			numberConditionalSentenceNode(x.getConditionalSentenceNode1());
			numberLogicalOperatorNode(x.getNextLogicalOperatorNode());
		}
	}

	private void numberConditionalSentenceNode(ConditionalSentenceNode x) {
		if (x == null)
			return;
		x.number = kk++;
		numberComparisonSentenceNode(x.getComparisonSentenceNode());
		numberIfNodeList(x.getIfListNode());
	}

	private void numberIfNodeList(ListNode x) {
		if (x == null)
			return;
		x.number = kk++;
		numberIfNode((IfNode) x.getNode());
		numberIfNodeList(x.getNext());
	}

	private void numberIfNode(IfNode x) {
		if (x == null)
			return;
		x.number = kk++;
		numberComparisonSentenceNode(x.getComparisonSentenceNode1());
		numberComparisonSentenceNode(x.getComparisonSentenceNode2());
	}

	private void numberComparisonSentenceNode(ComparisonSentenceNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getComparisonOperatorNode() == null)
			numberInstanceofSentenceNode(x.getInstanceofSentenceNode());
		else
			numberComparisonOperatorNode(x.getComparisonOperatorNode());
	}

	private void numberComparisonOperatorNode(ComparisonOperatorNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getNextComparisonOperatorNode() == null) {
			numberInstanceofSentenceNode(x.getInstanceofSentenceNode1());
			numberInstanceofSentenceNode(x.getInstanceofSentenceNode2());
		} else {
			numberInstanceofSentenceNode(x.getInstanceofSentenceNode1());
			numberComparisonOperatorNode(x.getNextComparisonOperatorNode());
		}
	}

	private void numberInstanceofSentenceNode(InstanceofSentenceNode x) {
		if (x == null)
			return;
		x.number = kk++;
		numberAdditionSentenceNode(x.getAdditionSentenceNode());
	}

	private void numberAdditionSentenceNode(AdditionSentenceNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getAdditionOperatorNode() == null)
			numberMultiplicationSentenceNode(x.getMultiplicationSentenceNode());
		else
			numberAdditionOperatorNode(x.getAdditionOperatorNode());
	}

	private void numberAdditionOperatorNode(AdditionOperatorNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getNextAdditionOperatorNode() == null) {
			numberMultiplicationSentenceNode(x.getMultiplicationSentenceNode1());
			numberMultiplicationSentenceNode(x.getMultiplicationSentenceNode2());
		} else {
			numberMultiplicationSentenceNode(x.getMultiplicationSentenceNode1());
			numberAdditionOperatorNode(x.getNextAdditionOperatorNode());
		}
	}

	private void numberMultiplicationSentenceNode(MultiplicationSentenceNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getMultiplicationOperatorNode() == null)
			numberFactorNode(x.getFactorNode());
		else
			numberMultiplicationOperatorNode(x.getMultiplicationOperatorNode());
	}

	private void numberMultiplicationOperatorNode(MultiplicationOperatorNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getNextMultiplicationOperatorNode() == null) {
			numberFactorNode(x.getFactorNode1());
			numberFactorNode(x.getFactorNode2());
		} else {
			numberFactorNode(x.getFactorNode1());
			numberMultiplicationOperatorNode(x.getNextMultiplicationOperatorNode());
		}
	}

	private void numberFactorNode(FactorNode x) {
		if (x == null)
			return;
		x.number = kk++;
		numberConditionalSentenceNode(x.getConditionalSentenceNode());
	}

	private void numberRenameNode(RenameNode x) {
		if (x == null)
			return;
		x.number = kk++;
		numberRenameSetNodeList(x.getRenameSetNodeList());
	}

	private void numberRenameSetNodeList(ListNode x) {
		if (x == null)
			return;
		x.number = kk++;
		numberRenameSetNode((RenameSetNode) x.getNode());
		numberRenameSetNodeList(x.getNext());
	}

	private void numberRenameSetNode(RenameSetNode x) {
		if (x == null)
			return;
		x.number = kk++;
	}

	/*
	 * ******************************************************************* Print
	 * the numbered tree
	 *********************************************************************/

	private void printRelationalOperationsNodeList(ListNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": ListNode (RelationalOperationsNodeList) ===> "
				+ (x.getNode() == null ? "null" : String.valueOf(x.getNode().number)) + " "
				+ (x.getNext() == null ? "null" : String.valueOf(x.getNext().number)));
		printRelationalOperationsNode((RelationalOperationsNode) x.getNode());
		printRelationalOperationsNodeList(x.getNext());
	}

	private void printRelationalOperationsNode(RelationalOperationsNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": RelationalOperationsNode ===> "
				+ (x.getNode() == null ? "null" : String.valueOf(x.getNode().getNumber())));
		if (x.getNode() instanceof QueryNode)
			printQueryNode((QueryNode) x.getNode());
	}

	private void printQueryNode(QueryNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": QueryNode ===> "
				+ (x.getNode() == null ? "null" : String.valueOf(x.getNode().getNumber())));
		if (x.getNode() instanceof ReadOnlyOperationsNode)
			printReadyOnlyOperationsNode((ReadOnlyOperationsNode) x.getNode());
	}

	private void printReadyOnlyOperationsNode(ReadOnlyOperationsNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": ReadyOnlyOperationsNode ===> "
				+ (x.getNode() == null ? "null" : String.valueOf(x.getNode().getNumber())));
		if (x.getNode() instanceof UnitaryOperationsNode)
			printUnitaryOperationsNode((UnitaryOperationsNode) x.getNode());
		if (x.getNode() instanceof BinaryOperationsNode)
			printBinaryOperationsNode((BinaryOperationsNode) x.getNode());
	}

	private void printBinaryOperationsNode(BinaryOperationsNode x) {
		if (x == null)
			return;
		String token = "";
		if (x.getBinaryOperationsNodeChildren() instanceof UnionNode)
			token = "v";
		if (x.getBinaryOperationsNodeChildren() instanceof IntersectionNode)
			token = "^";
		if (x.getBinaryOperationsNodeChildren() instanceof DifferenceNode)
			token = "-";
		if (x.getBinaryOperationsNodeChildren() instanceof DivisionNode)
			token = "/";
		if (x.getBinaryOperationsNodeChildren() instanceof CrossJoinNode)
			token = "x";
		if (x.getBinaryOperationsNodeChildren() instanceof JoinNode) {
			System.out.println(
					x.number + ": BinaryOperationsNode ===> " + x.getBinaryOperationsNodeChildren().getNumber() + " "
							+ (x.getBinarySetNode() == null ? "null" : String.valueOf(x.getBinarySetNode().number)));
			printJoinNode((JoinNode) x.getBinaryOperationsNodeChildren());
		} else
			System.out.println(x.number + ": BinaryOperationsNode ===> " + (token == "" ? "" : token + " ")
					+ (x.getBinarySetNode() == null ? "null" : String.valueOf(x.getBinarySetNode().number)));
		printBinarySetNode(x.getBinarySetNode());
	}

	private void printJoinNode(JoinNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": JoinNode ===> " + (x.getLogicalSentenceNode() == null ? "null (NATURAL JOIN)"
				: String.valueOf(x.getLogicalSentenceNode().number)));
		printLogicalSentenceNode(x.getLogicalSentenceNode());
	}

	private void printBinarySetNode(BinarySetNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": BinaryOperationsNode ===> "
				+ (x.getReadyOnlyOperationsNode1() == null ? x.getRelationNode1().getPosition().image
						: x.getReadyOnlyOperationsNode1().number)
				+ " " + (x.getReadyOnlyOperationsNode2() == null ? x.getRelationNode2().getPosition().image
						: x.getReadyOnlyOperationsNode2().number)
				+ " ");
		if (x.getReadyOnlyOperationsNode1() != null)
			printReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode1());
		if (x.getReadyOnlyOperationsNode2() != null)
			printReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode2());
	}

	private void printUnitaryOperationsNode(UnitaryOperationsNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": UnitaryOperationsNode ===> "
				+ (x.getUnitaryOperationsChildrenNode() == null ? "null"
						: String.valueOf(x.getUnitaryOperationsChildrenNode().getNumber()))
				+ " "
				+ (x.getReadyOnlyOperationsNode() == null ? "null"
						: String.valueOf(x.getReadyOnlyOperationsNode().number))
				+ " " + (x.getRelationNode() == null ? "null" : String.valueOf(x.getRelationNode().getImage())));
		if (x.getUnitaryOperationsChildrenNode() instanceof ProjectNode)
			printProjectNode((ProjectNode) x.getUnitaryOperationsChildrenNode());
		else if (x.getUnitaryOperationsChildrenNode() instanceof RenameNode)
			printRenameNode((RenameNode) x.getUnitaryOperationsChildrenNode());
		else if (x.getUnitaryOperationsChildrenNode() instanceof SelectNode)
			printSelectNode((SelectNode) x.getUnitaryOperationsChildrenNode());
		else if (x.getUnitaryOperationsChildrenNode() instanceof TransitiveCloseNode)
			printTransitiveCloseNode((TransitiveCloseNode) x.getUnitaryOperationsChildrenNode());
		printReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode());
	}

	private void printTransitiveCloseNode(TransitiveCloseNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": TransitiveCloseNode");
	}

	private void printProjectNode(ProjectNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": ProjectNode ===> "
				+ (x.getProjectNodeList() == null ? "null" : String.valueOf(x.getProjectNodeList().number)));
		printProjectNodeList(x.getProjectNodeList());
	}

	private void printProjectNodeList(ListNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": ListNode (ProjectAttributeList) ===> "
				+ (x.getNode() == null ? "null" : String.valueOf(x.getNode().getPosition().image)) + " "
				+ (x.getNext() == null ? "null" : String.valueOf(x.getNext().number)));
		printProjectNodeList(x.getNext());
	}

	private void printSelectNode(SelectNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": SelectNode ===> "
				+ (x.getLogicalSentenceNode() == null ? "null" : String.valueOf(x.getLogicalSentenceNode().number)));
		printLogicalSentenceNode(x.getLogicalSentenceNode());
	}

	private void printLogicalSentenceNode(LogicalSentenceNode x) {
		if (x == null)
			return;
		if (x.getLogicalOperatorNode() == null) {
			System.out.println(x.number + ": LogicalSentenceNode ===> " + (x.getConditionalSentenceNode() == null
					? "null" : String.valueOf(x.getConditionalSentenceNode().number)));
			printConditionalSentenceNode(x.getConditionalSentenceNode());
		} else {
			System.out.println(x.number + ": LogicalSentenceNode ===> " + (x.getLogicalOperatorNode() == null ? "null"
					: String.valueOf(x.getLogicalOperatorNode().number)));
			printLogicalOperatorNode(x.getLogicalOperatorNode());
		}
	}

	private void printLogicalOperatorNode(LogicalOperatorNode x) {
		if (x == null)
			return;
		if (x.getNextLogicalOperatorNode() == null) {
			System.out.println(x.number + ": LogicalOperatorNode ===> " + x.getPosition().image + " "
					+ (x.getConditionalSentenceNode1() == null ? "null"
							: String.valueOf(x.getConditionalSentenceNode1().number) + " "
									+ (x.getConditionalSentenceNode2() == null ? "null"
											: String.valueOf(x.getConditionalSentenceNode2().number))));
			printConditionalSentenceNode(x.getConditionalSentenceNode1());
			printConditionalSentenceNode(x.getConditionalSentenceNode2());
		} else {
			System.out.println(x.number + ": LogicalOperatorNode ===> " + x.getPosition().image + " "
					+ (x.getConditionalSentenceNode1() == null ? "null"
							: String.valueOf(x.getConditionalSentenceNode1().number))
					+ " " + (x.getNextLogicalOperatorNode() == null ? "null"
							: String.valueOf(x.getNextLogicalOperatorNode().number)));
			printConditionalSentenceNode(x.getConditionalSentenceNode1());
			printLogicalOperatorNode(x.getNextLogicalOperatorNode());
		}
	}

	private void printConditionalSentenceNode(ConditionalSentenceNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": ConditionalSentenceNode ===> "
				+ (x.getComparisonSentenceNode() == null ? "null" : x.getComparisonSentenceNode().number) + " "
				+ (x.getIfListNode() == null ? "null" : x.getIfListNode().number));
		printComparisonSentenceNode(x.getComparisonSentenceNode());
		printIfNodeList(x.getIfListNode());
	}

	private void printIfNodeList(ListNode x) {
		if (x == null)
			return;
		System.out.println(
				x.number + ": ListNode (IfNodeList) ===> " + (x.getNode() == null ? "null" : x.getNode().number) + " "
						+ (x.getNext() == null ? "null" : x.getNext().number));
		printIfNode((IfNode) x.getNode());
		printIfNodeList(x.getNext());
	}

	private void printIfNode(IfNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": IfNode ===> "
				+ (x.getComparisonSentenceNode1() == null ? "null" : x.getComparisonSentenceNode1().number) + " "
				+ (x.getComparisonSentenceNode2() == null ? "null" : x.getComparisonSentenceNode2().number));
		printComparisonSentenceNode(x.getComparisonSentenceNode1());
		printComparisonSentenceNode(x.getComparisonSentenceNode2());
	}

	private void printComparisonSentenceNode(ComparisonSentenceNode x) {
		if (x == null)
			return;
		System.out
				.println(x.number + ": ComparisonSentenceNode ===> "
						+ (x.getInstanceofSentenceNode() == null
								? (x.getComparisonOperatorNode() == null ? "null"
										: x.getComparisonOperatorNode().number)
								: x.getInstanceofSentenceNode().number));
		if (x.getComparisonOperatorNode() == null)
			printInstanceofSentenceNode(x.getInstanceofSentenceNode());
		else
			printComparisonOperatorNode(x.getComparisonOperatorNode());
	}

	private void printComparisonOperatorNode(ComparisonOperatorNode x) {
		if (x == null)
			return;
		if (x.getNextComparisonOperatorNode() == null) {
			System.out.println(x.number + ": ComparisonOperatorNode ===> " + x.getPosition().image + " "
					+ (x.getInstanceofSentenceNode1() == null ? "null"
							: String.valueOf(x.getInstanceofSentenceNode1().number) + " "
									+ (x.getInstanceofSentenceNode2() == null ? "null"
											: String.valueOf(x.getInstanceofSentenceNode2().number))));
			printInstanceofSentenceNode(x.getInstanceofSentenceNode1());
			printInstanceofSentenceNode(x.getInstanceofSentenceNode2());
		} else {
			System.out.println(x.number + ": ComparisonOperatorNode ===> " + x.getPosition().image + " "
					+ (x.getInstanceofSentenceNode1() == null ? "null"
							: String.valueOf(x.getInstanceofSentenceNode1().number))
					+ " " + (x.getNextComparisonOperatorNode() == null ? "null"
							: String.valueOf(x.getNextComparisonOperatorNode().number)));
			printInstanceofSentenceNode(x.getInstanceofSentenceNode1());
			printComparisonOperatorNode(x.getNextComparisonOperatorNode());
		}
	}

	private void printInstanceofSentenceNode(InstanceofSentenceNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": InstanceofSentenceNode ===> "
				+ (x.getAdditionSentenceNode() == null ? "null" : x.getAdditionSentenceNode().number) + " "
				+ (x.getType() == null ? "null" : x.getType().image));
		printAdditionSentenceNode(x.getAdditionSentenceNode());
	}

	private void printAdditionSentenceNode(AdditionSentenceNode x) {
		if (x == null)
			return;
		if (x.getAdditionOperatorNode() == null) {
			System.out.println(x.number + ": AdditionSentenceNode ===> " + (x.getMultiplicationSentenceNode() == null
					? "null" : String.valueOf(x.getMultiplicationSentenceNode().number)));
			printMultiplicationSentenceNode(x.getMultiplicationSentenceNode());
		} else {
			System.out.println(x.number + ": AdditionSentenceNode ===> " + (x.getAdditionOperatorNode() == null ? "null"
					: String.valueOf(x.getAdditionOperatorNode().number)));
			printAdditionOperatorNode(x.getAdditionOperatorNode());
		}
	}

	private void printAdditionOperatorNode(AdditionOperatorNode x) {
		if (x == null)
			return;
		if (x.getNextAdditionOperatorNode() == null) {
			System.out.println(x.number + ": AdditionOperatorNode ===> " + x.getPosition().image + " "
					+ (x.getMultiplicationSentenceNode1() == null ? "null"
							: String.valueOf(x.getMultiplicationSentenceNode1().number) + " "
									+ (x.getMultiplicationSentenceNode2() == null ? "null"
											: String.valueOf(x.getMultiplicationSentenceNode2().number))));
			printMultiplicationSentenceNode(x.getMultiplicationSentenceNode1());
			printMultiplicationSentenceNode(x.getMultiplicationSentenceNode2());
		} else {
			System.out.println(x.number + ": AdditionOperatorNode ===> " + x.getPosition().image + " "
					+ (x.getMultiplicationSentenceNode1() == null ? "null"
							: String.valueOf(x.getMultiplicationSentenceNode1().number))
					+ " " + (x.getNextAdditionOperatorNode() == null ? "null"
							: String.valueOf(x.getNextAdditionOperatorNode().number)));
			printMultiplicationSentenceNode(x.getMultiplicationSentenceNode1());
			printAdditionOperatorNode(x.getNextAdditionOperatorNode());
		}
	}

	private void printMultiplicationSentenceNode(MultiplicationSentenceNode x) {
		if (x == null)
			return;
		if (x.getMultiplicationOperatorNode() == null) {
			System.out.println(x.number + ": MultiplicationSentenceNode ===> "
					+ (x.getFactorNode() == null ? "null" : String.valueOf(x.getFactorNode().number)));
			printFactorNode(x.getFactorNode());
		} else {
			System.out.println(
					x.number + ": MultiplicationSentenceNode ===> " + (x.getMultiplicationOperatorNode() == null
							? "null" : String.valueOf(x.getMultiplicationOperatorNode().number)));
			printMultiplicationOperatorNode(x.getMultiplicationOperatorNode());
		}
	}

	private void printMultiplicationOperatorNode(MultiplicationOperatorNode x) {
		if (x == null)
			return;
		if (x.getNextMultiplicationOperatorNode() == null) {
			System.out
					.println(
							x.number + ": MultiplicatioinOperatorNode ===> " + x.getPosition().image + " "
									+ (x.getFactorNode1() == null ? "null"
											: String.valueOf(x.getFactorNode1().number) + " "
													+ (x.getFactorNode2() == null ? "null"
															: String.valueOf(x.getFactorNode2().number))));
			printFactorNode(x.getFactorNode1());
			printFactorNode(x.getFactorNode2());
		} else {
			System.out.println(x.number + ": MultiplicationOperatorNode ===> " + x.getPosition().image + " "
					+ (x.getFactorNode1() == null ? "null" : String.valueOf(x.getFactorNode1().number)) + " "
					+ (x.getNextMultiplicationOperatorNode() == null ? "null"
							: String.valueOf(x.getNextMultiplicationOperatorNode().number)));
			printFactorNode(x.getFactorNode1());
			printMultiplicationOperatorNode(x.getNextMultiplicationOperatorNode());
		}
	}

	private void printFactorNode(FactorNode x) {
		if (x == null)
			return;
		if (x.getConditionalSentenceNode() == null) {
			System.out.println(x.number + ": FactorNode ===> " + (x.getNot() == null ? "null" : "note") + " "
					+ String.valueOf(x.getPosition().image));
		} else {
			System.out.println(x.number + ": FactorNode ===> " + (x.getConditionalSentenceNode() == null ? "null"
					: String.valueOf(x.getConditionalSentenceNode().number)));
			printConditionalSentenceNode(x.getConditionalSentenceNode());
		}
	}

	private void printRenameNode(RenameNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": RenameNode ===> "
				+ (x.getRenameSetNodeList() == null ? "null" : String.valueOf(x.getRenameSetNodeList().number)));
		printRenameSetNodeList(x.getRenameSetNodeList());
	}

	private void printRenameSetNodeList(ListNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": ListNode (RenameSetNodeList) ===> "
				+ (x.getNode() == null ? "null" : String.valueOf(x.getNode().number)) + " "
				+ (x.getNext() == null ? "null" : String.valueOf(x.getNext().number)));
		printRenameSetNode((RenameSetNode) x.getNode());
		printRenameSetNodeList(x.getNext());
	}

	private void printRenameSetNode(RenameSetNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": RenameSetNode ===> "
				+ (x.getToRenameAttributeNode() == null ? "null"
						: String.valueOf(x.getToRenameAttributeNode().getPosition().image))
				+ " " + (x.getRenamedAttributeNode() == null ? "null"
						: String.valueOf(x.getRenamedAttributeNode().getPosition().image)));
	}

	/*
	 * *******************************************************************
	 * Export to GrapViz Dot
	 *********************************************************************/

	private String toGraphRelationalOperationsNodeList(ListNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"ListNode(RelationalOperationsNodeList)\"];"
				+ (x.getNode() == null ? "" : "\n" + x.number + " -> " + String.valueOf(x.getNode().number) + ";")
				+ (x.getNext() == null ? "" : "\n" + x.number + " -> " + String.valueOf(x.getNext().number) + ";"));
		temp += toGraphRelationalOperationsNode((RelationalOperationsNode) x.getNode());
		return temp + toGraphRelationalOperationsNodeList(x.getNext());
	}

	private String toGraphRelationalOperationsNode(RelationalOperationsNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"RelationalOperationsNode\"];" + (x.getNode() == null ? ""
				: "\n" + x.number + " -> " + String.valueOf(x.getNode().getNumber()) + ";"));
		if (x.getNode() instanceof QueryNode)
			return temp + toGraphQueryNode((QueryNode) x.getNode());
		return "";
	}

	private String toGraphQueryNode(QueryNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"QueryNode\"];" + (x.getNode() == null ? ""
				: "\n" + x.number + " -> " + String.valueOf(x.getNode().getNumber()) + ";"));
		if (x.getNode() instanceof ReadOnlyOperationsNode)
			return temp + toGraphReadyOnlyOperationsNode((ReadOnlyOperationsNode) x.getNode());
		return "";
	}

	private String toGraphReadyOnlyOperationsNode(ReadOnlyOperationsNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"ReadyOnlyOperationsNode\"];" + (x.getNode() == null ? ""
				: "\n" + x.number + " -> " + String.valueOf(x.getNode().getNumber()) + ";"));
		if (x.getNode() instanceof UnitaryOperationsNode)
			return temp + toGraphUnitaryOperationsNode((UnitaryOperationsNode) x.getNode());
		if (x.getNode() instanceof BinaryOperationsNode)
			return temp + toGraphBinaryOperationsNode((BinaryOperationsNode) x.getNode());
		return "";
	}

	private String toGraphBinaryOperationsNode(BinaryOperationsNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"BinaryOperationsNode\"];"
				+ (x.getBinaryOperationsNodeChildren() == null ? ""
						: "\n" + x.number + " -> " + String.valueOf(x.getBinaryOperationsNodeChildren().getNumber())
								+ ";")
				+ (x.getBinarySetNode() == null ? ""
						: "\n" + x.number + " -> " + String.valueOf(x.getBinarySetNode().number) + ";"));
		if (x.getBinaryOperationsNodeChildren() instanceof UnionNode)
			temp += toGraphUnionNode((UnionNode) x.getBinaryOperationsNodeChildren());
		if (x.getBinaryOperationsNodeChildren() instanceof IntersectionNode)
			temp += toGraphIntersectionNode((IntersectionNode) x.getBinaryOperationsNodeChildren());
		if (x.getBinaryOperationsNodeChildren() instanceof DifferenceNode)
			temp += toGraphDifferenceNode((DifferenceNode) x.getBinaryOperationsNodeChildren());
		if (x.getBinaryOperationsNodeChildren() instanceof DivisionNode)
			temp += toGraphDivisionNode((DivisionNode) x.getBinaryOperationsNodeChildren());
		if (x.getBinaryOperationsNodeChildren() instanceof JoinNode)
			temp += toGraphJoinNode((JoinNode) x.getBinaryOperationsNodeChildren());
		if (x.getBinaryOperationsNodeChildren() instanceof CrossJoinNode)
			temp += toGraphCrossJoinNode((CrossJoinNode) x.getBinaryOperationsNodeChildren());
		return temp + toGraphBinarySetNode(x.getBinarySetNode());
	}

	private String toGraphBinarySetNode(BinarySetNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"BinarySetNode\"];" + (x.getReadyOnlyOperationsNode1() == null
				? "\n" + x.number + " -> \"" + x.getRelationNode1().getPosition().image + " (" + x.number + ")\""
				: "\n" + x.number + " -> " + String.valueOf(x.getReadyOnlyOperationsNode1().getNumber()))
				+ (x.getReadyOnlyOperationsNode2() == null ? "\n" + x.number + " -> \""
						+ x.getRelationNode2().getPosition().image + " (" + x.number + ")\""
						: "\n" + x.number + " -> " + String.valueOf(x.getReadyOnlyOperationsNode2().getNumber())));
		if (x.getReadyOnlyOperationsNode1() != null)
			temp += toGraphReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode1());
		if (x.getReadyOnlyOperationsNode2() != null)
			temp += toGraphReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode2());
		return temp;
	}

	private String toGraphUnionNode(UnionNode x) {
		if (x == null)
			return "";
		return x.number + " [label=\"v\"];";
	}

	private String toGraphIntersectionNode(IntersectionNode x) {
		if (x == null)
			return "";
		return x.number + " [label=\"^\"];";
	}

	private String toGraphDifferenceNode(DifferenceNode x) {
		if (x == null)
			return "";
		return x.number + " [label=\"-\"];";
	}

	private String toGraphCrossJoinNode(CrossJoinNode x) {
		if (x == null)
			return "";
		return x.number + " [label=\"x\"];";
	}

	private String toGraphDivisionNode(DivisionNode x) {
		if (x == null)
			return "";
		return x.number + " [label=\"/\"];";
	}

	private String toGraphJoinNode(JoinNode x) {
		if (x == null)
			return "";
		return x.number + " [label=\"JoinNode " + (x.getLogicalSentenceNode() == null ? " (Natuaral)" : "") + "\"];"
				+ (x.getLogicalSentenceNode() == null ? ""
						: "\n" + x.number + " -> " + String.valueOf(x.getLogicalSentenceNode().number) + "; ")
				+ toGraphLogicalSentenceNode(x.getLogicalSentenceNode());
	}

	private String toGraphUnitaryOperationsNode(UnitaryOperationsNode x) {
		if (x == null)
			return "";
		String temp = "\n"
				+ (x.number + " [label=\"UnitaryOperationsNode\"];"
						+ (x.getUnitaryOperationsChildrenNode() == null ? ""
								: "\n" + x.number + " -> "
										+ String.valueOf(x.getUnitaryOperationsChildrenNode().getNumber()) + ";")
						+ (x.getReadyOnlyOperationsNode() == null ? ""
								: "\n" + x.number + " -> " + String.valueOf(x.getReadyOnlyOperationsNode().number)
										+ "; ")
						+ (x.getRelationNode() == null ? ""
								: "\n" + x.number + " -> \"" + String.valueOf(x.getRelationNode().getImage()) + " ("
										+ String.valueOf(x.number) + ")\";"));
		if (x.getUnitaryOperationsChildrenNode() instanceof ProjectNode)
			temp += toGraphProjectNode((ProjectNode) x.getUnitaryOperationsChildrenNode());
		else if (x.getUnitaryOperationsChildrenNode() instanceof RenameNode)
			temp += toGraphRenameNode((RenameNode) x.getUnitaryOperationsChildrenNode());
		else if (x.getUnitaryOperationsChildrenNode() instanceof SelectNode)
			temp += toGraphSelectNode((SelectNode) x.getUnitaryOperationsChildrenNode());
		else if (x.getUnitaryOperationsChildrenNode() instanceof TransitiveCloseNode)
			temp += toGraphTransitiveCloseNode((TransitiveCloseNode) x.getUnitaryOperationsChildrenNode());
		return temp + toGraphReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode());
	}

	private String toGraphTransitiveCloseNode(TransitiveCloseNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"TransitiveCloseNode\"];";
		return temp;
	}

	private String toGraphProjectNode(ProjectNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"ProjectNode\"];" + (x.getProjectNodeList() == null ? ""
				: "\n" + x.number + " -> " + String.valueOf(x.getProjectNodeList().number) + ";"));
		return temp + toGraphProjectNodeList(x.getProjectNodeList());
	}

	private String toGraphProjectNodeList(ListNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"ListNode(ProjectAttributeList)\"];" + (x.getNode() == null ? ""
				: "\n" + x.number + " -> \"" + String.valueOf(x.getNode().getPosition().image) + " ("
						+ String.valueOf(x.number) + ")\";")
				+ (x.getNext() == null ? "" : "\n" + x.number + " -> " + String.valueOf(x.getNext().number) + ";"));
		return temp + toGraphProjectNodeList(x.getNext());
	}

	private String toGraphSelectNode(SelectNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"SelectNode\"];" + (x.getLogicalSentenceNode() == null ? ""
				: "\n" + x.number + " -> " + String.valueOf(x.getLogicalSentenceNode().number) + ";");
		return temp + toGraphLogicalSentenceNode(x.getLogicalSentenceNode());
	}

	private String toGraphLogicalSentenceNode(LogicalSentenceNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"LogicalSentenceNode\"];";
		if (x.getLogicalOperatorNode() == null) {
			temp += (x.getConditionalSentenceNode() == null ? ""
					: "\n" + x.number + " -> " + String.valueOf(x.getConditionalSentenceNode().number) + ";");
			return temp + toGraphConditionalSentenceNode(x.getConditionalSentenceNode());
		} else {
			temp += (x.getLogicalOperatorNode() == null ? ""
					: "\n" + x.number + " -> " + String.valueOf(x.getLogicalOperatorNode().number) + ";");
			return temp + toGraphLogicalOperatorNode(x.getLogicalOperatorNode());
		}
	}

	private String toGraphLogicalOperatorNode(LogicalOperatorNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"LogicalOperatorNode ( " + x.getPosition().image + " )\"];"
				+ (x.getConditionalSentenceNode1() == null ? ""
						: "\n" + x.number + " ->" + x.getConditionalSentenceNode1().number + ";")
				+ (x.getConditionalSentenceNode2() == null ? ""
						: "\n" + x.number + " ->" + x.getConditionalSentenceNode2().number + ";")
				+ (x.getNextLogicalOperatorNode() == null ? ""
						: "\n" + x.number + " ->" + x.getNextLogicalOperatorNode().number + ";");
		return temp + toGraphConditionalSentenceNode(x.getConditionalSentenceNode1())
				+ toGraphConditionalSentenceNode(x.getConditionalSentenceNode2())
				+ toGraphLogicalOperatorNode(x.getNextLogicalOperatorNode());
	}

	private String toGraphConditionalSentenceNode(ConditionalSentenceNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"ConditionalSentenceNode\"];"
				+ (x.getComparisonSentenceNode() == null ? ""
						: "\n" + x.number + " -> " + x.getComparisonSentenceNode().number + ";")
				+ (x.getIfListNode() == null ? "" : "\n" + x.number + " -> " + x.getIfListNode().number + ";");
		return temp += toGraphComparisonSentenceNode(x.getComparisonSentenceNode())
				+ toGraphIfListNode(x.getIfListNode());
	}

	private String toGraphIfListNode(ListNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"ListNode(IfList)\"];"
				+ (x.getNode() == null ? "" : "\n" + x.number + " -> " + x.getNode().number + ";")
				+ (x.getNext() == null ? "" : "\n" + x.number + " -> " + x.getNext().number + ";");

		return temp + toGraphIfNode((IfNode) x.getNode()) + toGraphIfListNode(x.getNext());
	}

	private String toGraphIfNode(IfNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"IfNode\"];"
				+ (x.getComparisonSentenceNode1() == null ? ""
						: "\n" + x.number + " -> " + x.getComparisonSentenceNode1().number + ";")
				+ (x.getComparisonSentenceNode2() == null ? ""
						: "\n" + x.number + " -> " + x.getComparisonSentenceNode2().number + ";");
		return temp + toGraphComparisonSentenceNode(x.getComparisonSentenceNode1())
				+ toGraphComparisonSentenceNode(x.getComparisonSentenceNode2());
	}

	private String toGraphComparisonSentenceNode(ComparisonSentenceNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"ComparisonSentenceNode\"];"
				+ (x.getInstanceofSentenceNode() == null ? ""
						: "\n" + x.number + " -> " + x.getInstanceofSentenceNode().number + ";")
				+ (x.getComparisonOperatorNode() == null ? ""
						: "\n" + x.number + " -> " + x.getComparisonOperatorNode().number + ";");
		return temp + toGraphInstanceofSentenceNode(x.getInstanceofSentenceNode())
				+ toGraphComparisonOperatorNode(x.getComparisonOperatorNode());
	}

	private String toGraphComparisonOperatorNode(ComparisonOperatorNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"ComparisonOperatorNode ( " + x.getPosition().image + " )\"];"
				+ (x.getInstanceofSentenceNode1() == null ? ""
						: "\n" + x.number + " ->" + x.getInstanceofSentenceNode1().number + ";")
				+ (x.getInstanceofSentenceNode2() == null ? ""
						: "\n" + x.number + " ->" + x.getInstanceofSentenceNode2().number + ";")
				+ (x.getNextComparisonOperatorNode() == null ? ""
						: "\n" + x.number + " ->" + x.getNextComparisonOperatorNode().number + ";");
		return temp + toGraphInstanceofSentenceNode(x.getInstanceofSentenceNode1())
				+ toGraphInstanceofSentenceNode(x.getInstanceofSentenceNode2())
				+ toGraphComparisonOperatorNode(x.getNextComparisonOperatorNode());
	}

	private String toGraphInstanceofSentenceNode(InstanceofSentenceNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"InstanceofSentenceNode\"];"
				+ (x.getAdditionSentenceNode() == null ? ""
						: "\n" + x.number + " ->" + x.getAdditionSentenceNode().number + ";")
				+ (x.getType() == null ? "" : "\n" + x.number + " ->" + x.getType().image + "(" + x.number + ")" + ";");
		return temp + toGraphAdditionSentenceNode(x.getAdditionSentenceNode());
	}

	private String toGraphAdditionSentenceNode(AdditionSentenceNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"AdditionSentenceNode\"];"
				+ (x.getMultiplicationSentenceNode() == null ? ""
						: "\n" + x.number + " -> " + x.getMultiplicationSentenceNode().number + ";")
				+ (x.getAdditionOperatorNode() == null ? ""
						: "\n" + x.number + " -> " + x.getAdditionOperatorNode().number + ";");
		return temp + toGraphMultiplicationSentenceNode(x.getMultiplicationSentenceNode())
				+ toGraphAdditionOperatorNode(x.getAdditionOperatorNode());
	}

	private String toGraphAdditionOperatorNode(AdditionOperatorNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"AdditionOperatorNode ( " + x.getPosition().image + " )\"];"
				+ (x.getMultiplicationSentenceNode1() == null ? ""
						: "\n" + x.number + " ->" + x.getMultiplicationSentenceNode1().number + ";")
				+ (x.getMultiplicationSentenceNode2() == null ? ""
						: "\n" + x.number + " ->" + x.getMultiplicationSentenceNode2().number + ";")
				+ (x.getNextAdditionOperatorNode() == null ? ""
						: "\n" + x.number + " ->" + x.getNextAdditionOperatorNode().number + ";");
		return temp + toGraphMultiplicationSentenceNode(x.getMultiplicationSentenceNode1())
				+ toGraphMultiplicationSentenceNode(x.getMultiplicationSentenceNode2())
				+ toGraphAdditionOperatorNode(x.getNextAdditionOperatorNode());
	}

	private String toGraphMultiplicationSentenceNode(MultiplicationSentenceNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"MultiplicationSentenceNode\"];"
				+ (x.getFactorNode() == null ? "" : "\n" + x.number + " -> " + x.getFactorNode().number + ";")
				+ (x.getMultiplicationOperatorNode() == null ? ""
						: "\n" + x.number + " -> " + x.getMultiplicationOperatorNode().number + ";");
		return temp + toGraphFactorNode(x.getFactorNode())
				+ toGraphMultiplicationOperatorNode(x.getMultiplicationOperatorNode());
	}

	private String toGraphMultiplicationOperatorNode(MultiplicationOperatorNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"MultiplicationOperatorNode ( " + x.getPosition().image + " )\"];"
				+ (x.getFactorNode1() == null ? "" : "\n" + x.number + " ->" + x.getFactorNode1().number + ";")
				+ (x.getFactorNode2() == null ? "" : "\n" + x.number + " ->" + x.getFactorNode2().number + ";")
				+ (x.getNextMultiplicationOperatorNode() == null ? ""
						: "\n" + x.number + " ->" + x.getNextMultiplicationOperatorNode().number + ";");
		return temp + toGraphFactorNode(x.getFactorNode1()) + toGraphFactorNode(x.getFactorNode2())
				+ toGraphMultiplicationOperatorNode(x.getNextMultiplicationOperatorNode());
	}

	private String toGraphFactorNode(FactorNode x) {
		if (x == null)
			return "";
		String temp = "\n" + x.number + " [label=\"FactorNode\"];";
		if (x.getConditionalSentenceNode() == null)
			temp += (x.getNot() == null ? "" : "\n" + x.number + " -> Not(" + x.number + ");") + "\n" + x.number
					+ " -> \"" + x.getPosition().image.replaceAll("\"", "'") + "(" + x.number + ")\";";
		else
			temp += "\n" + x.number + " -> " + x.getConditionalSentenceNode().number + ";";
		return temp + toGraphConditionalSentenceNode(x.getConditionalSentenceNode());
	}

	private String toGraphRenameNode(RenameNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"RenameNode\"];" + (x.getRenameSetNodeList() == null ? ""
				: "\n" + x.number + " -> " + String.valueOf(x.getRenameSetNodeList().number) + ";"));
		return temp + toGraphRenameSetNodeList(x.getRenameSetNodeList());
	}

	private String toGraphRenameSetNodeList(ListNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"ListNode(RenameSetNodeList)\"];"
				+ (x.getNode() == null ? "" : "\n" + x.number + " -> " + String.valueOf(x.getNode().number) + ";")
				+ (x.getNext() == null ? "" : "\n" + x.number + " -> " + String.valueOf(x.getNext().number) + ";"));
		return temp + toGraphRenameSetNode((RenameSetNode) x.getNode()) + toGraphRenameSetNodeList(x.getNext());
	}

	private String toGraphRenameSetNode(RenameSetNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"RenameSetNode\"];"
				+ (x.getToRenameAttributeNode() == null ? ""
						: "\n" + x.number + " -> \"" + x.getToRenameAttributeNode().getPosition().image)
				+ " (" + String.valueOf(String.valueOf(x.number) + ")\";")
				+ (x.getRenamedAttributeNode() == null ? ""
						: "\n" + x.number + " -> \"" + x.getRenamedAttributeNode().getPosition().image)
				+ " (" + String.valueOf(String.valueOf(x.number) + ")\";"));
		return temp;
	}
}
