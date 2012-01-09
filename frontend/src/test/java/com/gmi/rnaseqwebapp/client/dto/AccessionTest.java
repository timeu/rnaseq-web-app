package com.gmi.rnaseqwebapp.client.dto;

import java.util.Date;





public class AccessionTest extends AbstractReaderTest{
	
	String name = "Accession";
	Double latitude = 69.12312;
	Double longitude = 10.5222;
	Integer accession_id = 1;
	Date date = new Date(1290380400000l);
	String country = "SWE";
	String dataset = "both";
	String collector = "Magnus Nordborg";
	
	protected String getAccessionJson() {
		
		String accession_json = "{\"collection_date\":\""+date.toString()+"\",\"name\":\""+name+"\",\"latitude\":"+latitude+",\"accession_id\":"+accession_id+",\"country\":\""+country+"\",\"collector\":\""+collector+"\",\"longitude\":"+longitude+",\"dataset\":\""+dataset+"\"}";
		return accession_json;
	}
	
	public void testAccession() {
		
		Accession accession = accessionReader.read(getAccessionJson());
		assertAccession(accession);
	}
	
	protected void assertAccession(Accession accession) {
		assertEquals(accession_id,accession.getAccessionId());
		assertEquals(latitude,accession.getLatitude());
		assertEquals(longitude,accession.getLongitude());
		assertEquals(country,accession.getCountry());
		assertEquals(dataset,accession.getDataset());
		assertEquals(collector,accession.getCollector());
		assertDate(date,accession.getCollectionDate());
	}
	
	public void testAccessions() {
		String accession_json = getAccessionJson();
		String accessions_json = "{\"count\":2,\"accessions\":["+accession_json+","+accession_json+"]}";
		Accessions accessions = accessionsReader.read(accessions_json);
		assertEquals(2,accessions.getCount());
		assertNotNull(accessions.getAccessions());
		assertEquals(2, accessions.getAccessions().size());
		for (int i =0;i<2;i++) {
			assertAccession(accessions.getAccessions().get(i));
		}
	}
	
}
