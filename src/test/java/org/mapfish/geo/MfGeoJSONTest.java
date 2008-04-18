package org.mapfish.geo;

import java.util.LinkedList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import net.sf.json.util.JSONBuilder;
import net.sf.json.util.JSONStringer;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Unit test for MfGeoJSON.
 */
public class MfGeoJSONTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MfGeoJSONTest(String testName) {
        super(testName);
    }

    /**
     * Test.
     */
    public void testMfGeoJSON() {
        JSONStringer stringer;
        MfGeoJSON builder;

        Geometry g;

        MfFeature f1;
        MfFeature f2;
        MfFeatureCollection fc;

        String geojsonExpected;
        String geojsonResulted;

        final Coordinate coord1 = new Coordinate(1.1, 2.2);
        final Coordinate coord2 = new Coordinate(3.3, 4.4);
        final Coordinate coord3 = new Coordinate(5.5, 6.6);

        // encodeGeometry POINT test
        stringer = new JSONStringer();
        builder = new MfGeoJSON(stringer);
        g = new GeometryFactory().createPoint(coord1);
        geojsonExpected =
            "{\"type\":\"Point\",\"coordinates\":[1.1,2.2]}";
        builder.encodeGeometry(g);
        geojsonResulted = stringer.toString();
        assertTrue(geojsonExpected.equals(geojsonResulted));

        // encodeGeometry LINESTRING test
        stringer = new JSONStringer();
        builder = new MfGeoJSON(stringer);
        Coordinate[] coordArrayLS = {coord1, coord2};
        g = new GeometryFactory().createLineString(coordArrayLS);
        geojsonExpected =
            "{\"type\":\"LineString\",\"coordinates\":[[1.1,2.2],[3.3,4.4]]}";
        builder.encodeGeometry(g);
        geojsonResulted = stringer.toString();
        assertTrue(geojsonExpected.equals(geojsonResulted));

        // encodeGeometry POLYGON test
        stringer = new JSONStringer();
        builder = new MfGeoJSON(stringer);
        Coordinate[] coordArrayLR = {coord1, coord2, coord3, coord1};
        LinearRing lr = new GeometryFactory().createLinearRing(coordArrayLR);
        g = new GeometryFactory().createPolygon(lr, null);
        geojsonExpected =
            "{\"type\":\"Polygon\",\"coordinates\":[[[1.1,2.2],[3.3,4.4],[5.5,6.6],[1.1,2.2]]]}";
        builder.encodeGeometry(g);
        geojsonResulted = stringer.toString();
        assertTrue(geojsonExpected.equals(geojsonResulted));

        // encodeFeature test
        stringer = new JSONStringer();
        builder = new MfGeoJSON(stringer);
        f1 = new MfFeature() {
            public String getFeatureId() {
                return "fid_foo";
            }
            public MfGeometry getMfGeometry() {
                return new MfGeometry(
                    new GeometryFactory().createPoint(coord1));
            }
            public void toJSON(JSONBuilder builder) {
                builder.key("prop_foo").value("foo");
            }
        };
        geojsonExpected =
            "{\"type\":\"Feature\",\"id\":\"fid_foo\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.1,2.2]},\"properties\":{\"prop_foo\":\"foo\"}}";
        builder.encodeFeature(f1);
        geojsonResulted = stringer.toString();
        assertTrue(geojsonExpected.equals(geojsonResulted));
        
        // encodeFeature test with null internal geometry
        stringer = new JSONStringer();
        builder = new MfGeoJSON(stringer);
        f1 = new MfFeature() {
            public String getFeatureId() {
                return "fid_foo";
            }
            public MfGeometry getMfGeometry() {
                return new MfGeometry(null);
            }
            public void toJSON(JSONBuilder builder) {
                builder.key("prop_foo").value("foo");
            }
        };
        geojsonExpected =
            "{\"type\":\"Feature\",\"id\":\"fid_foo\",\"geometry\":null,\"properties\":{\"prop_foo\":\"foo\"}}";
        builder.encodeFeature(f1);
        geojsonResulted = stringer.toString();
        assertTrue(geojsonExpected.equals(geojsonResulted));

        // encodeFeature test with null geometry
        stringer = new JSONStringer();
        builder = new MfGeoJSON(stringer);
        f1 = new MfFeature() {
            public String getFeatureId() {
                return "fid_foo";
            }
            public MfGeometry getMfGeometry() {
                return null;
            }
            public void toJSON(JSONBuilder builder) {
                builder.key("prop_foo").value("foo");
            }
        };
        // use geojsonExpected as defined above
        builder.encodeFeature(f1);
        geojsonResulted = stringer.toString();
        assertTrue(geojsonExpected.equals(geojsonResulted));

        // encodeFeatureCollection test
        stringer = new JSONStringer();
        builder = new MfGeoJSON(stringer);
        f1 = new MfFeature() {
            public String getFeatureId() {
                return "fid_foo";
            }
            public MfGeometry getMfGeometry() {
                return new MfGeometry(
                    new GeometryFactory().createPoint(coord1));
            }
            public void toJSON(JSONBuilder builder) {
                builder.key("prop_foo").value("foo");
            }
        };
        f2 = new MfFeature() {
            public String getFeatureId() {
                return "fid_bar";
            }
            public MfGeometry getMfGeometry() {
                return new MfGeometry(
                    new GeometryFactory().createPoint(coord2));
            }
            public void toJSON(JSONBuilder builder) {
                builder.key("prop_bar").value("bar");
            }
        };
        LinkedList<MfFeature> ll = new LinkedList<MfFeature>();
        ll.add(f1);
        ll.add(f2);
        fc = new MfFeatureCollection(ll);
        geojsonExpected =
            "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"fid_foo\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.1,2.2]},\"properties\":{\"prop_foo\":\"foo\"}},{\"type\":\"Feature\",\"id\":\"fid_bar\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[3.3,4.4]},\"properties\":{\"prop_bar\":\"bar\"}}]}";
        builder.encodeFeatureCollection(fc);
        geojsonResulted = stringer.toString();
        assertTrue(geojsonExpected.equals(geojsonResulted));
    }
}
