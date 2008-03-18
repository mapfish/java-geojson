package org.mapfish.geo;

import java.util.LinkedList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import net.sf.json.util.JSONBuilder;

/**
 * Unit test for MfFeature.
 */
public class MfFeatureCollectionTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MfFeatureCollectionTest(String testName) {
        super(testName);
    }

    /**
     * Test.
     */
    public void testMfFeatureCollection() {
        LinkedList<MfFeature> c = new LinkedList<MfFeature>();
        MfFeatureCollection fc = new MfFeatureCollection(c);
        assertTrue(fc.getGeoType().equals(MfGeo.GeoType.FEATURECOLLECTION));
        assertTrue(fc.getCollection() == c);
    }
}
