package com.gmi.rnaseqwebapp.client.command;

import java.util.Collection;
import java.util.Iterator;

import com.gmi.rnaseqwebapp.client.dispatch.RequestBuilderActionImpl;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypePredicate;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypesReader;
import com.google.gwt.http.client.Response;

public class GetPhenotypesAction extends RequestBuilderActionImpl<GetPhenotypesActionResult> {

	private final int start;
	private final int length;
	private final Collection<PhenotypePredicate<?>> searchTerms;
	private final PhenotypesReader reader;
	
	public GetPhenotypesAction (int start, int length,Collection<PhenotypePredicate<?>> collection,PhenotypesReader reader)  {
		this.start = start;
		this.length = length;
		this.searchTerms = collection;
		this.reader = reader;
		
	}

	public GetPhenotypesAction(PhenotypesReader reader) {
		this(0,-1,null,reader);
	}

	@Override
	public String getUrl() {
		return getUrl(start, length,getSearchTermParameters());
	}
	
	@Override
	public GetPhenotypesActionResult extractResult(Response response) {
		return new GetPhenotypesActionResult(reader.read(response.getText()));
	}
	
	private String getSearchTermParameters()  {
		StringBuilder sb = new StringBuilder();
		if (searchTerms != null && searchTerms.size() > 0)
		{
			Iterator<PhenotypePredicate<?>> iterator = searchTerms.iterator();
			while(iterator.hasNext()) {
				PhenotypePredicate<?> searchTerm = iterator.next();
				if (searchTerm.getValue() != null) {
					sb.append("&");
					sb.append(searchTerm.getKey().toLowerCase());
					sb.append("=");
					sb.append(searchTerm.getValue());
				}
			}
			return sb.toString();
		}
		return "";
	}
	
	public static String getUrl(int start,int length,String searchTermParameters) {
		return BaseURL + "/getPhenotypes?range_start="+ start + "&range_length=" + length+searchTermParameters;
	}

}
