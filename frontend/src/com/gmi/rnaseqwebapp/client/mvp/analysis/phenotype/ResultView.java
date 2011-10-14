package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import java.util.ArrayList;
import java.util.List;

import at.gmi.nordborglab.widgets.geneviewer.client.datasource.DataSource;
import at.gmi.nordborglab.widgets.geneviewer.client.datasource.impl.GeneSuggestion;
import at.gmi.nordborglab.widgets.geneviewer.client.datasource.impl.ServerSuggestOracle;
import at.gmi.nordborglab.widgets.gwasgeneviewer.client.GWASGeneViewer;

import com.gmi.rnaseqwebapp.client.dto.Cofactor;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.DataTable;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ResultView extends ViewImpl implements ResultPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ResultView> {
	}
	
	@UiField AnchorElement download_link; 
	@UiField FlowPanel gwas_container;
	private final DataSource geneDataSource;
	private String[] colors = {"blue", "green", "red", "cyan", "purple"};
	private String[] gene_mark_colors = {"red", "red", "blue", "red", "green"};
	protected List<GWASGeneViewer> gwasGeneViewers = new ArrayList<GWASGeneViewer>();
	@UiField(provided=true)	final SuggestBox searchGene;

	@Inject
	public ResultView(final Binder binder, final DataSource geneDataSource) {
		this.geneDataSource = geneDataSource;
		searchGene = new SuggestBox(new ServerSuggestOracle(geneDataSource,5));
		((DefaultSuggestionDisplay)searchGene.getSuggestionDisplay()).setAnimationEnabled(true);
		searchGene.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				GeneSuggestion suggestion =  (GeneSuggestion)event.getSelectedItem();
				GWASGeneViewer viewer = getGWASGeneViewer(suggestion.getGene().getChromosome());
				if (viewer != null)
				{
					viewer.clearDisplayGenes();
					viewer.addDisplayGene(suggestion.getGene());
					viewer.refresh();
				}
			}
		});
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setDownloadURL(String url) {
		download_link.setHref(url);
	}

	@Override
	public void drawAssociationCharts(List<DataTable> dataTables,
			List<Cofactor> cofactors, List<Integer> chrLengths,
			double maxScore, double bonferroniThreshold) {
		clearAssociationCharts();
		Integer i = 1;
		java.util.Iterator<DataTable> iterator = dataTables.iterator();
		int width = gwas_container.getOffsetWidth() - 70;
		
		while(iterator.hasNext())
		{
			DataTable dataTable = iterator.next();
			String color = colors[i%colors.length];
			String gene_marker_color = gene_mark_colors[i%gene_mark_colors.length];
			GWASGeneViewer chart = new GWASGeneViewer("Chr"+i.toString(), color, gene_marker_color, width,geneDataSource);
			gwasGeneViewers.add(chart);
			for (Cofactor cofactor: cofactors){
				if (cofactor.getChr() == i)
				{
					chart.addSelection(GWASGeneViewer.getSelectionFromPos(dataTable, cofactor.getPos()));
					cofactors.remove(i);
				}
			}
			chart.setGeneInfoUrl("http://arabidopsis.org/servlets/TairObject?name={0}&type=gene");
			gwas_container.add((IsWidget)chart);
			chart.draw(dataTable,maxScore,0,chrLengths.get(i-1),bonferroniThreshold);
			i++;
		}
		
	}
	
	protected GWASGeneViewer getGWASGeneViewer(String chromosome) {
		for (GWASGeneViewer gwasGeneViewer : gwasGeneViewers) {
			
			if (gwasGeneViewer.getChromosome().equals(chromosome)) 
				return gwasGeneViewer;
		}
		return null;
	}


	public void clearAssociationCharts() {
		gwas_container.clear();
		gwasGeneViewers.clear();
	}
}