package jena;

import jena.base.OISOntoFactory;
import jena.base.OISReasoner;
import jena.base.Printer;

public class App {

	public static void main(String[] args) {
		Printer.print(OISOntoFactory.getRandomModel(3,2,5,3),"firstLvlOnto");
		Printer.print(OISReasoner.getInferredModel(
				                     OISOntoFactory.getRandomModel(3,2,5,3)),"firstLvlOnto_inferred");
	}
}
