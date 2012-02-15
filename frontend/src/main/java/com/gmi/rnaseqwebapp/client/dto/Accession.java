package com.gmi.rnaseqwebapp.client.dto;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.pehl.piriti.commons.client.Format;

import com.gmi.rnaseqwebapp.client.util.AbstractDtoPredicate;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;

public class Accession extends BaseModel implements Comparable<Object>{
	
	
	public static ProvidesKey<Accession> KEY_PROVIDER = new ProvidesKey<Accession>() {
		@Override
		public Object getKey(Accession item) {
			if (item != null && item.getId() != null) {
				return item.getId();
			}
			return null;
		}
	};
	
	
	Integer accession_id;
	String name;
	Double latitude;
	Double longitude;
	String country;
	String collector;
	String dataset;
	
	@Format("dd.MM.yyyy") Date collection_date;
	
	
	
	@Override
	public String getId() {
		return accession_id.toString();
	}


	public String getName() {
		return name;
	}


	public Integer getAccessionId() {
		return accession_id;
	}


	public Double getLatitude() {
		return latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}


	public String getCountry() {
		return country;
	}


	public String getCollector() {
		return collector;
	}
	
	public Date getCollectionDate() {
		return collection_date;
	}
	
	public String getDataset() {
		return dataset;
	}
	
	public String getLabelForMapMarker() {
		return getName()+" (" + getAccessionId()+")";
	}


	@Override
	public int compareTo(Object o) {
		if (o instanceof Accession) {
			return accession_id.compareTo(((Accession)o).accession_id);
		}
		else if (o instanceof Integer) {
			return accession_id.compareTo((Integer)o);
		}
		else
			throw new IllegalArgumentException ("The object that is compared must be either Integer or of type Accession"); 
	}
	
	public static  abstract class AccessionPredicate extends AbstractDtoPredicate<Accession, String> {

		public static enum CRITERIA {AccessionID,Name,Longitude,Latitude,Country,Collector,CollectionDate}

		public AccessionPredicate(String value,CRITERIA key) {
			super(value,key.toString());
		}
		
	}
	
	public static class AccessionNamePredicate extends AccessionPredicate{
		
		public AccessionNamePredicate(String value) {
			super(value,CRITERIA.Name);
		}

		@Override
		public boolean apply(Accession type) {
			
			return isContained(type.getName(), value);
		}
	}
	
	public static class AccessionCountryPredicate extends AccessionPredicate{
		
		public AccessionCountryPredicate(String value) {
			super(value,CRITERIA.Country);
		}

		@Override
		public boolean apply(Accession type) {
			return isContained(type.getCountry(), value);
		}
	}
	
	public static class AccessionCollectorPredicate extends AccessionPredicate{
		
		public AccessionCollectorPredicate(String value) {
			super(value,CRITERIA.Collector);
		}

		@Override
		public boolean apply(Accession type) {
			return isContained(type.getCollector(), value);
		}
	}
	
	
	
	
	public static DataTable calculateLocDistribution(Collection<Accession> accessions) {
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "Country");
		dataTable.addColumn(ColumnType.NUMBER, "Count");
		HashMap<String, Integer> aggr_countries = new HashMap<String,Integer>();
		for (Accession accession : accessions) {
			if (aggr_countries.containsKey(accession.getCountry())) {
				aggr_countries.put(accession.getCountry(),aggr_countries.get(accession.getCountry())+1);
			}
			else
			{
				aggr_countries.put(accession.getCountry(), 1);
			}
		}
		for (Map.Entry<String, Integer> entry : aggr_countries.entrySet()) {
		    int rowIndex = dataTable.addRow();
		    dataTable.setValue(rowIndex, 0,entry.getKey());
		    dataTable.setValue(rowIndex, 1, entry.getValue());
		}
		return dataTable;
	} 
	
	public static Accession getAccessionForId(List<Accession> accessions,int id) {
		for (Accession accession:accessions) {
			if (accession.getAccessionId() == id) 
				return accession;
		}
		return null;
	}
}
