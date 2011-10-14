package com.gmi.rnaseqwebapp.client.gin;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.google.gwt.inject.client.AsyncProvider;
import com.gmi.rnaseqwebapp.client.ClientData;
import com.gmi.rnaseqwebapp.client.mvp.home.HomePresenter;
import com.gmi.rnaseqwebapp.client.mvp.main.MainPagePresenter;
import com.google.inject.Provider;
import com.gmi.rnaseqwebapp.client.mvp.accessions.AccessionPresenter;
import com.gmi.rnaseqwebapp.client.mvp.help.HelpPresenter;
import com.gmi.rnaseqwebapp.client.resources.MyResources;
import com.gmi.rnaseqwebapp.client.mvp.analysis.AnalysisPresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListPresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.PhenotypePresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.PhenotypeDetailPresenter;


@GinModules({ClientDispatchModule.class,ClientModule.class})
public interface ClientGinjector extends Ginjector{
	EventBus getEventBus();
	PlaceManager getPlaceManager();
	Provider<MainPagePresenter> getMainPagePresenter();
	Provider<HomePresenter> getHomePresenter();
	AsyncProvider<AccessionPresenter> getAccessionPresenter();
	AsyncProvider<HelpPresenter> getHelpPresenter();
	MyResources getResource();
	ClientData getClientData();
	AsyncProvider<AnalysisPresenter> getAnalysisPresenter();
	AsyncProvider<ResultsListPresenter> getResultsListPresenter();
	AsyncProvider<PhenotypePresenter> getPhenotypePresenter();
	AsyncProvider<PhenotypeDetailPresenter> getPhenotypeDetailPresenter();
}
