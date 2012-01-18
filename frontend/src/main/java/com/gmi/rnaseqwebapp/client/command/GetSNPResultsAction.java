package com.gmi.rnaseqwebapp.client.command;

import java.util.Collection;
import java.util.Iterator;

import com.gmi.rnaseqwebapp.client.dispatch.RequestBuilderActionImpl;
import com.gmi.rnaseqwebapp.client.dto.Readers.SNPResultsReader;
import com.gmi.rnaseqwebapp.client.dto.SNPResult.SNPResultPredicate;
import com.gmi.rnaseqwebapp.client.util.AbstractDtoPredicate;
import com.google.gwt.http.client.Response;

public class GetSNPResultsAction extends RequestBuilderActionImpl<GetSNPResultsActionResult> {

	private final int start;
	private final int length;
	private final Collection<SNPResultPredicate<?>> searchTerms;
	private final SNPResultsReader reader;
	private final String environ_type;
	private final String result_type;
	
	public GetSNPResultsAction (String environ_type,String result_type,int start, int length,Collection<SNPResultPredicate<?>> collection,SNPResultsReader reader)  {
		this.start = start;
		this.length = length;
		this.searchTerms = collection;
		this.reader = reader;
		this.result_type = result_type;
		this.environ_type = environ_type;
		
	}

	public GetSNPResultsAction(String environ_type,String result_type,SNPResultsReader reader) {
		this(environ_type,result_type,0,-1,null,reader);
	}

	@Override
	public String getUrl() {
		return getUrl(environ_type,result_type,start, length,getSearchTermParameters());
	}
	
	@Override
	public GetSNPResultsActionResult extractResult(Response response) {
		return new GetSNPResultsActionResult(reader.read(response.getText()));
	}
	
	private String getSearchTermParameters()  {
		return SNPResultPredicate.toRequestString(searchTerms);
		/*StringBuilder sb = new StringBuilder();
		if (searchTerms != null && searchTerms.size() > 0)
		{
			Iterator<SNPResultPredicate<?>> iterator = searchTerms.iterator();
			while(iterator.hasNext()) {
				SNPResultPredicate<?> searchTerm = iterator.next();
				if (searchTerm.getValue() != null) {
					sb.append("&");
					sb.append(searchTerm.getKey().toLowerCase());
					sb.append("=");
					sb.append(searchTerm.getValue());
				}
			}
			return sb.toString();
		}
		return "";*/
	}
	
	public static String getUrl(String environ_type,String result_type,int start,int length,String searchTermParameters) {
		return BaseURL + "/getTopResults?environ_type="+environ_type+"&result_type="+result_type+"&range_start="+ start + "&range_length=" + length+searchTermParameters;
	}

}

