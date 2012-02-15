package com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist;

import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.ui.ColoredCell;
import com.gmi.rnaseqwebapp.client.ui.HighlightCell;
import com.gmi.rnaseqwebapp.client.ui.HyperlinkCell;
import com.gmi.rnaseqwebapp.client.util.SearchTermHighlight;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Hyperlink;


public interface ResultsListDataGridColumns {
	
	static Float bonferroniThreshold = new Float(7.53); 
	
	
	
	public static class PhenotypeIdColumn extends Column<Phenotype,Number> {

		public PhenotypeIdColumn() {
			super(new NumberCell(NumberFormat.getFormat("0")));
		}

		@Override
		public Number getValue(Phenotype object) {
			return object.getPhenotypeId();
		}
	}
	
	public static class NameColumn extends Column<Phenotype,String> {

		public NameColumn(SearchTermHighlight searchTerm) {
			super(new HighlightCell(searchTerm));
		}

		@Override
		public String getValue(Phenotype object) {
			return object.getName();
		}
	}
	
	public static class ChrColumn extends Column<Phenotype,String> {
		
		public ChrColumn(SearchTermHighlight searchTerm) {
			super(new HighlightCell(searchTerm));
		}

		@Override
		public String getValue(Phenotype object) {
			return object.getChr().toString();
		}
	}
	public static class MaxScore10Column extends Column<Phenotype,Number> {

		public MaxScore10Column() {
			super(new ColoredCell(bonferroniThreshold));
		}

		@Override
		public Number getValue(Phenotype object) {
			return object.getMaxScore10C();
		}
		
	}
	
	public static class MaxScore16Column extends Column<Phenotype,Number> {

		public MaxScore16Column() {
			super(new ColoredCell(bonferroniThreshold));
		}

		@Override
		public Number getValue(Phenotype object) {
			return object.getMaxScore16C();
		}
	}
	
	public static class MaxScoreFullColumn extends Column<Phenotype,Number> {

		public MaxScoreFullColumn() {
			super(new ColoredCell(bonferroniThreshold));
		}

		@Override
		public Number getValue(Phenotype object) {
			return object.getMaxScoreFull();
		}
	}
	
	public static class PseudoHeritability10Column extends Column<Phenotype,Number> {

		public PseudoHeritability10Column() {
			super(new NumberCell());
		}

		@Override
		public Number getValue(Phenotype object) {
			return object.getPseudoHeritability10C();
		}
		
	}
	
	public static class PseudoHeritability16Column extends Column<Phenotype,Number> {

		public PseudoHeritability16Column() {
			super(new NumberCell());
		}

		@Override
		public Number getValue(Phenotype object) {
			return object.getPseudoHeritability16C();
		}
		
	}
	
	
	
	
	public static class StartColumn extends Column<Phenotype,Number> {
		
		public StartColumn() {
			super(new NumberCell(NumberFormat.getFormat("0")));
		}

		@Override
		public Number getValue(Phenotype object) {
			return object.getStart();
		}
	}
	
	public static class EndColumn extends Column<Phenotype,Number> {
		
		public EndColumn() {
			super(new NumberCell(NumberFormat.getFormat("0")));
		}

		@Override
		public Number getValue(Phenotype object) {
			return object.getEnd();
		}
	}
	
	public static class DetailsColumn extends Column<Phenotype,Hyperlink> {
		
		String nameToken;
		
		public DetailsColumn(String nameToken) {
			super(new HyperlinkCell());
			this.nameToken = nameToken;
			// TODO Auto-generated constructor stub
		}

		@Override
		public Hyperlink getValue(Phenotype object) {
			String url = "!phenotypepage;id="+object.getName();
			return new Hyperlink("Details", url);
		}
		
	}
}
