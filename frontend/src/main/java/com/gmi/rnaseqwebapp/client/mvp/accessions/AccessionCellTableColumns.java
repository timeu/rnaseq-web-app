package com.gmi.rnaseqwebapp.client.mvp.accessions;

import java.util.Date;

import com.gmi.rnaseqwebapp.client.dto.Accession;
import com.gmi.rnaseqwebapp.client.ui.HighlightCell;
import com.gmi.rnaseqwebapp.client.util.SearchTermHighlight;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.cellview.client.Column;

public interface AccessionCellTableColumns {
	
	
/*	public class SearchTerm  {
		public static enum CRITERIA {AccessionID,Name,Longitude,Latitude,Country,Collector,CollectionDate}
		
		private final CRITERIA criteria;
		private String value ="";
		
		public SearchTerm(CRITERIA criteria) {
			this.criteria = criteria;
		}
		
		public CRITERIA getCriteria() {
			return criteria;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		
		public RegExp getSearchRegExp() {
			if (value == null || value == "")
				return null;
			else
				return RegExp.compile("(" + value + ")", "ig");
		}
	}
	*/
	
	public static class AccessionIdColumn extends Column<Accession, Number> {
		public AccessionIdColumn() {
			super(new NumberCell(NumberFormat.getFormat("0")));
		}

		@Override
		public Integer getValue(Accession object) {
			return object.getAccessionId();
		}
	}
	
	public static class NameColumn extends Column<Accession,String>
	{
		public NameColumn(SearchTermHighlight searchTerm)
		{
			super(new HighlightCell(searchTerm));
		}
		
		@Override
		public String getValue(Accession object) {
			return object.getName();
		}
	}
	
	public static class LatitudeColumn extends Column<Accession,Number> {

		public LatitudeColumn() {
			super(new NumberCell());
		}

		@Override
		public Number getValue(Accession object) {
			return object.getLatitude();
		}
	}
	
	public static class LongitudeColunn extends Column<Accession,Number> {
		public LongitudeColunn() {
			super(new NumberCell());
		}

		@Override
		public Number getValue(Accession object) {
			return object.getLongitude();
		}
	}
	
	public static class LongitudeLatitudeColumn extends Column<Accession,String> {

		public LongitudeLatitudeColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(Accession object) {
			return object.getLongitude() +"/" + object.getLatitude();
		}
	}
	
	public static class CountryColumn extends Column<Accession,String> {
		public CountryColumn(SearchTermHighlight searchTerm) {
			super(new HighlightCell(searchTerm));
		}

		@Override
		public String getValue(Accession object) {
			return object.getCountry();
		}
	}
	
	public static class CollectorColumn extends Column<Accession,String> {
		public CollectorColumn(SearchTermHighlight searchTerm) {
			super(new HighlightCell(searchTerm));
		}

		@Override
		public String getValue(Accession object) {
			return object.getCollector();
		}
	}
	
	public static class CollectionDateColumn extends Column<Accession,Date> {
		public CollectionDateColumn() {
			super(new DateCell(DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG)));
		}

		@Override
		public Date getValue(Accession object) {
			return object.getCollectionDate();
		}
	}

}
