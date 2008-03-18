package org.mapfish.geo;

import java.util.LinkedList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Unit test for MfFeature.
 */
public class MfGeometryTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MfGeometryTest(String testName) {
        super(testName);
    }

    /**
     * Test.
     */
    public void testMfGeometry() {
        Geometry g = new GeometryFactory().createPoint(new Coordinate());
        MfGeometry mg = new MfGeometry(g);
        assertTrue(mg.getGeoType().equals(MfGeo.GeoType.GEOMETRY));
        assertTrue(mg.getInternalGeometry() == g);
    }
}
