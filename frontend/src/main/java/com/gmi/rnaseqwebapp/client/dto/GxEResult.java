package com.gmi.rnaseqwebapp.client.dto;

public class GxEResult extends BaseModel {
	
	public enum TYPE {Full,Genetic,Environ,Combined};
	
	TYPE type;
	String dataset;
	String phenotype;
	
	public GxEResult() {
		
	}
	public GxEResult(String dataset,TYPE type) {
		this.type = type;
		this.dataset = dataset;
	}
	
	public String getDataset() {
		return dataset;
	}
	
	public String getPhenotype() {
		return phenotype;
	}
	
	public TYPE getType() {
		return type;
	}
	
}
