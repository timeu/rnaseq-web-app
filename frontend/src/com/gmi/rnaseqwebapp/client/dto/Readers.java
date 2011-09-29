package com.gmi.rnaseqwebapp.client.dto;

import name.pehl.piriti.json.client.JsonReader;

public interface Readers {

	public interface PhenotypeReader extends JsonReader<Phenotype>{}

	public interface PhenotypesReader extends JsonReader<Phenotypes>{}

	public interface AccessionReader extends JsonReader<Accession> {}
	
	public interface AccessionsReader extends JsonReader<Accessions> {}
}
