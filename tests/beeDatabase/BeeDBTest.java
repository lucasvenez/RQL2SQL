package beeDatabase;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;

import br.edu.ifsp.parser.ParseException;
import br.edu.ifsp.parser.Parser;
import br.edu.ifsp.parser.Translation;

public class BeeDBTest {

	String definition = null;
	Parser parser = null;
	String translations = "";

	@Before
	public void initialize() {
		definition = "Hive.idHive:INTEGER;" + "Hive.positionX:INTEGER;" + "Hive.positionY:INTEGER;"
				+ "Hive.honey:DOUBLE;" + "Comb.idComb:INTEGER;" + "Comb.idHive:INTEGER;" + "Job.idJob:INTEGER;"
				+ "Job.description:VARCHAR;" + "Job.salary:DOUBLE;" + "Bee.idBee:INTEGER;" + "Bee.name:VARCHAR;"
				+ "Bee.age:INTEGER;" + "Bee.honey:DOUBLE;" + "Bee.idJob:INTEGER;" + "Bee.idCom:INTEGER;"
				+ "Hierarchy.idInferior:INTEGER;" + "Hierarchy.idSuperior:INTEGER;" + "Flower.idFlower:INTEGER;"
				+ "Flower.positionX:INTEGER;" + "Flower.positionY:INTEGER;" + "Pollen.idFlower:INTEGER;"
				+ "Pollen.idBee:INTEGER;";
		parser = new Parser();
	}

	/*@Test
	public void question1() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "Bee;");
		assertEquals(0, translation.getSemanticErrors());
	}

	@Test
	public void question2() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "¢ idBee, name, honey (Bee);");
		assertEquals(0, translation.getSemanticErrors());
	}

	@Test
	public void question3() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "£ salary > 10 (Job);");
		assertEquals(0, translation.getSemanticErrors());
	}

	@Test
	public void question4() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "§ positionX latitude, positionY longitude (Hive);");
		assertEquals(0, translation.getSemanticErrors());
	}

	@Test
	public void question5() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "£ description = \"Transporter\" (Bee [] Job);");
		assertEquals(0, translation.getSemanticErrors());
	}

	@Test
	public void question6() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "(¢ idHive, positionX, positionY (Hive)) v Flower;");
		assertEquals(0, translation.getSemanticErrors());
	}

	@Test
	public void question7() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "Bee x Job;");
		assertEquals(0, translation.getSemanticErrors());
	}

	@Test
	public void question8() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "(¢ idBee (Bee)) ^ (¢ idHive (Hive));");
		assertEquals(0, translation.getSemanticErrors());
	}

	@Test
	public void question9() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "(¢ idHive (Hive)) - (¢ idBee (Bee));");
		assertEquals(0, translation.getSemanticErrors());
	}*/

	@Test
	public void question10() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "Pollen / (¢ idBee (Bee));");
		assertEquals(0, translation.getSemanticErrors());
		System.out.println(translation.getTranslation());
	}

	/*@Test
	public void question11() throws IOException, ParseException {
		Translation translation = parser.translate(definition, "<<Hierarchy>>;");
		assertEquals(0, translation.getSemanticErrors());
	}*/
}
