package br.edu.ifsp.syntacticTree;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

import br.edu.ifsp.syntacticTree.interfaces.UnitaryOperationsNodeChildren;

public class PrintTree {
	int kk;

	/**
	 * Method used to initialize the node count
	 */
	public PrintTree() {
		kk = 1; // initialize the node count
	}

	/**
	 * Method used to print on standard output the syntactic tree analysis
	 * 
	 * @param x
	 */
	public void printRoot(ListNode x) {
		if (x == null) {
			String temp = "\n" + ("Empty syntctic tree. Nothing to be printed");
		} else {
			System.out.println("\nPrinting the syntactic analysis:");
			numberRelationalOperationsNodeList(x);
			printRelationalOperationsNodeList(x);
			System.out.println();
		}
	}

	/**
	 * Method used to print the .Dot GraphViz extension on a external file
	 * 
	 * @param x
	 */
	public void exportDotTree(ListNode x) throws IOException {
		if (x == null) {
			String temp = "\n" + ("Empty syntctic tree. Nothing to be printed");
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
				fw.write("digraph RQL{\n" + toGraphRelationalOperationsNodeList(x) + "\n}");
				fw.close();
				arquivo.createNewFile();
			}
		}
	}

	/*
	 * *******************************************************************
	 * Number the tree nodes
	 *********************************************************************/

	public void numberRelationalOperationsNodeList(ListNode x) {
		if (x == null)
			return;
		x.number = kk++;
		numberRelationalOperationsNode((RelationalOperationsNode) x.getNode());
		numberRelationalOperationsNodeList(x.getNext());
	}

	public void numberRelationalOperationsNode(RelationalOperationsNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getNode() instanceof QueryNode)
			numberQueryNode((QueryNode) x.getNode());
	}

	public void numberQueryNode(QueryNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getNode() instanceof ReadyOnlyOperationsNode)
			numberReadyOnlyOperationsNode((ReadyOnlyOperationsNode) x.getNode());
	}

	private void numberReadyOnlyOperationsNode(ReadyOnlyOperationsNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getNode() instanceof UnitaryOperationsNode)
			numberUnitaryOperationsNode((UnitaryOperationsNode) x.getNode());
	}

	private void numberUnitaryOperationsNode(UnitaryOperationsNode x) {
		if (x == null)
			return;
		x.number = kk++;
		if (x.getUnitaryOperationsChildrenNode() instanceof ProjectNode)
			numberProjectNode((ProjectNode) x.getUnitaryOperationsChildrenNode());
		if (x.getUnitaryOperationsChildrenNode() instanceof RenameNode)
			numberRenameNode((RenameNode) x.getUnitaryOperationsChildrenNode());
		// if(x.getUnitaryOperationsChildrenNode() instanceof SelectNode)
		// numberSelectNode((ProjectNode)x.getNode());
		numberReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode());
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
		if (x.getNode() instanceof ReadyOnlyOperationsNode)
			printReadyOnlyOperationsNode((ReadyOnlyOperationsNode) x.getNode());
	}

	private void printReadyOnlyOperationsNode(ReadyOnlyOperationsNode x) {
		if (x == null)
			return;
		System.out.println(x.number + ": ReadyOnlyOperationsNode ===> "
				+ (x.getNode() == null ? "null" : String.valueOf(x.getNode().getNumber())));
		if (x.getNode() instanceof UnitaryOperationsNode)
			printUnitaryOperationsNode((UnitaryOperationsNode) x.getNode());
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
		// else if (x.getUnitaryOperationsChildrenNode() instanceof SelectNode)
		// printSelectNode((SelectNode) x.getUnitaryOperationsChildrenNode());
		printReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode());
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
		String temp = "\n"
				+ (x.number + " [label=\"ListNode(RelationalOperationsNodeList)\"];"
						+ (x.getNode() == null ? ""
								: "\n" + x.number + " -> " + String.valueOf(x.getNode().number) + ";")
						+ (x.getNext() == null ? ""
								: "\n" + x.number + " -> " + String.valueOf(x.getNext().number) + ";"));
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
		if (x.getNode() instanceof ReadyOnlyOperationsNode)
			return temp + toGraphReadyOnlyOperationsNode((ReadyOnlyOperationsNode) x.getNode());
		return "";
	}

	private String toGraphReadyOnlyOperationsNode(ReadyOnlyOperationsNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"ReadyOnlyOperationsNode\"];" + (x.getNode() == null ? ""
				: "\n" + x.number + " -> " + String.valueOf(x.getNode().getNumber()) + ";"));
		if (x.getNode() instanceof UnitaryOperationsNode)
			return temp + toGraphUnitaryOperationsNode((UnitaryOperationsNode) x.getNode());
		return "";
	}

	private String toGraphUnitaryOperationsNode(UnitaryOperationsNode x) {
		if (x == null)
			return "";
		String temp = "\n" + (x.number + " [label=\"UnitaryOperationsNode\"];"
				+ (x.getUnitaryOperationsChildrenNode() == null ? ""
						: "\n" + x.number + " -> " + String.valueOf(x.getUnitaryOperationsChildrenNode().getNumber())
								+ ";")

				+ (x.getReadyOnlyOperationsNode() == null ? ""
						: "\n" + x.number + " -> " + String.valueOf(x.getReadyOnlyOperationsNode().number) + "; ")
				+ (x.getRelationNode() == null ? ""
						: "\n" + x.number + " -> \"" + String.valueOf(x.getRelationNode().getImage()) + " ("
								+ String.valueOf(x.number) + ")\";"));
		if (x.getUnitaryOperationsChildrenNode() instanceof ProjectNode)
			temp += toGraphProjectNode((ProjectNode) x.getUnitaryOperationsChildrenNode());
		else if (x.getUnitaryOperationsChildrenNode() instanceof RenameNode)
			temp += toGraphRenameNode((RenameNode) x.getUnitaryOperationsChildrenNode());
		// else if (x.getNode() instanceof SelectNode)
		// toGraphSelectNode((SelectNode) x.getNode());
		return temp + toGraphReadyOnlyOperationsNode(x.getReadyOnlyOperationsNode());
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
		String temp = "\n" + (x.number + " [label=\"ListNode(ProjectAttributeList)\"];"
				+ (x.getNode() == null ? ""
						: "\n" + x.number + " -> \"" + String.valueOf(x.getNode().getPosition().image) + " ("
								+ String.valueOf(x.number) + ")\";")
				+ (x.getNext() == null ? "" : "\n" + x.number + " -> " + String.valueOf(x.getNext().number) + ";"));
		return temp + toGraphProjectNodeList(x.getNext());
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
		String temp = "\n"
				+ (x.number + " [label=\"ListNode(RenameSetNodeList)\"];"
						+ (x.getNode() == null ? ""
								: "\n" + x.number + " -> " + String.valueOf(x.getNode().number) + ";")
						+ (x.getNext() == null ? ""
								: "\n" + x.number + " -> " + String.valueOf(x.getNext().number) + ";"));
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
