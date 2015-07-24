package com.gmi.rnaseqwebapp.client.dto;


import javax.sound.sampled.Control.Type;

import com.gmi.rnaseqwebapp.client.util.AbstractDtoPredicate;
import com.google.gwt.view.client.ProvidesKey;


public class Phenotype extends BaseModel {
	
	public static ProvidesKey<Phenotype> KEY_PROVIDER = new ProvidesKey<Phenotype>() {
		@Override
		public Object getKey(Phenotype item) {
			if (item != null && item.getId() != null) {
				return item.getId();
			}
			return null;
		}
	};

	Integer phenotype_id;
	String name;
	Integer chr;
	Integer start;
	Integer end;
	Dataset bsDataset;
	Dataset mRNADataSet;

	
	@Override
	public String getId() {
		return phenotype_id.toString();
	}
	
	public Integer getPhenotypeId() {
		return phenotype_id;
	}
	
	public String getName() {
		return name;
	}
	public Integer getChr() {
		return chr;
	}
	public Integer getStart() {
		return start;
	}
	public Integer getEnd() {
		return end;
	}
	
	public Dataset getBsDataset() {
		return bsDataset;
	}
	
	public Dataset getMRNADataset() {
		return mRNADataSet;
	}
	
	
	public static  abstract class PhenotypePredicate<S> extends AbstractDtoPredicate<Phenotype, S> {

		public static enum CRITERIA {PhenotypeID,Name,Chr,Start,End}
		
		public PhenotypePredicate(S value,CRITERIA key) {
			super(value,key.toString());
		}

	}
	
	
	public static class PhenotypeNamePredicate extends PhenotypePredicate<String> {
		
		

		public PhenotypeNamePredicate(String value) {
			super(value,CRITERIA.Name);
		}

		@Override
		public boolean apply(Phenotype type) {
			return isContained(type.getName(), value);
		}
	}
	
	public static class PhenotypeChrPredicate extends PhenotypePredicate<String> {

		public PhenotypeChrPredicate(String value) {
			super(value,CRITERIA.Chr);
		}

		@Override
		public boolean apply(Phenotype type) {
			return isContained(type.getChr().toString(), value);
		}
	}
	
	public static class PhenotypeStartPredicate extends PhenotypePredicate<Number> {

		public PhenotypeStartPredicate(Number value) {
			super(value,CRITERIA.Start);
		}

		@Override
		public boolean apply(Phenotype type) {
			return (value == null) || (type.getStart() >= value.intValue());
		}
	}
	
	public static class PhenotypeEndPredicate extends PhenotypePredicate<Number> {

		public PhenotypeEndPredicate(Number value) {
			super(value,CRITERIA.End);
		}

		@Override
		public boolean apply(Phenotype type) {
			return (value == null) ||  (type.getEnd() <= value.intValue());
		}
	}

	public Dataset getDatasetFromName(Dataset.TYPE dataset) {
		if (dataset == Dataset.TYPE.bisulfite) 
			return bsDataset;
		else
			return mRNADataSet;
	}

	
	
	/*public static <S> List<Phenotype> filter(List<Phenotype> phenotypes, List<PhenotypePredicate<S>> predicates) {
		List<Phenotype> filtered_list = new ArrayList<Phenotype>();
		for (Phenotype phenotype : phenotypes) {
			boolean isAdd = true;
			for (PhenotypePredicate<S> predicate: predicates) {
				if (!predicate.apply(phenotype)) {
					isAdd = false;
					break;
				}
			 }
			if (isAdd)
				filtered_list.add(phenotype);
		}
		return filtered_list;
	}*/
}
