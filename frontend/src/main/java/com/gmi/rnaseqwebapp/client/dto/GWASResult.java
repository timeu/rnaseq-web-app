package com.gmi.rnaseqwebapp.client.dto;

import java.util.ArrayList;
import java.util.List;

public class GWASResult extends BaseModel {

	
	String name;
	String phenotype;
	String environment;
	String dataset;
	String transformation;
	Integer step = 0;
	//Cofactor cofactor;
	//private List<Cofactor> cofactors =null;
	
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
	
	public Integer getStep() {
		return step;
	}
	/*public List<Cofactor> getCofactors() {
		if (cofactors == null) {
			cofactors = new ArrayList<Cofactor>();
			if (cofactor != null) {
				for (GWASResult result: getTr)
			}
		}
		return cofactors;
	}*/
	
	
}
