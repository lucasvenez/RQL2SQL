package translation;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.edu.ifsp.parser.ParseException;
import br.edu.ifsp.parser.Parser;
import br.edu.ifsp.parser.Translation;

public class MonographyExamples {

	String definition = null;
	Parser parser = null;
	String translations = "";

	@Before
	public void initialize() {
		definition = "Table.idTable:INTEGER;Table.description:VARCHAR;";
		parser = new Parser();
	}
	
	@Ignore
	public void relation() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "Table;");
		translation.printDotTree();
		translation.printSyntacticTree();
		translation.exportDotTree();
	}

	@Ignore
	public void simple() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "¢ idTable (Table);");
		translation.exportDotTree();
	}
	
	@Ignore
	public void chained() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "¢ description (£ idTable = 1 (Table));");
		translation.exportDotTree();
	}
	
	@Test
	public void binary() throws IOException, ParseException {
		String definition2 = "Composicao.idComponente:INTEGER;Composicao.idComposicao:INTEGER;";
		Translation translation = parser.translate(definition2, "<<Composicao>>;");
		System.out.println(translation.getTranslation());
	}
}
