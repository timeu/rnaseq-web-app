package com.gmi.rnaseqwebapp.client.dto;

public class GxEResult extends BaseModel {
	
	public enum TYPE {Full,Genetic,Environ,Combined};
	
	TYPE type;
	String phenotype;
	
	public GxEResult() {
		
	}
	public GxEResult(String phenotype,TYPE type) {
		this.type = type;
		this.phenotype = phenotype;
	}
	
	public String getPhenotype() {
		return phenotype;
	}
	
	public TYPE getType() {
		return type;
	}
	
}
