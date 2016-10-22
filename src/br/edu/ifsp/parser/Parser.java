package br.edu.ifsp.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import br.edu.ifsp.codeGeneration.CodeGenerator;
import br.edu.ifsp.semanticAnalysis.RelationCheck;
import br.edu.ifsp.symbolTable.SymbolTable;
import br.edu.ifsp.syntacticTree.ListNode;

/**
 * Class responsible for translate a RQL input to SQL output
 * @author Dérick
 *
 */
public class Parser {

	/**
	 * Compiler version description.
	 */
	final String version = "Relational Query Language Translator 1.0 - 2016";

	public Translation translate(String definition, String rql) throws IOException, ParseException {

		/**
		 * Attribute used to store a reference of a parser object that will do
		 * the compilation of source code in Relational Query Language.
		 */

		InputStream stream = new ByteArrayInputStream(rql.getBytes(StandardCharsets.ISO_8859_1));
		/**
		 * Attribute used to store a reference of a parser object that will do
		 * the compilation of source code in Relational Query Language.
		 */
		RelationalQueryLanguage parser = new RelationalQueryLanguage(stream);

		/**
		 * Build a new symbol table (schema definition)
		 */
		SymbolTable symbolTable = new SymbolTable(definition);
		
		/**
		 * Start the syntactic analysis
		 */
		ListNode root = parser.program();
		/**
		 * Verifies the presence of lexical errors
		 */
		try{
		switch (parser.token_source.foundLexError()) {
		case 0:
			System.out.println("Lexical errors were not found.");
			break;

		case 1:
			System.out.printf("%c[1m%i%c[0m lexical error was found.\u005cn", 27, 1, 27);
			break;

		default:
			System.out.printf("%c[1m%i%c[0m lexical errors were found.\u005cn", 27, parser.token_source.foundLexError(),
					27);
		}}catch(Exception e){
		}
		
		/**
		 * Defines the semantic analysis and result variables
		 */
		String output = "";
		RelationCheck rc = null;
		int semanticErrors = 0;
		String errorsDescription = "";

		/**
		 * Starts the semantic analysis
		 */
		if (symbolTable != null) {
			rc = new RelationCheck(symbolTable);
			semanticErrors = rc.semanticAnalysis(root);
			errorsDescription = rc.getSemanticErrorsDescription();
			System.out.println("Semantic Errors: " + semanticErrors);
		}
		
		/**
		 * Generates the SQL output
		 */
		if (semanticErrors == 0) {
			CodeGenerator generator = new CodeGenerator(symbolTable, version);
			output = generator.generate(root);
		}

		/**
		 * Puts the information on a Translation object
		 */
		Translation translation = new Translation(root, semanticErrors, errorsDescription, output, symbolTable);
		System.out.println("Analysis finished.");
		return translation;
	}
	
	/**
	 * Method that returns the version of parser
	 * @return String version
	 */
	public String getVersion(){
		return version;
	}
}