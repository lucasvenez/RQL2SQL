package objectTests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import br.edu.ifsp.parser.Token;
import br.edu.ifsp.semanticAnalysis.RelationCheck;
import br.edu.ifsp.symbolTable.*;
import br.edu.ifsp.syntacticTree.*;

public class RelationCheckUnitaryTest {

	static SymbolTable table;
	RelationCheck rc;

	@BeforeClass
	public static void setUp() {
		/* Definition of symbol Table */
		table = new SymbolTable("Pessoa.idPessoa:INTEGERPRIMARYKEY;" + "Pessoa.nome:VARCHAR;"
				+ "A.idA:INTEGERPRIMARYKEY;" + "B.idB:INTEGERPRIMARYKEY;" + "C.idC:INTEGERPRIMARYKEY;"
				+ "C.recursiveIdC:INTEGER;" + "Produto.idProduto:INTEGERPRIMARYKEY;" + "Produto.id:INTEGER;"
				+ "Venda.id:INTEGER PRIMARYKEY;" + "Telefone.idTelefone:INTEGERPRIMARYKEY;"
				+ "Telefone.idPessoa:INTEGER;");
	}

	@Test
	public void relationSemanticTest() {

		rc = new RelationCheck(table);

		/*
		 * Successful relation Equivalent query: Pessoa;
		 */
		ListNode root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new UnitaryOperationsNode(new RelationNode(new Token(0, "Pessoa")))))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Failed relation Equivalent query: Carro;
		 */
		root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new UnitaryOperationsNode(new RelationNode(new Token(0, "Carro")))))));

		assertEquals(1, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}

	@Test
	public void projectionSemanticTest() {

		rc = new RelationCheck(table);

		/*
		 * One attribute successful projection Equivalent query: ¢ idPessoa
		 * (Pessoa);
		 */
		ListNode root = new ListNode(new RelationalOperationsNode(new QueryNode(new ReadOnlyOperationsNode(
				new UnitaryOperationsNode(new ProjectNode(new ListNode(new AttributeNode(new Token(0, "idPessoa")))),
						new ReadOnlyOperationsNode(
								new UnitaryOperationsNode(new RelationNode(new Token(0, "Pessoa")))))))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * One attribute failed projection Equivalent query: ¢ nome (Telefone);
		 */
		root = new ListNode(new RelationalOperationsNode(new QueryNode(new ReadOnlyOperationsNode(
				new UnitaryOperationsNode(new ProjectNode(new ListNode(new AttributeNode(new Token(0, "nome")))),
						new ReadOnlyOperationsNode(
								new UnitaryOperationsNode(new RelationNode(new Token(0, "Telefone")))))))));

		assertEquals(1, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Two attribute successful projection Equivalent query: ¢ idPessoa,
		 * nome (Pessoa));
		 */
		ListNode list = new ListNode(new AttributeNode(new Token(0, "idPessoa")));
		list.add(new AttributeNode(new Token(0, "nome")));
		root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new UnitaryOperationsNode(new ProjectNode(list), new ReadOnlyOperationsNode(
						new UnitaryOperationsNode(new RelationNode(new Token(0, "Pessoa")))))))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Two attribute failed projection Equivalent query: ¢ idTelefone,
		 * numero (Pessoa));
		 */
		list = new ListNode(new AttributeNode(new Token(0, "idTelefone")));
		list.add(new AttributeNode(new Token(0, "numero")));
		root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new UnitaryOperationsNode(new ProjectNode(list), new ReadOnlyOperationsNode(
						new UnitaryOperationsNode(new RelationNode(new Token(0, "Pessoa")))))))));

		assertEquals(2, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Two chained projection Equivalent query: ¢ nome (¢ idPessoa
		 * (Pessoa));
		 */
		root = new ListNode(new RelationalOperationsNode(new QueryNode(new ReadOnlyOperationsNode(
				new UnitaryOperationsNode(new ProjectNode(new ListNode(new AttributeNode(new Token(0, "nome")))),
						new ReadOnlyOperationsNode(new UnitaryOperationsNode(
								new ProjectNode(new ListNode(
										new AttributeNode(new Token(0, "idPessoa")))),
								new ReadOnlyOperationsNode(
										new UnitaryOperationsNode(new RelationNode(new Token(0, "Pessoa")))))))))));

		assertEquals(1, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}

	@Test
	public void renameSemanticTest() {

		rc = new RelationCheck(table);

		/*
		 * One attribute successful rename Equivalent query: § idPessoa id
		 * (Pessoa);
		 */
		ListNode root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(new ReadOnlyOperationsNode(new UnitaryOperationsNode(
								new RenameNode(
										new ListNode(new RenameSetNode(new Token(0, "idPessoa"), new Token(0, "id")))),
								new ReadOnlyOperationsNode(
										new UnitaryOperationsNode(new RelationNode(new Token(0, "Pessoa")))))))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * One attribute failed rename Equivalent query: § numero num (Pessoa);
		 */
		root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(new ReadOnlyOperationsNode(new UnitaryOperationsNode(
								new RenameNode(
										new ListNode(new RenameSetNode(new Token(0, "numero"), new Token(0, "num")))),
								new ReadOnlyOperationsNode(
										new UnitaryOperationsNode(new RelationNode(new Token(0, "Pessoa")))))))));

		assertEquals(1, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Two attribute successful rename Equivalent query: § idPessoa id, nome
		 * nomePessoa (Pessoa));
		 */
		ListNode list = new ListNode(new RenameSetNode(new Token(0, "idPessoa"), new Token(0, "id")));
		list.add(new RenameSetNode(new Token(0, "nome"), new Token(0, "nomePessoa")));
		root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new UnitaryOperationsNode(new RenameNode(list), new ReadOnlyOperationsNode(
						new UnitaryOperationsNode(new RelationNode(new Token(0, "Pessoa")))))))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Two attribute failed rename Equivalent query: § numero num, id
		 * iidPessoa (Pessoa));
		 */
		list = new ListNode(new RenameSetNode(new Token(0, "numero"), new Token(0, "num")));
		list.add(new RenameSetNode(new Token(0, "id"), new Token(0, "idPessoa")));
		root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new UnitaryOperationsNode(new RenameNode(list), new ReadOnlyOperationsNode(
						new UnitaryOperationsNode(new RelationNode(new Token(0, "Pessoa")))))))));

		assertEquals(2, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Two chained renames Equivalent query: § id cod (§ idPessoa id
		 * (Pessoa));
		 */
		root = new ListNode(
				new RelationalOperationsNode(new QueryNode(new ReadOnlyOperationsNode(new UnitaryOperationsNode(
						new RenameNode(new ListNode(new RenameSetNode(new Token(0, "id"),
								new Token(0, "cod")))),
						new ReadOnlyOperationsNode(new UnitaryOperationsNode(
								new RenameNode(
										new ListNode(new RenameSetNode(new Token(0, "idPessoa"), new Token(0, "id")))),
								new ReadOnlyOperationsNode(
										new UnitaryOperationsNode(new RelationNode(new Token(0, "Pessoa")))))))))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}

	@Test
	public void chainedOperationsSemanticTest() {

		rc = new RelationCheck(table);

		/*
		 * One attribute successful rename Equivalent query: § idPessoa id (¢
		 * idPessoa (Pessoa));
		 */
		ListNode root = new ListNode(
				new RelationalOperationsNode(new QueryNode(new ReadOnlyOperationsNode(new UnitaryOperationsNode(
						new RenameNode(new ListNode(new RenameSetNode(new Token(0, "idPessoa"),
								new Token(0, "id")))),
						new ReadOnlyOperationsNode(new UnitaryOperationsNode(
								new ProjectNode(new ListNode(new AttributeNode(new Token(0, "idPessoa")))),
								new ReadOnlyOperationsNode(
										new UnitaryOperationsNode(new RelationNode(new Token(0, "Pessoa")))))))))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * One attribute successful rename Equivalent query: ¢ id (§ idPessoa id
		 * (Pessoa));
		 */
		root = new ListNode(new RelationalOperationsNode(new QueryNode(new ReadOnlyOperationsNode(
				new UnitaryOperationsNode(new ProjectNode(new ListNode(new AttributeNode(new Token(0, "id")))),
						new ReadOnlyOperationsNode(new UnitaryOperationsNode(
								new RenameNode(new ListNode(new RenameSetNode(new Token(0, "idPessoa"),
										new Token(0, "id")))),
								new ReadOnlyOperationsNode(
										new UnitaryOperationsNode(new RelationNode(new Token(0, "Pessoa")))))))))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}

	@Test
	public void transitiveCloseSemanticTest() {

		rc = new RelationCheck(table);

		/*
		 * Transitive closure with a simple relation Equivalent query: <<C>>;
		 */
		ListNode root = new ListNode(new RelationalOperationsNode(
				new QueryNode(new ReadOnlyOperationsNode(new UnitaryOperationsNode(new TransitiveCloseNode(new Token()),
						new RelationNode(new Token(0, "C")))))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Failure transitive closure with a simple relation Equivalent query:
		 * <<A>>;
		 */
		root = new ListNode(new RelationalOperationsNode(
				new QueryNode(new ReadOnlyOperationsNode(new UnitaryOperationsNode(new TransitiveCloseNode(new Token()),
						new RelationNode(new Token(0, "A")))))));

		assertEquals(1, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}
}
