package com.gmi.rnaseqwebapp.client.command;

import com.gmi.rnaseqwebapp.client.dispatch.RequestBuilderActionImpl;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypeReader;
import com.google.gwt.core.client.JsonUtils;
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
		String histogram_str = json.get("histogramdataTable").isString().stringValue();
		DataTable histogramdataTable = DataTable.create(JSONParser.parseLenient(histogram_str).isObject().getJavaScriptObject());
		return new GetPhenotypeDataActionResult(phenotype,histogramdataTable);
	}

	public static String getUrl(String id) {
		return BaseURL + "/getPhenotypeData?id="+id;
	}
	

}
