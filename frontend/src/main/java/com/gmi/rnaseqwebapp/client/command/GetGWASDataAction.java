package com.gmi.rnaseqwebapp.client.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gmi.rnaseqwebapp.client.dispatch.RequestBuilderActionImpl;
import com.gmi.rnaseqwebapp.client.dto.GWASResult;
import com.gmi.rnaseqwebapp.client.dto.ResultData;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.visualization.client.DataTable;

public class GetGWASDataAction extends RequestBuilderActionImpl<GetGWASDataActionResult> {

	private final String phenotype;
	private final String environment;
	private final String dataset;
	private final String transformation;
	private final String result;
	
	public GetGWASDataAction(final GWASResult result) {
		super();
		this.phenotype = result.getPhenotype();
		this.environment = result.getEnvironment();
		this.dataset = result.getDataset();
		this.transformation = result.getTransformation();
		this.result = result.getName();
	}

	@Override
	public String getUrl() {
		return getUrl(phenotype,environment, dataset, transformation,result);
	}

	@Override
	public GetGWASDataActionResult extractResult(Response response) {
		JSONObject serverData = JSONParser.parseLenient(response.getText()).isObject();
		JSONObject chr2data = serverData.get("chr2data").isObject();
		JSONArray chr2length = serverData.get("chr2length").isArray();
		double max_value = serverData.get("max_value").isNumber().doubleValue();
		double bonferroniThreshold = serverData.get("bonferroniThreshold").isNumber().doubleValue();
		List<DataTable> dataTables  =  new ArrayList<DataTable>();
		List<Integer> chr_lengths = new ArrayList<Integer>();
		Set<String> keys = chr2data.keySet();
		for (String chromosome : keys) 
		{
			String data = chr2data.get(chromosome).isString().stringValue();
			int chrLength = (int) chr2length.get(Integer.parseInt(chromosome)-1).isNumber().doubleValue();
			chr_lengths.add(chrLength);
			DataTable dataTable = DataTable.create(JSONParser.parseLenient(data).isObject().getJavaScriptObject());
			dataTable.insertRows(0,1);
			dataTable.setValue(0, 0, 0);
			int index = dataTable.addRow();
			dataTable.setValue(index, 0, chrLength);
			dataTables.add(dataTable);
		}
		return new GetGWASDataActionResult(new ResultData(dataTables,chr_lengths,max_value,bonferroniThreshold));
	}
	
	public static String getUrl(String Phenotype,String Environment, String Dataset,String Transformation, String Result)  {
		return BaseURL + "/getGWASData?phenotype="+ Phenotype + "&environment="+Environment+"&dataset="+Dataset + "&transformation=" + Transformation + "&result=" + Result;
	}
	
	
}
