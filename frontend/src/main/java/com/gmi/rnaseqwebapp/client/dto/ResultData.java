package com.gmi.rnaseqwebapp.client.dto;

import java.util.List;

import com.google.gwt.visualization.client.DataTable;

public class ResultData {

	protected List<DataTable> associationTables;
	protected double max_score;
	protected double bonferroniThreshold;
	
	public ResultData (List<DataTable> associationTables,List<Integer> chr_lengths,double max_score,double bonferroniThreshold)
	{
		this.associationTables = associationTables;
		this.max_score = max_score;
		this.bonferroniThreshold = bonferroniThreshold;
	}
	
	public List<DataTable> getAssociationTables()
	{
		return this.associationTables;
	}
	
	public double getMaxScore()
	{
		return this.max_score;
	}
	

	public double getBonferroniThreshold() {
		return bonferroniThreshold;
	}
}
