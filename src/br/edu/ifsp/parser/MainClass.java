package br.edu.ifsp.parser;

import java.io.IOException;

public class MainClass {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("2 arguments are required for the translation");
			System.exit(0);
		}
		Parser parser = new Parser();
		Translation translation = null;
		try {
			translation = parser.translate(args[0], args[1]);
			System.out.println("**--Section--**");
		} catch (IOException | ParseException e) {
			System.out.println("**--Section--**");
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}
		if (translation != null) {
			System.out.println("**--Section--**");
			if (!translation.getSemanticErrorsDescription().equals(""))
				System.out.println(translation.getSemanticErrorsDescription());
			System.out.println("**--Section--**");
			System.out.println(translation.getTranslation());
		} else {
			System.out.println("**--Section--**");
			System.out.println("**--Section--**");
		}
	}
}
