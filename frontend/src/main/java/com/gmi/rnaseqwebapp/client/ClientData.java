package com.gmi.rnaseqwebapp.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.gmi.rnaseqwebapp.client.dto.Accession;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypePredicate;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypePredicate.CRITERIA;
import com.gmi.rnaseqwebapp.client.dto.Readers.AccessionReader;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.visualization.client.DataTable;
import com.google.inject.Inject;



public class ClientData {

	private List<Accession> accessions = null;
	private final AccessionReader accessionReader;
	private List<Integer> chr_sizes = null;
	
	@Inject
	public ClientData(final AccessionReader accessionReader) {
		this.accessionReader = accessionReader;
	}
	
	public void loadData() {
		Dictionary clientData = Dictionary.getDictionary("clientData");
		String json = clientData.get("accessions");
		List<Accession> accessions = accessionReader.readList(json);
		this.accessions =accessions;
		JsArrayNumber chr_sizes_json = JsonUtils.safeEval(clientData.get("chr_sizes"));
		chr_sizes = new ArrayList<Integer>();
		for (int i = 0;i<  chr_sizes_json.length();i++) {
			chr_sizes.add(Integer.valueOf((int)chr_sizes_json.get(i)));
		}
	}
	
	public Accession getAccessionForId(int id) {
		return Accession.getAccessionForId(getAccessions(), id);
	}
	
	public List<Accession> getAccessions() {
		return accessions;
	}

	public List<Integer> getChrSizes() {
		return chr_sizes;
	}
}
