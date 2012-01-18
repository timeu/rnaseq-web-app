package com.gmi.rnaseqwebapp.client.dto;

import java.util.List;

public class SNPResults {
	
	List<SNPResult> results;
	
	Integer count;
	
	Integer start;
	
	Integer length;
	
	public List<SNPResult> getList() {
		return results;
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
	
	public void update(List<SNPResult> list) {
		this.results = list;
		this.count = list.size();
	}

	public boolean hasMore() {
		return count > results.size();
	}
}
