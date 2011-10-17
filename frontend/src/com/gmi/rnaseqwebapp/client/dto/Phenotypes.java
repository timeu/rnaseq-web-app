package com.gmi.rnaseqwebapp.client.dto;

import java.util.List;

public class Phenotypes {

	List<Phenotype> phenotypes;
	
	Integer count;
	
	Integer start;
	
	Integer length;
	
	public List<Phenotype> getPhenotypes() {
		return phenotypes;
	}

	public int getCount() {
		return count;
	}
	
	public int getStart() {
		return start;
	}
	
	
	public int getLength() {
		return length;
	}

	public boolean isContained(int check_start, int check_length) {
		if ((check_start + check_length) > (start + length) ||
		    (check_start < start))
			return false;
		return true;
	}
	
	public void update(List<Phenotype> list) {
		this.phenotypes = list;
		this.count = list.size();
	}

	public boolean hasMore() {
		return count > phenotypes.size();
	}
	
}
