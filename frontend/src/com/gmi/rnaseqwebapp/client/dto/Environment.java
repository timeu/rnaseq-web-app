package com.gmi.rnaseqwebapp.client.dto;

import java.util.List;

public class Environment extends BaseModel {
	
	public enum TYPES {T16C,T10C};
	
	String name;
	String phenotype;
	
	List<Dataset> datasets;
	
	public String getName() {
		return name;
	}
	
	public String getPhenotype() {
		return phenotype;
	}
	
	
	public List<Dataset> getDatasets() {
		return datasets;
	}
}
