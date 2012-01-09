package com.gmi.rnaseqwebapp.client.dto;

import java.util.List;


public class Accessions {
	List<Accession> accessions;
	int count;
	
	public List<Accession> getAccessions() {
		return accessions;
	}
	
	public int getCount() {
		return count;
	}
}