package com.gmi.rnaseqwebapp.client.dto;

import java.util.List;

public class Dataset extends BaseModel {
	String name;
	String phenotype;
	String environment;
	
	List<Transformation> transformations;
	
	public String getName() {
		return name;
	}
	public String getPhenotype() {
		return phenotype;
	}
	public String getEnvironment() {
		return environment;
	}
	
	
	public List<Transformation> getTransformations() {
		return transformations;
	}
}
