package com.gmi.rnaseqwebapp.client.gin;



import at.gmi.nordborglab.widgets.geneviewer.client.datasource.DataSource;
import at.gmi.nordborglab.widgets.geneviewer.client.datasource.impl.JBrowseDataSourceImpl;

import com.gmi.rnaseqwebapp.client.ClientData;
import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.RNASeqPlaceManager;
import com.gmi.rnaseqwebapp.client.dto.Readers.AccessionReader;
import com.gmi.rnaseqwebapp.client.dto.Readers.DatasetReader;
import com.gmi.rnaseqwebapp.client.dto.Readers.EnvironmentReader;
import com.gmi.rnaseqwebapp.client.dto.Readers.GWASResultReader;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypeReader;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypesReader;
import com.gmi.rnaseqwebapp.client.dto.Readers.TransformationReader;
import com.gmi.rnaseqwebapp.client.mvp.accessions.AccessionPresenter;
import com.gmi.rnaseqwebapp.client.mvp.accessions.AccessionView;
import com.gmi.rnaseqwebapp.client.mvp.analysis.AnalysisPresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.AnalysisView;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.EnvironmentDetailPresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.EnvironmentDetailView;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.PhenotypeDetailPresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.PhenotypeDetailView;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.PhenotypeOverviewPresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.PhenotypeOverviewView;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.PhenotypePresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.PhenotypeView;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListPresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListView;
import com.gmi.rnaseqwebapp.client.mvp.help.HelpPresenter;
import com.gmi.rnaseqwebapp.client.mvp.help.HelpView;
import com.gmi.rnaseqwebapp.client.mvp.home.HomePresenter;
import com.gmi.rnaseqwebapp.client.mvp.home.HomeView;
import com.gmi.rnaseqwebapp.client.mvp.main.MainPagePresenter;
import com.gmi.rnaseqwebapp.client.mvp.main.MainPageView;
import com.gmi.rnaseqwebapp.client.resources.CellTableResources;
import com.gmi.rnaseqwebapp.client.resources.MyResources;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;
import com.gwtplatform.dispatch.client.actionhandler.caching.DefaultCacheImpl;
import com.gwtplatform.mvp.client.annotations.GaAccount;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalyticsNavigationTracker;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.ResultPresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.ResultView;



public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		install(new DefaultModule(RNASeqPlaceManager.class));
		
		bind(MyResources.class).in(Singleton.class);
	    bind(CellTableResources.class).in(Singleton.class);
	    
	    bind(AccessionReader.class).asEagerSingleton();
	    bind(PhenotypesReader.class).asEagerSingleton();
	    bind(PhenotypeReader.class).asEagerSingleton();
	    bind(EnvironmentReader.class).asEagerSingleton();
	    bind(DatasetReader.class).asEagerSingleton();
	    bind(TransformationReader.class).asEagerSingleton();
	    bind(GWASResultReader.class).asEagerSingleton();
	    
	    bind(DataSource.class).toProvider(JBrowseDataSourceProvider.class).in(Singleton.class);
	    
	    bind(ClientData.class).asEagerSingleton();
	    
		//Google Analytics
	    bindConstant().annotatedWith(GaAccount.class).to("UA-26151238-1");
	    bind(GoogleAnalyticsNavigationTracker.class).asEagerSingleton();
	    
		bind(Cache.class).to(DefaultCacheImpl.class).in(Singleton.class);
		
		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.homepage);

		bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
				MainPageView.class, MainPagePresenter.MyProxy.class);


		bindPresenter(HomePresenter.class, HomePresenter.MyView.class,
				HomeView.class, HomePresenter.MyProxy.class);

		bindPresenter(AccessionPresenter.class,
				AccessionPresenter.MyView.class, AccessionView.class,
				AccessionPresenter.MyProxy.class);

		bindPresenter(HelpPresenter.class, HelpPresenter.MyView.class,
				HelpView.class, HelpPresenter.MyProxy.class);

		bindPresenter(AnalysisPresenter.class, AnalysisPresenter.MyView.class,
				AnalysisView.class, AnalysisPresenter.MyProxy.class);

		bindPresenter(ResultsListPresenter.class,
				ResultsListPresenter.MyView.class, ResultsListView.class,
				ResultsListPresenter.MyProxy.class);

		bindPresenter(PhenotypePresenter.class,
				PhenotypePresenter.MyView.class, PhenotypeView.class,
				PhenotypePresenter.MyProxy.class);

		bindSingletonPresenterWidget(PhenotypeOverviewPresenter.class,
				PhenotypeOverviewPresenter.MyView.class,
				PhenotypeOverviewView.class);

		bindSingletonPresenterWidget(PhenotypeDetailPresenter.class,
				PhenotypeDetailPresenter.MyView.class,
				PhenotypeDetailView.class);

		bindPresenterWidget(EnvironmentDetailPresenter.class,
				EnvironmentDetailPresenter.MyView.class,
				EnvironmentDetailView.class);

		bindPresenterWidget(ResultPresenter.class,
				ResultPresenter.MyView.class, ResultView.class);
	}
	
	static class JBrowseDataSourceProvider implements Provider<DataSource> {
	    public DataSource get() {
	      return new JBrowseDataSourceImpl("/gwas/");
	    }
	  }

}
