package com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist;

import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.ui.HighlightCell;
import com.gmi.rnaseqwebapp.client.util.SearchTermHighlight;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Column;

public interface ResultsListDataGridColumns {
	
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
	
}
