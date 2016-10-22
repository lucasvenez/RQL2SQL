package translation;


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import br.edu.ifsp.parser.ParseException;
import br.edu.ifsp.parser.Parser;
import br.edu.ifsp.parser.Translation;

public class TranslationTest {

	String definition = null;
	Parser parser = null;

	@Before
	public void initialize() {
		definition = "People.idPeople:INTEGER;" + "People.name:VARCHAR;" + "People.age:INTEGER;"
				+ "Sale.idSale:INTEGER;" + "Sale.idPeople:INTEGER;" + "Sale.saleDate:VARCHAR;"
				+ "ItemSale.idSale:INTEGER;" + "ItemSale.idProduct:INTEGER;" + "ItemSale.quantity:INTEGER;"
				+ "Product.idProduct:INTEGER;" + "Product.name:VARCHAR;" + "Product.price:DOUBLE;"
				+ "Product.idCategory:INTEGER;" + "Category.idCategory:INTEGER;" + "Category.description:VARCHAR;"
				+ "Composition.idCompound:INTEGER;" + "Composition.idComponent:INTEGER;";
		parser = new Parser();
	}

	@Test
	public void projection() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "¢ idPeople (People);");
		assertEquals(0, translation.getSemanticErrors());

		translation = parser.translate(definition, "¢ nonExistent (People);");
		assertEquals(1, translation.getSemanticErrors());

		translation = parser.translate(definition, "¢ nonExistent (NonExistent);");
		assertEquals(2, translation.getSemanticErrors());
	}

	@Test
	public void selection() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "£ idPeople > 5 v idPeople < 10 (People);");
		assertEquals(0, translation.getSemanticErrors());

		translation = parser.translate(definition, "£ idPeople = nonExistent (People);");
		assertEquals(1, translation.getSemanticErrors());

		translation = parser.translate(definition, "£ idPeople = 10 (nonExistent);");
		assertEquals(2, translation.getSemanticErrors());

		// String Test
		translation = parser.translate(definition, "£ name = \"Jessica\" (People);");
		assertEquals(0, translation.getSemanticErrors());
		System.out.println(translation.getTranslation());
	}

	@Test
	public void rename() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "§ idPeople id (People);");
		assertEquals(0, translation.getSemanticErrors());
		
		translation = parser.translate(definition, "§ idNonExistent id (People);");
		assertEquals(1, translation.getSemanticErrors());

		translation = parser.translate(definition, "§ idPeople id (NonExistent);");
		assertEquals(2, translation.getSemanticErrors());
	}

	@Test
	public void transitiveClosure() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "<<Composition>>;");
		assertEquals(0, translation.getSemanticErrors());

		translation = parser.translate(definition, "<<People>>;");
		assertEquals(1, translation.getSemanticErrors());

		translation = parser.translate(definition, "<<nonExistent>>;");
		assertEquals(2, translation.getSemanticErrors());
	}
	
	/*---------------------------BINARY----------------------------*/
	@Test
	public void join() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "People [] Sale;");
		assertEquals(0, translation.getSemanticErrors());
		System.out.println(translation.getTranslation());
	}
}
