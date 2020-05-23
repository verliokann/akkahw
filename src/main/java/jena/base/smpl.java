package jena.base;

public class smpl {

	/*package ru.nntu.codegenerator.jena;

	import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.net.URLEncoder;
	import java.nio.file.Path;
	import java.nio.file.Paths;
	import java.util.Collection;
	import java.util.Iterator;
	import java.util.List;
	import java.util.Map;
	import java.util.Objects;
	import java.util.Set;

	import org.apache.jena.rdf.model.Property;
	import org.apache.jena.datatypes.xsd.XSDDatatype;
	import org.apache.jena.ontology.DataRange;
	import org.apache.jena.ontology.DatatypeProperty;
	import org.apache.jena.ontology.Individual;
	import org.apache.jena.ontology.ObjectProperty;
	import org.apache.jena.ontology.OntClass;
	import org.apache.jena.ontology.OntModel;
	import org.apache.jena.ontology.OntModelSpec;
	import org.apache.jena.ontology.Ontology;
	import org.apache.jena.ontology.impl.DataRangeImpl;
	import org.apache.jena.rdf.model.Literal;
	import org.apache.jena.rdf.model.ModelFactory;
	import org.apache.jena.rdf.model.Resource;
	import org.apache.jena.rdf.model.ResourceFactory;
	import org.apache.jena.rdf.model.Statement;
	import org.apache.jena.vocabulary.DCTerms;
	import org.apache.jena.vocabulary.OWL;
	import org.apache.jena.vocabulary.OWL2;
	import org.apache.jena.vocabulary.VCARD;
	import org.apache.jena.vocabulary.XSD;

	import ru.nntu.codegenerator.domain.Generator;
	import ru.nntu.codegenerator.jena.model.GeneratorModel;
	import ru.nntu.codegenerator.jena.model.ParameterModel;

	public class SourceModelController {
		static OntModel model;
		static Resource source;
		static OntClass classGenerator;
		static OntClass classParameter;
		static OntClass classTemplate;
		static OntClass classDomain;
		
		static Property parameterProperty;
		static Property templateProperty;
		static ObjectProperty hasStateInProperty;
		static ObjectProperty hasStateOutProperty;
		static ObjectProperty hasDomainProperty;
		static ObjectProperty hasTemplateProperty;
		
		static String SOURCE = "http://www.w3.org/2002/07/owl";
		static String sourceURI = "http://vst.science.jena/source";
		static String NS = SOURCE + "#";
		
		public static void createGenerator() {
			
		}
		
		public static void createSourceModel(Map<String, Map<String, String>> sourceMapForConfigParameters) {
			model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
			model.setNsPrefix("owl", OWL.NS);

			source = model.createResource(sourceURI);
			OntClass classSource = model.createClass(sourceURI + "/Source");
			OntClass classParameter = model.createClass(sourceURI+ "/Parameter");
			OntClass classDomain = model.createClass(sourceURI+ "/Domain");
			
			Property parameterProperty = model.createDatatypeProperty(sourceURI + "/Restriction");
			ObjectProperty hasDomainProperty = model.createObjectProperty(sourceURI + "/hasDomain");
			ObjectProperty hasStateOutProperty = model.createObjectProperty(sourceURI + "/hasStateOut");

			Set<String> keys = sourceMapForConfigParameters.keySet();
			Iterator<String> iterator = keys.iterator();
			
			while (iterator.hasNext()) {
				String template = iterator.next();
				
				Individual ind = classSource.createIndividual(sourceURI + "/Source/Source_" + (template.replace(" ", "_")));
				
				
				Map<String, String> parameters = sourceMapForConfigParameters.get(template);
				for(Map.Entry<String, String> param : parameters.entrySet()) {
					System.out.println("-------------");
					System.out.println(param.getKey() + " - " + param.getValue());
					
						if (Objects.nonNull(param.getValue()) && Objects.nonNull(param.getKey())) {
							Individual indParameter = classParameter.createIndividual(sourceURI + "/" + param.getKey());
							Individual indDomain = classDomain.createIndividual(sourceURI + "/Domain_" + param.getKey());
//							indDomain.addProperty(parameterProperty, param.getKey());
							indParameter.addProperty(hasDomainProperty, indDomain);
							ind.addProperty(hasStateOutProperty, indParameter);
							DatatypeProperty dataTypeProperty = model.createDatatypeProperty(sourceURI+"/Source_" + param.getKey());
							dataTypeProperty.setDomain(classSource);
							dataTypeProperty.setRange(XSD.xstring);
							
							Literal sourceValue = model.createTypedLiteral(param.getValue(), XSDDatatype.XSDstring);
							Statement sourceStatement = model.createStatement(indParameter, dataTypeProperty, sourceValue);
							model.add(sourceStatement);
						}
				}
			}

			source.addProperty(DCTerms.source, classSource);
		}
		
		
		public static void createGeneratorModel(List<GeneratorModel> generatorModelList) {
			if(Objects.isNull(model)) {
				model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
				source = model.createResource(sourceURI);
				classGenerator = model.createClass(sourceURI+ "/Generator");
				classParameter = model.createClass(sourceURI+ "/Parameter");
				classTemplate = model.createClass(sourceURI+ "/Template");
				classDomain = model.createClass(sourceURI+ "/Domain");
				
				parameterProperty = model.createDatatypeProperty(sourceURI + "/Restriction");
				templateProperty = model.createDatatypeProperty(sourceURI + "/TemplateProperty");
				hasStateInProperty = model.createObjectProperty(sourceURI + "/hasStateIn");
				hasStateOutProperty = model.createObjectProperty(sourceURI + "/hasStateOut");
				hasDomainProperty = model.createObjectProperty(sourceURI + "/hasDomain");
				hasTemplateProperty = model.createObjectProperty(sourceURI + "/hasTemplate");
			}

			Iterator<GeneratorModel> iterator = generatorModelList.iterator();
			
			while (iterator.hasNext()) {
				GeneratorModel generator = iterator.next();
				createInterfaceForState(generator.getNameGenerator(), generator.getParametersStateIn(),
						generator.getParametersStateOut(), generator.getTemplateContent());
			}
			
			saveFile(model);
		}
		
		private static void createInterfaceForState(String generatorName, List<ParameterModel> paramsIn,
				List<ParameterModel> paramsOut, String contentTemplate) {
			
			Individual indTemplate = classTemplate.createIndividual(sourceURI + "/Template_" + generatorName);
			
			if (Objects.nonNull(contentTemplate) && !contentTemplate.isEmpty()) {
				indTemplate.addProperty(templateProperty, contentTemplate);
			}
			
			Individual indGenerator = classGenerator.createIndividual(sourceURI + "/" + generatorName);
			
			if (Objects.nonNull(paramsIn)) {
				indGenerator = generateProperties(indGenerator, generatorName, paramsIn, hasStateInProperty);
			}
			
			if (Objects.nonNull(paramsOut)) {
				indGenerator = generateProperties(indGenerator, generatorName, paramsOut, hasStateOutProperty);
			}

			indGenerator.addProperty(hasTemplateProperty, indTemplate);
			
			source.addProperty(DCTerms.source, classGenerator);
			source.addProperty(DCTerms.source, classParameter);
		}
		
		private static Individual generateProperties(Individual indGenerator, String generatorName, List<ParameterModel> params, ObjectProperty property) {
			Iterator<ParameterModel> paramsIterator = params.iterator();
			
			while(paramsIterator.hasNext()) {
				ParameterModel param = paramsIterator.next();
				Individual indParameter = classParameter.createIndividual(sourceURI + "/" + generatorName + "_" + param.getParameterName());
				if (Objects.nonNull(param.getDomain()) && Objects.nonNull(param.getDomain().getRestriction())) {
					Individual indDomain = classDomain.createIndividual(sourceURI + "/" + param.getDomain().getNameModel());
					indDomain.addProperty(parameterProperty, param.getDomain().getRestriction());
					indParameter.addProperty(hasDomainProperty, indDomain);
				}
				
				indGenerator.addProperty(property, indParameter);
			}
			return indGenerator;
		}
		
		private static void saveFile(OntModel model) {
			FileWriter out;
			try {
				Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
				String s = currentRelativePath.toString();
				System.out.println("Current relative path is: " + s);
				out = new FileWriter( currentRelativePath.toAbsolutePath() + File.separator +  "PostGrewDB"+ File.separator +"source.owl" );
				model.write( out, "RDF/XML-ABBREV" );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}*/

	
	
}
