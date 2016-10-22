package objectTests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import br.edu.ifsp.parser.Token;
import br.edu.ifsp.semanticAnalysis.RelationCheck;
import br.edu.ifsp.symbolTable.*;
import br.edu.ifsp.syntacticTree.*;

public class RelationCheckBinaryTest {

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
	public void unionSemanticTest() {
		rc = new RelationCheck(table);

		/*
		 * Simple union operation Equivalent query: A v B;
		 */
		BinarySetNode set = new BinarySetNode();
		set.addFirstRelation(new RelationNode(new Token(0, "A")));
		set.addSecondRelation(new RelationNode(new Token(0, "B")));

		ListNode root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new BinaryOperationsNode(new UnionNode(new Token(0, "v")), set)))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Failure union operation Equivalent query: Pessoa v B;
		 */
		set = new BinarySetNode();
		set.addFirstRelation(new RelationNode(new Token(0, "Pessoa")));
		set.addSecondRelation(new RelationNode(new Token(0, "B")));

		root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new BinaryOperationsNode(new UnionNode(new Token(0, "v")), set)))));

		assertEquals(1, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}

	@Test
	public void intersectSemanticTest() {

		rc = new RelationCheck(table);

		/*
		 * Simple intersection operation Equivalent query: A ^ B;
		 */
		BinarySetNode set = new BinarySetNode();
		set.addFirstRelation(new RelationNode(new Token(0, "A")));
		set.addSecondRelation(new RelationNode(new Token(0, "B")));

		ListNode root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new BinaryOperationsNode(new IntersectionNode(new Token(0, "^")), set)))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Failure intersection operation Equivalent query: Pessoa ^ B;
		 */
		set = new BinarySetNode();
		set.addFirstRelation(new RelationNode(new Token(0, "Pessoa")));
		set.addSecondRelation(new RelationNode(new Token(0, "B")));

		root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new BinaryOperationsNode(new IntersectionNode(new Token(0, "^")), set)))));

		assertEquals(1, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}

	@Test
	public void differenceSemanticTest() {

		rc = new RelationCheck(table);

		/*
		 * Simple difference operation Equivalent query: A - B;
		 */
		BinarySetNode set = new BinarySetNode();
		set.addFirstRelation(new RelationNode(new Token(0, "A")));
		set.addSecondRelation(new RelationNode(new Token(0, "B")));

		ListNode root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new BinaryOperationsNode(new DifferenceNode(new Token(0, "-")), set)))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Failure difference operation Equivalent query: Pessoa - B;
		 */
		set = new BinarySetNode();
		set.addFirstRelation(new RelationNode(new Token(0, "Pessoa")));
		set.addSecondRelation(new RelationNode(new Token(0, "B")));

		root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new BinaryOperationsNode(new DifferenceNode(new Token(0, "-")), set)))));

		assertEquals(1, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}

	@Test
	public void crossJoinSemanticTest() {

		rc = new RelationCheck(table);

		/*
		 * Simple difference operation Equivalent query: A x B;
		 */
		BinarySetNode set = new BinarySetNode();
		set.addFirstRelation(new RelationNode(new Token(0, "A")));
		set.addSecondRelation(new RelationNode(new Token(0, "B")));

		ListNode root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new BinaryOperationsNode(new CrossJoinNode(new Token(0, "x")), set)))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}

	@Test
	public void divisionSemanticTest() {

		rc = new RelationCheck(table);

		/*
		 * Simple difference operation Equivalent query: Produto / Venda;
		 */
		BinarySetNode set = new BinarySetNode();
		set.addFirstRelation(new RelationNode(new Token(0, "Produto")));
		set.addSecondRelation(new RelationNode(new Token(0, "Venda")));

		ListNode root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new BinaryOperationsNode(new DivisionNode(new Token(0, "/")), set)))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Failure difference operation Equivalent query: Pessoa / A;
		 */
		set = new BinarySetNode();
		set.addFirstRelation(new RelationNode(new Token(0, "Pessoa")));
		set.addSecondRelation(new RelationNode(new Token(0, "A")));

		root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new BinaryOperationsNode(new DivisionNode(new Token(0, "/")), set)))));

		assertEquals(1, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();
	}

	@Test
	public void joinSemanticTest() {

		rc = new RelationCheck(table);

		/*
		 * Natural join operation Equivalent query: Pessoa [] Telefone;
		 */
		BinarySetNode set = new BinarySetNode();
		set.addFirstRelation(new RelationNode(new Token(0, "Pessoa")));
		set.addSecondRelation(new RelationNode(new Token(0, "Telefone")));

		ListNode root = new ListNode(new RelationalOperationsNode(new QueryNode(
				new ReadOnlyOperationsNode(new BinaryOperationsNode(new JoinNode(new Token(0, "[]")), set)))));

		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Inner join operation Equivalent query: Pessoa [idPessoa = idPessoa]
		 * Telefone;
		 */
		set = new BinarySetNode();
		set.addFirstRelation(new RelationNode(new Token(0, "Pessoa")));
		set.addSecondRelation(new RelationNode(new Token(0, "Telefone")));

		root = new ListNode(
				new RelationalOperationsNode(
						new QueryNode(new ReadOnlyOperationsNode(
								new BinaryOperationsNode(
										new JoinNode(new Token(0, "[]"),
												new LogicalSentenceNode(new ConditionalSentenceNode(
														new ComparisonSentenceNode(new ComparisonOperatorNode(
																new Token(0, "="),
																new InstanceofSentenceNode(new AdditionSentenceNode(
																		new MultiplicationSentenceNode(new FactorNode(
																				null, new Token(69, "idPessoa"))))),
																new InstanceofSentenceNode(new AdditionSentenceNode(
																		new MultiplicationSentenceNode(new FactorNode(
																				null, new Token(69, "idPessoa")))))))))),
										set)))));
		
		assertEquals(0, rc.semanticAnalysis(root));
		rc.clearSemanticErrors();

		/*
		 * Failure Inner join operation Equivalent query: Pessoa [idCarro =
		 * idTelefone] Telefone;
		 */
		/*
		 * set = new BinarySetNode(); set.addFirstRelation(new RelationNode(new
		 * Token(0, "Pessoa"))); set.addSecondRelation(new RelationNode(new
		 * Token(0, "Telefone")));
		 * 
		 * root = new ListNode( new RelationalOperationsNode( new QueryNode( new
		 * ReadyOnlyOperationsNode( new BinaryOperationsNode( new JoinNode(new
		 * Token(0, "[]"), new LogicalSentenceNode( new ConditionalSentenceNode(
		 * new ComparisonSentenceNode( new ComparisonOperatorNode( new Token(0,
		 * "="), new InstanceofSentenceNode( new AdditionSentenceNode( new
		 * MultiplicationSentenceNode( new FactorNode(null, new Token(0,
		 * "idCarro"))))), new InstanceofSentenceNode( new AdditionSentenceNode(
		 * new MultiplicationSentenceNode( new FactorNode(null, new Token(0,
		 * "idTelefone")))))))))), set)))));
		 * 
		 * assertEquals(1, rc.semanticAnalysis(root)); rc.clearSemanticErrors();
		 */
	}
}
