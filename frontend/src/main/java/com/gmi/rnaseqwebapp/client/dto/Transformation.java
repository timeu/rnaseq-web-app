package com.gmi.rnaseqwebapp.client.dto;

import java.util.List;


public class Transformation extends BaseModel {

	String name;
	String phenotype;
	String environment;
	String dataset;
	List<Cofactor> cofactors;
	List<GWASResult> results;
	List<CisVsTransStat> tssUpstream;
	List<CisVsTransStat> radius;
	
	
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
	
	public List<Cofactor> getCofactors() {
		return cofactors;
	}
	
	public List<Cofactor> getCofactors(int step) {
		if (step > cofactors.size())
			return cofactors;
		return cofactors.subList(0, step);
	}
	
	public List<CisVsTransStat> getTssUpstream()  {
		return tssUpstream;
	}
	
	public List<CisVsTransStat> getRadius()  {
		return radius;
	}
}
