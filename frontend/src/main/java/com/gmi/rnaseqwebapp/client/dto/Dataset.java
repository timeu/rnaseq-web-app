package com.gmi.rnaseqwebapp.client.dto;

import java.util.ArrayList;
import java.util.List;

public class Dataset extends BaseModel {
	public enum TYPE {mRNA,bisulfite};
	String phenotype;
	TYPE type;
	Float maxScore10C;
	Float maxScore16C;
	Float maxScoreFull;
	Float pseudoHeritability10C;
	Float pseudoHeritability16C;
	List<Environment> environments;
	List<GxEResult> gxeResults = new ArrayList<GxEResult>();
	
	public TYPE getType() {
		return type;
	}
	public String getPhenotype() {
		return phenotype;
	}
	
	public List<Environment> getEnvironments() {
		return environments;
	}
	
	public Float getMaxScore10C() {
		return maxScore10C;
	}
	public Float getMaxScore16C() {
		return maxScore16C;
	}
	public Float getMaxScoreFull() {
		return maxScoreFull;
	}
	public Float getPseudoHeritability10C() {
		return pseudoHeritability10C;
	}
	public Float getPseudoHeritability16C() {
		return pseudoHeritability16C;
	}
	
	public List<GxEResult> getGxEResults()  {
		return gxeResults;
	}
	
	public Environment getEnvironmentFromName(String name) {
		for (Environment environment : getEnvironments()) 
		{
			if (name != null && environment.getName().equals(name))
				return environment;
		}
		return null;
	}
	
	public GxEResult getGxEResultFromType(GxEResult.TYPE type) {
		for (GxEResult gxeResult : getGxEResults()) 
		{
			if (type != null && type == gxeResult.type)
				return gxeResult;
		}
		return null;
	}
}
