package com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist;

import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.dto.SNPResult;
import com.gmi.rnaseqwebapp.client.ui.ColoredCell;
import com.gmi.rnaseqwebapp.client.ui.HighlightCell;
import com.gmi.rnaseqwebapp.client.ui.HyperlinkCell;
import com.gmi.rnaseqwebapp.client.util.SearchTermHighlight;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Hyperlink;

public interface TopSNPsListDataGridColumns {
	
	static Float bonferroniThreshold = new Float(7.53); 
	
	public static class GeneColumn extends Column<SNPResult,String> {

		public GeneColumn(SearchTermHighlight searchTerm) {
			super(new HighlightCell(searchTerm));
		}

		@Override
		public String getValue(SNPResult object) {
			return object.getGene();
		}
	}
	
	
	public static class GeneChrColumn extends Column<SNPResult,String> {
		
		public GeneChrColumn(SearchTermHighlight searchTerm) {
			super(new HighlightCell(searchTerm));
		}

		@Override
		public String getValue(SNPResult object) {
			return object.getGeneChr().toString();
		}
	}
	
	public static class GeneMidPosColumn extends Column<SNPResult,Number> {
		
		public GeneMidPosColumn() {
			super(new NumberCell(NumberFormat.getFormat("0")));
		}

		@Override
		public Number getValue(SNPResult object) {
			return object.getGeneMidPos();
		}
	}
	
	public static class SnpChrColumn extends Column<SNPResult,String> {
		
		public SnpChrColumn(SearchTermHighlight searchTerm) {
			super(new HighlightCell(searchTerm));
		}

		@Override
		public String getValue(SNPResult object) {
			return object.getSnpChr().toString();
		}
	}
	
	public static class SnpPosColumn extends Column<SNPResult,Number> {
		
		public SnpPosColumn() {
			super(new NumberCell(NumberFormat.getFormat("0")));
		}

		@Override
		public Number getValue(SNPResult object) {
			return object.getSnpPos();
		}
	}
	
	public static class MACColumn extends Column<SNPResult,Number> {
		
		public MACColumn() {
			super(new NumberCell(NumberFormat.getFormat("0")));
		}

		@Override
		public Number getValue(SNPResult object) {
			return object.getMAC();
		}
	}
	
	public static class MAFColumn extends Column<SNPResult,Number> {
		
		public MAFColumn() {
			super(new NumberCell());
		}

		@Override
		public Number getValue(SNPResult object) {
			return object.getMAF();
		}
	}
	public static class PercVarExplColumn extends Column<SNPResult,Number> {
		
		public PercVarExplColumn() {
			super(new NumberCell());
		}

		@Override
		public Number getValue(SNPResult object) {
			return object.getPercVarExpl();
		}
	}
	
	public static class ScoreColumn extends Column<SNPResult,Number> {
		public ScoreColumn() {
			super(new ColoredCell(bonferroniThreshold));
		}

		@Override
		public Number getValue(SNPResult object) {
			return object.getScore();
		}
	}
	
	public static class DetailsColumn extends Column<SNPResult,Hyperlink> {
		
		String nameToken;
		String environ;
		String result;
		
		public DetailsColumn(String nameToken) {
			super(new HyperlinkCell());
			this.nameToken = nameToken;
			// TODO Auto-generated constructor stub
		}

		@Override
		public Hyperlink getValue(SNPResult object) {
			String url = nameToken;
			url = url+ ";id="+object.getGene()+";env="+environ+";result="+result;
			return new Hyperlink("Details", url);
		}
		
		public void setData(String environ,String result) {
			this.environ = environ;
			this.result = result;
		}
	}
}
