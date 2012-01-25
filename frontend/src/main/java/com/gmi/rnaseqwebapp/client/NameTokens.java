package com.gmi.rnaseqwebapp.client;

public class NameTokens {

	public static final String homepage = "!homepage";
	public static final String accessionspage = "accessionspage";
	public static final String helppage = "helppage";
	public static final String resultslistpage = "resultslistpage";
	public static final String phenotypepage = "!phenotypepage";
	public static final String topsnpslistpage = "!topsnpslistpage";
	
	private enum ENV {T10C,T16C,GxE}
	
	
	public static String getHomepage() {
		return homepage;
	}

	public static String getAccessionspage() {
		return accessionspage;
	}

	public static String getHelppage() {
		return helppage;
	}


	public static String getResultslistpage() {
		return resultslistpage;
	}

	public static String getPhenotypepage() {
		return phenotypepage;
	}
	
	public static String getTopsnpslistpage() {
		return topsnpslistpage;
	}
	
	public static String getTopsnpslistpage(String environ_type,String result_type) {
		return topsnpslistpage+";environ_type="+environ_type+";result_type="+result_type;
	}
	
	public static String getTopsnpslistpage16EX() {
		return getTopsnpslistpage(ENV.T16C.toString(), "EX");
	}
	
	public static String getTopsnpslistpage16LM() {
		return getTopsnpslistpage(ENV.T16C.toString(), "LM");
	}
	
	public static String getTopsnpslistpage16KW() {
		return getTopsnpslistpage(ENV.T16C.toString(), "KW");
	}
	
	public static String getTopsnpslistpage10EX() {
		return getTopsnpslistpage(ENV.T10C.toString(), "EX");
	}
	
	public static String getTopsnpslistpage10LM() {
		return getTopsnpslistpage(ENV.T10C.toString(), "LM");
	}
	
	public static String getTopsnpslistpage10KW() {
		return getTopsnpslistpage(ENV.T10C.toString(), "KW");
	}
	
	public static String getTopsnpslistpageGxEFull() {
		return getTopsnpslistpage(ENV.GxE.toString(), "Full");
	}
	
	public static String getTopsnpslistpageGxEEnviron() {
		return getTopsnpslistpage(ENV.GxE.toString(), "Environ");
	}

	public static String getTopsnpslistpageGxEGenetic() {
		return getTopsnpslistpage(ENV.GxE.toString(), "Genetic");
	}

	
}
