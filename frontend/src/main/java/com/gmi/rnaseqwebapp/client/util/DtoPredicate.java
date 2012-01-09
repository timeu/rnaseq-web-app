package com.gmi.rnaseqwebapp.client.util;

public interface DtoPredicate<T, S> extends Predicate<T>{
	void setValue(S value);
	S getValue();
}
