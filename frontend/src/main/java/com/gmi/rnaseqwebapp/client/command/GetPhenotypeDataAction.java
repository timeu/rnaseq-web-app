package com.gmi.rnaseqwebapp.client.command;

import com.gmi.rnaseqwebapp.client.dispatch.RequestBuilderActionImpl;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypeReader;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.visualization.client.DataTable;

public class GetPhenotypeDataAction extends RequestBuilderActionImpl<GetPhenotypeDataActionResult> {

	private final PhenotypeReader reader;
	private final String id;
	
	public GetPhenotypeDataAction(String id,PhenotypeReader reader) {
		this.id = id;
		this.reader = reader;
	}
	
	@Override
	public String getUrl() {
		return getUrl(id);
	}

	@Override
	public GetPhenotypeDataActionResult extractResult(Response response) {
		JSONObject json =  JSONParser.parseLenient(response.getText()).isObject();
		Phenotype phenotype = reader.read(json.get("phenotype").isObject());
		String mRNA_histogram_str = json.get("mRNAHistogramdataTable").isString().stringValue();
		String bs_histogram_str = json.get("bsHistogramdataTable").isString().stringValue();
		DataTable mRNAHistogramdataTable = DataTable.create(JSONParser.parseLenient(mRNA_histogram_str).isObject().getJavaScriptObject());
		DataTable bsHistogramDataTable = DataTable.create(JSONParser.parseLenient(bs_histogram_str).isObject().getJavaScriptObject());
		return new GetPhenotypeDataActionResult(phenotype,mRNAHistogramdataTable,bsHistogramDataTable);
	}

	public static String getUrl(String id) {
		return BaseURL + "/getPhenotypeData?id="+id;
	}
	

}
