package com.gmi.rnaseqwebapp.client.command;

import com.gmi.rnaseqwebapp.client.dispatch.RequestBuilderActionImpl;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.visualization.client.DataTable;

public class GetPhenotypeMotionChartDataAction extends 	RequestBuilderActionImpl<GetPhenotypeMotionChartDataActionResult> {

	private final String phenotype;
	private final String environment;
	private final String dataset;
	private final String transformation;
	
	public GetPhenotypeMotionChartDataAction(String phenotype,String environment,String dataset,String transformation) {
		this.phenotype = phenotype;
		this.environment = environment;
		this.dataset = dataset;
		this.transformation = transformation;
	}
	
	@Override
	public String getUrl() {
		return getUrl(phenotype,environment,dataset,transformation);
	}
	
	public static String getUrl(String phenotype,String environment, String dataset, String transformation) {
		return BaseURL + "/getPhenotypeMotionChartData?phenotype=" + phenotype + "&environment="+environment+"&dataset="+dataset+"&transformation="+transformation;
	}

	@Override
	public GetPhenotypeMotionChartDataActionResult extractResult(
			Response response) {
		
		JSONObject json = JSONParser.parseLenient(response.getText()).isObject();
		String motionchartTable_str = json.get("data").isString().stringValue();
		DataTable motionChartDataTable = DataTable.create(JSONParser.parseLenient(motionchartTable_str).isObject().getJavaScriptObject());
		return new GetPhenotypeMotionChartDataActionResult(motionChartDataTable);
	}
	

}
