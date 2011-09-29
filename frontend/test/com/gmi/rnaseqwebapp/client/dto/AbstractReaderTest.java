package com.gmi.rnaseqwebapp.client.dto;

import java.util.Date;

import com.gmi.rnaseqwebapp.client.dto.Readers.AccessionReader;
import com.gmi.rnaseqwebapp.client.dto.Readers.AccessionsReader;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypeReader;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypesReader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public abstract class AbstractReaderTest extends GWTTestCase
{
    @Override
    public String getModuleName()
    {
        return "com.gmi.rnaseqwebapp.RNASeqWebApp";
    }
    
    protected static AccessionReader accessionReader ; 
    protected static AccessionsReader accessionsReader;
    protected static PhenotypeReader phenotypeReader;
    protected static PhenotypesReader phenotypesReader;

    @Override
    protected void gwtSetUp() throws Exception
    {
        accessionReader = GWT.create(AccessionReader.class);
        accessionsReader = GWT.create(AccessionsReader.class);
        phenotypeReader = GWT.create(PhenotypeReader.class);
        phenotypesReader = GWT.create(PhenotypesReader.class);
    }


    /**
     * Dates are not compared with <code>equals()</code>. To prevent daylight
     * saving probblems only the the day, month and year is compared.
     * 
     * @param expected
     * @param actual
     */
    @SuppressWarnings("deprecation")
    protected void assertDate(Date expected, Date actual)
    {
        if (expected != null && actual != null)
        {
            assertEquals(expected.getYear(), actual.getYear());
            assertEquals(expected.getMonth(), actual.getMonth());
            assertEquals(expected.getDate(), actual.getDate());
        }
    }
    
}
