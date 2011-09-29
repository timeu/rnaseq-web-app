package com.gmi.rnaseqwebapp.client.gin;



import at.gmi.nordborglab.widgets.geneviewer.client.datasource.DataSource;
import at.gmi.nordborglab.widgets.geneviewer.client.datasource.impl.JBrowseDataSourceImpl;

import com.gmi.rnaseqwebapp.client.ClientData;
import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.RNASeqPlaceManager;
import com.gmi.rnaseqwebapp.client.dto.Readers.AccessionReader;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypeReader;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypesReader;
import com.gmi.rnaseqwebapp.client.mvp.home.HomePresenter;
import com.gmi.rnaseqwebapp.client.mvp.home.HomeView;
import com.gmi.rnaseqwebapp.client.mvp.main.MainPagePresenter;
import com.gmi.rnaseqwebapp.client.mvp.main.MainPageView;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;
import com.gwtplatform.dispatch.client.actionhandler.caching.DefaultCacheImpl;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gmi.rnaseqwebapp.client.mvp.accessions.AccessionPresenter;
import com.gmi.rnaseqwebapp.client.mvp.accessions.AccessionView;
import com.gmi.rnaseqwebapp.client.mvp.help.HelpPresenter;
import com.gmi.rnaseqwebapp.client.mvp.help.HelpView;
import com.gmi.rnaseqwebapp.client.resources.CellTableResources;
import com.gmi.rnaseqwebapp.client.resources.MyResources;
import com.gmi.rnaseqwebapp.client.mvp.analysis.AnalysisPresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.AnalysisView;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListPresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListView;



public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		install(new DefaultModule(RNASeqPlaceManager.class));
		
		bind(MyResources.class).in(Singleton.class);
	    bind(CellTableResources.class).in(Singleton.class);
	    
	    bind(AccessionReader.class).asEagerSingleton();
	    bind(PhenotypesReader.class).asEagerSingleton();
	    bind(PhenotypeReader.class).asEagerSingleton();
	    
	    bind(DataSource.class).toProvider(JBrowseDataSourceProvider.class).in(Singleton.class);
	    
	    bind(ClientData.class).asEagerSingleton();
	    
		
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
	}
	
	static class JBrowseDataSourceProvider implements Provider<DataSource> {
	    public DataSource get() {
	      return new JBrowseDataSourceImpl("/gwas/");
	    }
	  }

}
