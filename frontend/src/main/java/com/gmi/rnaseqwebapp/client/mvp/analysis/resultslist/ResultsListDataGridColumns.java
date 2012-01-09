package com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist;

import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.ui.HighlightCell;
import com.gmi.rnaseqwebapp.client.util.SearchTermHighlight;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Hyperlink;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

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
	
	
	public static class HyperlinkCell extends AbstractCell<Hyperlink> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				Hyperlink link, SafeHtmlBuilder sb) {
			sb.append(SafeHtmlUtils.fromTrustedString(link.toString()));
		}
		
	}
	
}
