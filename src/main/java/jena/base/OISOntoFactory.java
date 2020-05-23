package jena.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.XSD;

public class OISOntoFactory {
	static String SOURCE = "http://www.w3.org/2002/07/owl";
	static String sourceURI = "http://vst.science.jena/OIS/source";
	static String NS = SOURCE + "#";
	
	static OntModel model;
	static Resource source;
	static OntClass classBlock;
	static OntClass classProperty;
	static OntClass classDomain;
	
	
	static DatatypeProperty hasName;	
	static ObjectProperty hasProperty;
	static ObjectProperty hasDomain;
	static List<Individual> arr = new ArrayList<>(); //
		
    /* ------------------------------------ Генерируем модель -------------------------------------------------*/   	
	public static OntModel getRandomModel(int amOfBlocks, int minAmOfProp, int maxAmOfProp, int amOfDomains) {
	    // Создание и инициализация модели
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		model.setNsPrefix("owl", OWL.NS);	
		source = model.createResource(sourceURI);
		
		// Создание и инициализация классов		
		classBlock = model.createClass(sourceURI + "/Block");
		classProperty = model.createClass(sourceURI+ "/Property");
		classDomain = model.createClass(sourceURI+ "/Domain");
		
		// Создание и инициализация свойств и объектных свойств
		hasName = model.createDatatypeProperty(sourceURI + "/hasName");
		hasName.setDomain(classProperty);
		hasName.setRange(XSD.xstring);
				
		hasProperty = model.createObjectProperty(sourceURI + "/hasProperty");		
		hasProperty.addDomain(classBlock); 
		hasProperty.addRange(classProperty); 
		
		hasDomain = model.createObjectProperty(sourceURI + "/hasDomain");
		hasDomain.addDomain(classProperty);
		hasDomain.addDomain(classBlock);
		hasDomain.addRange(classDomain); 
		
		//Создание индивидов доменов
	    for (int i=0; i<amOfDomains; i++) {
	    	arr.add(model.createIndividual(sourceURI + "/dom"+i, classDomain));
	    }	
		
	    // Создание индивидов блоков, свойств блоков
	    // Создание связей блок-свойство и свойство-домен
	    for (int i=0; i<amOfBlocks; i++) {
	    	Individual block = model.createIndividual(sourceURI + "/block"+i, classBlock);
	    	for (int j=0; j<(new Random().nextInt(maxAmOfProp))+minAmOfProp; j++) {
	    		block.addProperty(hasProperty, 
	    				          model.createIndividual(sourceURI + "/prop"+i+"_"+j, classProperty)
	    				                                .addProperty(hasDomain, arr.get((new Random().nextInt(amOfDomains))))
	    				         
	    				         );
	    	}
	    }
	        
	  return model;
	}  
	
	//Создание правил логического вывода
}
