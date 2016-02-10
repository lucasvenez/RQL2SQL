package Tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;

import org.junit.Test;

import br.edu.ifsp.parser.ParseException;
import br.edu.ifsp.parser.RelationalQueryLanguage;

public class SemanticAnalysisTest {

	RelationalQueryLanguage rql;
	String args[] = new String[]{ "-a", "Pessoa.idPessoa:", "INTEGER", " PRIMARY KEY;", " Pessoa.nome:",
			" VARCHAR(45);", " Telefone.numero: ", "VARCHAR(17); ", "Telefone.idPessoa: ", "INTEGER", " REFERENCES ",
			"Pessoa.idPessoa; ", "\"teste.txt\""};

	@Test
	public void testSuccessfulProject() throws ParseException, IOException {
		args[12]="\"teste.txt\"";
		rql.main(args);
		assertEquals("expected", "actual");
	}

}
