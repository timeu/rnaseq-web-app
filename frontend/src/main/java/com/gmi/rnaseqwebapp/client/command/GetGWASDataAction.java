package com.gmi.rnaseqwebapp.client.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gmi.rnaseqwebapp.client.dispatch.RequestBuilderActionImpl;
import com.gmi.rnaseqwebapp.client.dto.GWASResult;
import com.gmi.rnaseqwebapp.client.dto.GxEResult;
import com.gmi.rnaseqwebapp.client.dto.ResultData;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.visualization.client.DataTable;

public class GetGWASDataAction extends RequestBuilderActionImpl<GetGWASDataActionResult> {

	public enum TYPE {GxE,GWAS}
	private final String phenotype;
	private final String environment;
	private final String dataset;
	private final String transformation;
	private final String result;
	private final TYPE type;
	
	
	public GetGWASDataAction(final GWASResult result) {
		this(result.getPhenotype(),result.getEnvironment(),result.getDataset(),result.getTransformation(),result.getName(),TYPE.GWAS);
	}
	
	public GetGWASDataAction(final GxEResult result) {
		this(result.getPhenotype(),"","","",result.getType().toString(),TYPE.GxE);
	}
	
	protected GetGWASDataAction(final String phenotype,final String environment, 
			final String dataset,final String transformation, final String result,final TYPE type) {
		super();
		this.result = result;
		this.phenotype = phenotype;
		this.dataset = dataset;
		this.environment = environment;
		this.transformation = transformation;
		this.type = type;
	}

	@Override
	public String getUrl() {
		if (type == TYPE.GWAS)
			return getUrl(phenotype,environment, dataset, transformation,result);
		else
			return getUrl(phenotype, result);
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
	
	public static String getUrl(String phenotype,String result) {
		return BaseURL +"/getGxEGWASData?phenotype="+phenotype+"&result="+result;
	}
	
	
}
