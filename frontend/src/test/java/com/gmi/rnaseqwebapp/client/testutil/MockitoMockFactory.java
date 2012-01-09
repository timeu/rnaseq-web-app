package com.gmi.rnaseqwebapp.client.testutil;

import org.mockito.Mockito;

import com.gwtplatform.tester.MockFactory;

public class MockitoMockFactory implements MockFactory {

	@Override
	public <T> T mock(Class<T> classToMock) {
		return Mockito.mock(classToMock);
	}

}
