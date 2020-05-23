package jena.base;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;

public class OISReasoner {

	/* ------------------------- Обработка модели ризонером ---------------------------*/	
	public static InfModel getInferredModel(OntModel model) {    
		String rules = "[rule1: (?b http://vst.science.jena/OIS/source/hasProperty ?p) "
				             + "(?p http://vst.science.jena/OIS/source/hasDomain ?d) -> "
				             + "(?b http://vst.science.jena/OIS/source/hasDomain ?d)]";
		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		reasoner.setDerivationLogging(true);
		InfModel inferredModel = ModelFactory.createInfModel(reasoner, model);
		return inferredModel;	    
	}
}
