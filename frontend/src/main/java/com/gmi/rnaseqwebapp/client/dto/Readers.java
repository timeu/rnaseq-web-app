package com.gmi.rnaseqwebapp.client.dto;

import name.pehl.piriti.json.client.JsonReader;

public interface Readers {

	public interface PhenotypeReader extends JsonReader<Phenotype>{}

	public interface PhenotypesReader extends JsonReader<Phenotypes>{}

	public interface AccessionReader extends JsonReader<Accession> {}
	
	public interface AccessionsReader extends JsonReader<Accessions> {}
	
	public interface DatasetReader extends JsonReader<Dataset> {}
	
	public interface EnvironmentReader extends JsonReader<Environment> {}
	
	public interface TransformationReader extends JsonReader<Transformation> {}
	
	public interface GWASResultReader extends JsonReader<GWASResult> {}
	
	public interface CofactorReader extends JsonReader<Cofactor> {}
}
