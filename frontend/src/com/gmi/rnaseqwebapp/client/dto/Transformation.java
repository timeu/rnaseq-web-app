package com.gmi.rnaseqwebapp.client.dto;

import java.util.List;


public class Transformation extends BaseModel {

	String name;
	String phenotype;
	String environment;
	String dataset;
	
	List<GWASResult> results;
	
	
	public String getName() {
		return name;
	}
	public String getPhenotype() {
		return phenotype;
	}
	public String getEnvironment() {
		return environment;
	}
	public String getDataset() {
		return dataset;
	}
	
	public List<GWASResult> getGWASResults() {
		return results;
	}
	
	public GWASResult getResultFromName(String resultName) {
		for (GWASResult result : results) {
			if (result.getName().equals(resultName))
				return result;
		}
		return null;
	}
	
}
