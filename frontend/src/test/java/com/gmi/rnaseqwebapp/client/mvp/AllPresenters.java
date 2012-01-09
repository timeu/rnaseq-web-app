package com.gmi.rnaseqwebapp.client.mvp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.gmi.rnaseqwebapp.client.mvp.accessions.AccessionPresenter;
import com.gmi.rnaseqwebapp.client.mvp.accessions.AccessionPresenterTest;
import com.gmi.rnaseqwebapp.client.mvp.main.MainPagePresenterTest;
import com.gmi.rnaseqwebapp.client.mvp.main.MainPageViewTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({MainPageViewTest.class,MainPagePresenterTest.class,
	AccessionPresenterTest.class})
public class AllPresenters {

}
