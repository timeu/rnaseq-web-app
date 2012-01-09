package com.gmi.rnaseqwebapp.client.dto;

import java.util.Date;

public class PhenotypeTest extends AbstractReaderTest {

	Integer phenotype_id = 1;
	String name = "Phenotype";
	Integer chr = 1;
	Integer start = 100;
	Integer end = 100;
	
	
	protected String getPhenotypeJson() {
		
		String accession_json = "{\"phenotype_id\":"+phenotype_id+",\"name\":\""+name+"\",\"chr\":"+chr+",\"start\":"+start+",\"end\":"+end+"}";
		return accession_json;
	}
	
	public void testPhenotype() {
		
		Phenotype phenotype = phenotypeReader.read(getPhenotypeJson());
		assertPhenotype(phenotype);
	}
	
	protected void assertPhenotype(Phenotype accession) {
		assertEquals(phenotype_id,accession.getPhenotypeId());
		assertEquals(name,accession.getName());
		assertEquals(chr,accession.getChr());
		assertEquals(start,accession.getStart());
		assertEquals(end,accession.getEnd());
	}
	
	public void testPhenotypes() {
		String phenotype_json = getPhenotypeJson();
		String phenotypes_json = "{\"count\":2,\"phenotypes\":["+phenotype_json+","+phenotype_json+"]}";
		Phenotypes phenotypes = phenotypesReader.read(phenotypes_json);
		assertEquals(2,phenotypes.getCount());
		assertNotNull(phenotypes.getPhenotypes());
		assertEquals(2, phenotypes.getPhenotypes().size());
		for (int i =0;i<2;i++) {
			assertPhenotype(phenotypes.getPhenotypes().get(i));
		}
	}
}
