package Tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.edu.ifsp.parser.ParseException;
import br.edu.ifsp.parser.RelationalQueryLanguage;
import br.edu.ifsp.parser.Token;
import br.edu.ifsp.semanticAnalysis.RelationCheck;
import br.edu.ifsp.symbolTable.*;
import br.edu.ifsp.syntacticTree.*;

public class RelationCheckTest {

	/*-a Pessoa.idPessoa: INTEGER PRIMARY KEY;
	 *  Pessoa.nome: VARCHAR(45);
	 *  Telefone.numero: VARCHAR(17);
	 *   Telefone.idPessoa: INTEGER REFERENCES Pessoa.idPessoa*/

	static SymbolTable table;
	RelationCheck rc;

	@BeforeClass
	public static void setUp() {
		/* Definition of symbol Table */
		table = new SymbolTable();
		Relation pessoa = new Relation();
		Relation telefone = new Relation();

		Attribute idPessoa = new Attribute("idPessoa", "INTEGER");
		Attribute nome = new Attribute("nome", "VARCHAR");
		Attribute numero = new Attribute("numero", "VARCHAR");

		pessoa.addAttribute("idPessoa", idPessoa);
		pessoa.addAttribute("nome", nome);
		telefone.addAttribute("numero", numero);
		telefone.addAttribute("idPessoa", idPessoa);

		table.addRelation("Pessoa", pessoa);
		table.addRelation("Telefone", telefone);
	}
	
