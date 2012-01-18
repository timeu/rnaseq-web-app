package com.gmi.rnaseqwebapp.client.dto;

import com.google.gwt.view.client.ProvidesKey;

public class CisVsTransStat extends BaseModel {
	
	public enum TYPE  {radius,tss_upstream};
	
	public static ProvidesKey<CisVsTransStat> KEY_PROVIDER = new ProvidesKey<CisVsTransStat>() {
		@Override
		public Object getKey(CisVsTransStat item) {
			if (item != null && item.getId() != null) {
				return item.getId();
			}
			return null;
		}
	};
	
	TYPE type;
	Float percVarGlobal;
	Float percVarLocal;
	Integer distance;
	Float pseudoHeritabilityGlobal;
	Float pseudoHeritabilityGlobalLocal;
	Float score;
	
	@Override
	public String getId() {
		return distance.toString();
	}
	
	public TYPE getType() {
		return type;
	}
	public Float getPercVarGlobal() {
		return percVarGlobal;
	}
	public Float getPercVarLocal() {
		return percVarLocal;
	}
	public Integer getDistance() {
		return distance;
	}
	public Float getPseudoHeritabilityGlobal() {
		return pseudoHeritabilityGlobal;
	}
	public Float getPseudoHeritabilityGlobalLocal() {
		return pseudoHeritabilityGlobalLocal;
	}
	public Float getScore() {
		return score;
	}
	
	
}
