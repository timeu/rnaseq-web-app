package com.gmi.rnaseqwebapp.client.dto;



public class Cofactor extends BaseModel{

	Integer step;
	Integer chr;
	Integer pos;
	double bic;
	double ebic;
	double mbic;
	double mbonf;
	double pseudo_heritability;
	
	
	@Override
	public String getId() {
		return step.toString();
	}
	
	public Integer getChr() {
		return chr;
	}
	
	public Integer getPos() {
		return pos;
	}

	public Integer getStep() {
		return step;
	}

	public double getBic() {
		return bic;
	}

	public double getMbic() {
		return mbic;
	}

	public double getEbic() {
		return ebic;
	}

	public double getMbonf() {
		return mbonf;
	}

	public double getPseudoHeritability() {
		return pseudo_heritability;
	}
}