	@Test
	public void relationSemanticTest() {
		
		rc = new RelationCheck(table);
		
		/* Successful relation
		 * Equivalent query:
		 * Pessoa;
		 *  */
		ListNode root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new RelationNode(
														new Token(0, "Pessoa")))))));
		
		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
		
		/* Failed relation
		 * Equivalent query:
		 * Carro;
		 *  */
		root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new RelationNode(
														new Token(0, "Carro")))))));
		
		assertEquals(1, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}

	@Test
	public void projectionSemanticTest() {
		
		rc = new RelationCheck(table);
		
		/* One attribute successful projection
		 * Equivalent query:
		 * ¢ idPessoa (Pessoa);
		 *  */
		ListNode root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new ProjectNode(
														new ListNode(
																new AttributeNode(
																		new Token(0, "idPessoa")))), 
												new ReadyOnlyOperationsNode(
														new UnitaryOperationsNode(
																new RelationNode(
																		new Token(0, "Pessoa")))))))));
		
		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
		
		/* One attribute failed projection
		 * Equivalent query:
		 * ¢ nome (Telefone);
		 *  */
		root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new ProjectNode(
														new ListNode(
																new AttributeNode(
																		new Token(0, "nome")))), 
												new ReadyOnlyOperationsNode(
														new UnitaryOperationsNode(
																new RelationNode(
																		new Token(0, "Telefone")))))))));
		
		assertEquals(1, rc.semanticAnalysis(root)); 
		rc.clearSemanticErrors();
		
		/* Two attribute successful projection
		 * Equivalent query:
		 * ¢ idPessoa, nome (Pessoa));
		 *  */
		ListNode list = new ListNode(new AttributeNode(new Token(0, "idPessoa")));
		list.add(new AttributeNode(new Token(0, "nome")));
		root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new ProjectNode(list), 
												new ReadyOnlyOperationsNode(
														new UnitaryOperationsNode(
																new RelationNode(
																		new Token(0, "Pessoa")))))))));
		
		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
		
		/* Two attribute failed projection
		 * Equivalent query:
		 * ¢ idTelefone, numero (Pessoa));
		 *  */
		list = new ListNode(new AttributeNode(new Token(0, "idTelefone")));
		list.add(new AttributeNode(new Token(0, "numero")));
		root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new ProjectNode(list), 
												new ReadyOnlyOperationsNode(
														new UnitaryOperationsNode(
																new RelationNode(
																		new Token(0, "Pessoa")))))))));
		
		assertEquals(2, rc.semanticAnalysis(root)); 
		rc.clearSemanticErrors();
		
		/* Two chained projection
		 * Equivalent query:
		 * ¢ nome (¢ idPessoa (Pessoa));
		 *  */
		root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new ProjectNode(
														new ListNode(
																new AttributeNode(
																		new Token(0, "nome")))), 
												new ReadyOnlyOperationsNode(
														new UnitaryOperationsNode(
																new ProjectNode(
																		new ListNode(
																				new AttributeNode(
																						new Token(0, "idPessoa")))), 
																new ReadyOnlyOperationsNode(
																		new UnitaryOperationsNode(
																				new RelationNode(
																						new Token(0, "Pessoa")))))))))));
		
		assertEquals(1, rc.semanticAnalysis(root)); 
		rc.clearSemanticErrors();
	}
	
	@Test
	public void renameSemanticTest() {
		
		rc = new RelationCheck(table);
		
		/* One attribute successful rename
		 * Equivalent query:
		 * § idPessoa id (Pessoa);
		 *  */
		ListNode root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new RenameNode(
														new ListNode(
																new RenameSetNode(
																		new Token(0, "idPessoa"), new Token(0, "id")))), 
												new ReadyOnlyOperationsNode(
														new UnitaryOperationsNode(
																new RelationNode(
																		new Token(0, "Pessoa")))))))));
		
		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
		
		/* One attribute failed rename
		 * Equivalent query:
		 * § numero num (Pessoa);
		 *  */
		root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new RenameNode(
														new ListNode(
																new RenameSetNode(
																		new Token(0, "numero"), new Token(0, "num")))), 
												new ReadyOnlyOperationsNode(
														new UnitaryOperationsNode(
																new RelationNode(
																		new Token(0, "Pessoa")))))))));
		
		assertEquals(1, rc.semanticAnalysis(root)); 
		rc.clearSemanticErrors();
		
		/* Two attribute successful rename
		 * Equivalent query:
		 * § idPessoa id, nome nomePessoa (Pessoa));
		 *  */
		ListNode list = new ListNode(new RenameSetNode(new Token(0, "idPessoa"), new Token(0, "id")));
		list.add(new RenameSetNode(new Token(0, "nome"), new Token(0, "nomePessoa")));
		root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new RenameNode(
														list), 
												new ReadyOnlyOperationsNode(
														new UnitaryOperationsNode(
																new RelationNode(
																		new Token(0, "Pessoa")))))))));
		
		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
		
		/* Two attribute failed rename
		 * Equivalent query:
		 * § numero num, id iidPessoa (Pessoa));
		 *  */
		list = new ListNode(new RenameSetNode(new Token(0, "numero"), new Token(0, "num")));
		list.add(new RenameSetNode(new Token(0, "id"), new Token(0, "idPessoa")));
		root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new RenameNode(
														list), 
												new ReadyOnlyOperationsNode(
														new UnitaryOperationsNode(
																new RelationNode(
																		new Token(0, "Pessoa")))))))));
		
		assertEquals(2, rc.semanticAnalysis(root)); 
		rc.clearSemanticErrors();
		
		/* Two chained renames
		 * Equivalent query:
		 * § id cod (§ idPessoa id (Pessoa));
		 *  */
		root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new RenameNode(
														new ListNode(
																new RenameSetNode(
																		new Token(0, "id"), new Token(0, "cod")))), 
												new ReadyOnlyOperationsNode(
														new UnitaryOperationsNode(
																new RenameNode(
																		new ListNode(
																				new RenameSetNode(
																						new Token(0, "idPessoa"), new Token(0, "id")))), 
																new ReadyOnlyOperationsNode(
																		new UnitaryOperationsNode(
																				new RelationNode(
																						new Token(0, "Pessoa")))))))))));
		
		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}
	
	@Test
	public void chainedOperationsSemanticTest() {
		
		rc = new RelationCheck(table);
		
		/* One attribute successful rename
		 * Equivalent query:
		 * § idPessoa id (¢ idPessoa (Pessoa));
		 *  */
		ListNode root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new RenameNode(
														new ListNode(
																new RenameSetNode(
																		new Token(0, "idPessoa"), new Token(0, "id")))), 
												new ReadyOnlyOperationsNode(
														new UnitaryOperationsNode(
																new ProjectNode(
																		new ListNode(
																				new AttributeNode(
																						new Token(0, "idPessoa")))), 
																new ReadyOnlyOperationsNode(
																		new UnitaryOperationsNode(
																				new RelationNode(
																						new Token(0, "Pessoa")))))))))));
		
		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
		
		/* One attribute successful rename
		 * Equivalent query:
		 * ¢ id (§ idPessoa id (Pessoa));
		 *  */
		root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(
								new ReadyOnlyOperationsNode(
										new UnitaryOperationsNode(
												new ProjectNode(
														new ListNode(
																new AttributeNode(
																		new Token(0, "id")))), 
												new ReadyOnlyOperationsNode(
														new UnitaryOperationsNode(
																new RenameNode(
																		new ListNode(
																				new RenameSetNode(
																						new Token(0, "idPessoa"), new Token(0, "id")))), 
																new ReadyOnlyOperationsNode(
																		new UnitaryOperationsNode(
																				new RelationNode(
																						new Token(0, "Pessoa")))))))))));
		
		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}
}
