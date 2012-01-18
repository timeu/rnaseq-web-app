package com.gmi.rnaseqwebapp.client.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class ColoredCell extends AbstractCell<Number> {
	
	private static Templates templates = GWT.create(Templates.class);
	
	interface Templates extends SafeHtmlTemplates {
		
		@SafeHtmlTemplates.Template("<div style=\"{0}\">{1}</div>")
		SafeHtml cell(SafeStyles styles, SafeHtml value);
	}
	protected Number threshold;
	
	public ColoredCell(Number threshold) {
		this.threshold = threshold;
	}


	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			Number value, SafeHtmlBuilder sb) {
		if (value == null) {
	        return;
	      }
		  String color ="black";
		  String style = "";
		  if (value.floatValue() > threshold.floatValue()) {
			  color="green";
			  style = "font-weight:bold;";
		  }
		  style = style +  "color:" + color+";";
	      SafeHtml safeValue = SafeHtmlUtils.fromString(value.toString());
	      SafeStyles styles = SafeStylesUtils.fromTrustedString(style);
	      SafeHtml rendered = templates.cell(styles, safeValue);
	      sb.append(rendered);
	}
	
}
