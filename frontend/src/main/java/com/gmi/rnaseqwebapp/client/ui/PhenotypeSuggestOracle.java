package com.gmi.rnaseqwebapp.client.ui;

import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.dto.Phenotypes;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypesReader;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;

public class PhenotypeSuggestOracle extends MultiWordSuggestOracle {

	protected int startQueryLength = 5;
	protected int maxCount=200;
	protected Phenotypes phenotypes ;
	protected int previousQueryLength = 0;
	private final PhenotypesReader phenotypesReader;
	
	public PhenotypeSuggestOracle(final PhenotypesReader phenotypesReader) 
	{
		this(phenotypesReader,5,200);
		
	}
	
	public PhenotypeSuggestOracle(final PhenotypesReader phenotypesReader,int startQueryLength,int maxCount ) {
		super();
		this.startQueryLength = startQueryLength;
		this.phenotypesReader = phenotypesReader;
		this.maxCount = maxCount;
		
	}
	
	
	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		try
		{
			if (request.getQuery().length() < startQueryLength) 
				return;
			if (phenotypes == null || phenotypes.hasMore() || previousQueryLength > request.getQuery().length() || phenotypes.getPhenotypes().size() == 0) 
			{
				previousQueryLength = 0;
				RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,"/gwas/getPhenotypes?range_length="+maxCount+"&name="+request.getQuery());
				requestBuilder.setCallback(new RequestCallback() {
					
					@Override
					public void onResponseReceived(
							com.google.gwt.http.client.Request httprequest,
							com.google.gwt.http.client.Response httpresponse) {
						phenotypes = phenotypesReader.read(httpresponse.getText());
						clear();
						for (Phenotype phenotype: phenotypes.getPhenotypes()) {
							add(phenotype.getName());
						}
						PhenotypeSuggestOracle.super.requestSuggestions(request, callback);
					}
	
					@Override
					public void onError(com.google.gwt.http.client.Request request,
							Throwable exception) {
						
					}
				});
			
				requestBuilder.send();
			
			}
			else
			{
				if (previousQueryLength == 0)
					previousQueryLength = request.getQuery().length()-1;
				super.requestSuggestions(request, callback);
			}
			
		}
		catch (Exception e) {
			
		}

	}

}
