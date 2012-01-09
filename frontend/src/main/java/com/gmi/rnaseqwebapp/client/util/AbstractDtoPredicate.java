package com.gmi.rnaseqwebapp.client.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.regexp.shared.RegExp;


public abstract class AbstractDtoPredicate<T, S> implements DtoPredicate<T,S>,SearchTermHighlight {
	protected S value;
	protected String key;
	
	public AbstractDtoPredicate(S value,String key) {
		this.value = value;
		this.key = key;
	}
	
	@Override
	public void setValue(S value) {
		this.value = value;
	}
	
	@Override
	public S getValue() {
		return this.value;
	}
	
	public String getKey() {
		return key;
	}
	
	public static boolean isContained(String hay, String needle) {
		if (needle.isEmpty() || hay.indexOf(needle) >= 0 )
			return true;
		else
			return false;
	}
	
	@Override
	public RegExp getSearchRegExp() {
		if (value == null || value == "")
			return null;
		else
			return RegExp.compile("(" + value + ")", "ig");
	}
	
	public static <T> List<T> filter(List<T> items, Collection<? extends AbstractDtoPredicate<T,?>> predicates) {
		List<T> filtered_list = new ArrayList<T>();
		for (T item : items) {
			boolean isAdd = true;
			for (AbstractDtoPredicate<T,?> predicate: predicates) {
				if (!predicate.apply(item)) {
					isAdd = false;
					break;
				}
			 }
			if (isAdd)
				filtered_list.add(item);
		}
		return filtered_list;
	}
}