package org.mapfish.geo;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import net.sf.json.util.JSONBuilder;

/**
 * Unit test for MfFeature.
 */
public class MfFeatureTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MfFeatureTest(String testName) {
        super(testName);
    }

    /**
     * Test.
     */
    public void testMfFeature() {
        MfFeature f = new MfFeature() {
            public String getFeatureId() { return null; }
            public MfGeometry getMfGeometry() { return null; }
            public void toJSON(JSONBuilder builder) {}

        };
        assertTrue(f.getGeoType().equals(MfGeo.GeoType.FEATURE));
    }
}
