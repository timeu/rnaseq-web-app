package com.gmi.rnaseqwebapp.client.dto;

import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypePredicate;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypePredicate.CRITERIA;
import com.gmi.rnaseqwebapp.client.util.AbstractDtoPredicate;
import com.google.gwt.view.client.ProvidesKey;

public class SNPResult extends BaseModel {
	
	public enum ENVIRON_TYPE {T10C,T16C,GxE};
	public enum RESULT_TYPE {EX,LM,KW,full,genetic,environ}

	public static ProvidesKey<SNPResult> KEY_PROVIDER = new ProvidesKey<SNPResult>() {
		@Override
		public Object getKey(SNPResult item) {
			if (item != null && item.getId() != null) {
				return item.getId();
			}
			return null;
		}
	};
	
	@Override
	public String getId() {
		return gene+"_"+snp_pos+"_snp_chr";
	}
	
	String gene;
	Integer gene_mid_pos;
	Integer gene_chr;
	Integer snp_chr;
	Integer snp_pos;
	Integer mac;
	Float maf;
	Float perc_var_expl;
	Float score;

	
	public String getGene() {
		return gene;
	}
	public Integer getGeneMidPos() {
		return gene_mid_pos;
	}
	public Integer getGeneChr() {
		return gene_chr;
	}
	public Integer getSnpChr() {
		return snp_chr;
	}
	public Integer getSnpPos() {
		return snp_pos;
	}
	
	public Integer getMAC() {
		return mac;
	}
	public Float getMAF() {
		return maf;
	}
	public Float getPercVarExpl() {
		return perc_var_expl;
	}
	public Float getScore() {
		return score;
	}
	
	
	public static  abstract class SNPResultPredicate<S> extends AbstractDtoPredicate<SNPResult, S> {

		public static enum CRITERIA {Gene,Gene_Chr,SNP_Chr,Result_Type,Environ_Type,Min_Score}
		
		public SNPResultPredicate(S value,CRITERIA key) {
			super(value,key.toString());
		}
	}
	
	public static class SNPResultGenePredicate extends SNPResultPredicate<String> {

		public SNPResultGenePredicate(String value) {
			super(value,CRITERIA.Gene);
		}

		@Override
		public boolean apply(SNPResult type) {
			return isContained(type.getGene(), value);
		}
	}
	
	public static class SNPResultGeneChrPredicate extends SNPResultPredicate<String> {

		public SNPResultGeneChrPredicate(String value) {
			super(value,CRITERIA.Gene_Chr);
		}

		@Override
		public boolean apply(SNPResult type) {
			return isContained(type.getGeneChr().toString(), value);
		}
	}
	
	public static class SNPResultSNPChrPredicate extends SNPResultPredicate<String> {

		public SNPResultSNPChrPredicate(String value) {
			super(value,CRITERIA.SNP_Chr);
		}

		@Override
		public boolean apply(SNPResult type) {
			return isContained(type.getSnpChr().toString(), value);
		}
	}
	
	
	
	public static class SNPResultMinScorePredicate extends SNPResultPredicate<Number> {

		public SNPResultMinScorePredicate(Number value) {
			super(value,CRITERIA.Min_Score);
		}

		@Override
		public boolean apply(SNPResult type) {
			return (value == null) ||  (type.getScore() >= value.floatValue());
		}
	}
	
	
}
