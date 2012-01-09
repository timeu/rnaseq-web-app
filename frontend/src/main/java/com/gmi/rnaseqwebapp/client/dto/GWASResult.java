package com.gmi.rnaseqwebapp.client.dto;

import java.util.List;

public class GWASResult extends BaseModel {

	
	String name;
	String phenotype;
	String environment;
	String dataset;
	String transformation;
	List<Cofactor> cofactors;
	
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
	public String getTransformation() {
		return transformation;
	}
	public List<Cofactor> getCofactors() {
		return cofactors;
	}
	
}
