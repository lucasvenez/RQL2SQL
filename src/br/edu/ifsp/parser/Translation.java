package br.edu.ifsp.parser;

import java.io.IOException;

import br.edu.ifsp.symbolTable.SymbolTable;
import br.edu.ifsp.syntacticTree.ListNode;
import br.edu.ifsp.syntacticTree.PrintTree;

/**
 * Class responsible for store the translation informations
 * @author Dérick
 *
 */
public class Translation {
	private ListNode root;
	private int semanticErrors;
	private String output;
	private SymbolTable symbolTable;
	private PrintTree printTree;
	private String errorsDescription;

	/**
	 * Method that translates the RQL
	 * @param root - The first node of syntactic tree
	 * @param semanticErrors - Count of semantic errors
	 * @param output - Translation of given RQL
	 * @param symbolTable - Symbol table with schema definition
	 */
	public Translation(ListNode root, int semanticErrors, String errorsDescription, String output, SymbolTable symbolTable) {
		this.root = root;
		this.semanticErrors = semanticErrors;
		this.output = output;
		this.symbolTable = symbolTable;
		this.printTree = new PrintTree();
		this.errorsDescription = errorsDescription;
	}

	/**
	 * Method that returns the semantic errors count
	 * @return int - the number of semantic errors
	 */
	public int getSemanticErrors() {
		return semanticErrors;
	}
	
	/**
	 * Method that returns the semantic errors description
	 * @return String - the description of semantic errors
	 */
	public String getSemanticErrorsDescription() {
		return errorsDescription;
	}
	
	/**
	 * Method that returns the syntactic tree root
	 * @return ListNode syntactic tree root
	 */
	public ListNode getSyntacticTree() {
		return root;
	}

	/**
	 * Method that returns the translation
	 * @return String translation
	 */
	public String getTranslation() {
		return output;
	}

	/**
	 * Method that returns the symbol table
	 * @return SymbolTable
	 */
	public SymbolTable getSchema() {
		return symbolTable;
	}
	
	/**
	 * Method that prints the relations and attributes of symbol table
	 */
	public void printSymbolTable(){
		symbolTable.printTable();
	}
	
	/**
	 * Method that prints the relations, attributes and features of this attributes
	 */
	public void printCompleteSymbolTable(){
		symbolTable.printCompleteTable();
	}
	
	/**
	 * Method that prints the analyzed syntactic tree
	 */
	public void printSyntacticTree(){
		printTree.printRoot(root);
	}
	
	/**
	 * Method that prints the DOT notation of syntactic tree
	 */
	public void printDotTree(){
		printTree.printDotTree(root);
	}
	
	/**
	 * Method that exports the syntactic tree to a .DOT file
	 * @throws IOException
	 */
	public void exportDotTree() throws IOException{
		printTree.exportDotTree(root);
	}
}
