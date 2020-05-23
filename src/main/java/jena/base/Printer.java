package jena.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;

public class Printer {
	/* ------------------------- Пишем модель в файл ----------------------------------*/
    public static void print(InfModel model, String name) {
    	saveFile(model, name);	
    } 
    
	/*
	 * public static void print(InfModel model, String name) { saveFile(model,
	 * name); }
	 */
	
	private static void saveFile(InfModel model, String name) {
		FileWriter out;
		try {
			Path currentRelativePath = Paths.get("src/main/resources/").toAbsolutePath();
			String s = currentRelativePath.toString();
			System.out.println("Current relative path is: " + s);
			out = new FileWriter( currentRelativePath.toAbsolutePath() + File.separator +  "owls"+ File.separator +name+".owl" );
			model.write( out, "RDF/XML-ABBREV" );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * private static void saveFile(InfModel model, String name) { FileWriter out;
	 * try { Path currentRelativePath =
	 * Paths.get("src/main/resources/").toAbsolutePath(); String s =
	 * currentRelativePath.toString();
	 * System.out.println("Current relative path is: " + s); out = new FileWriter(
	 * currentRelativePath.toAbsolutePath() + File.separator + "owls"+
	 * File.separator +name+".owl" ); model.write( out, "RDF/XML-ABBREV" ); } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); } }
	 */
}
