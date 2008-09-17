/*
 * Copyright (C) 2008  Camptocamp
 *
 * This file is part of MapFish Server
 *
 * MapFish Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MapFish Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MapFish Server.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mapfish.geo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import junit.framework.TestCase;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.util.ArrayList;

public class MfGeoJSONReaderTest extends TestCase {
    private final MfGeoFactory mfFactory = new MfGeoFactory() {
        public MfFeature createFeature(String id, MfGeometry geometry, JSONObject properties) {
            return new MyFeature(id, geometry, properties);
        }

    };

    private final MfGeoJSONReader reader = new MfGeoJSONReader(mfFactory);

    public MfGeoJSONReaderTest(String name) {
        super(name);
    }

    public void testPoint() throws JSONException {
        MfGeo result = reader.decode("{ \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }");
        assertSame(MfGeometry.class, result.getClass());
        MfGeometry geom = (MfGeometry) result;
        Geometry jts = geom.getInternalGeometry();
        assertSame(Point.class, jts.getClass());
        Point point = (Point) jts;
        assertEquals(100.0, point.getX());
        assertEquals(0.0, point.getY());
    }

    public void testLineString() throws JSONException {
        MfGeo result = reader.decode("{ \"type\": \"LineString\", \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ] }");
        assertSame(MfGeometry.class, result.getClass());
        MfGeometry geom = (MfGeometry) result;
        Geometry jts = geom.getInternalGeometry();
        assertSame(LineString.class, jts.getClass());
        LineString lineString = (LineString) jts;
        assertEquals(2, lineString.getNumPoints());
        assertEquals(new Coordinate(100.0, 0.0), lineString.getPointN(0).getCoordinate());
        assertEquals(new Coordinate(101.0, 1.0), lineString.getPointN(1).getCoordinate());
    }

    public void testPolygonNoHoles() throws JSONException {
        MfGeo result = reader.decode("{ \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ] }");
        assertSame(MfGeometry.class, result.getClass());
        MfGeometry geom = (MfGeometry) result;
        Geometry jts = geom.getInternalGeometry();
        assertSame(Polygon.class, jts.getClass());
        Polygon polygon = (Polygon) jts;
        assertEquals(5, polygon.getExteriorRing().getNumPoints());
        assertEquals(new Coordinate(100.0, 0.0), polygon.getExteriorRing().getPointN(0).getCoordinate());
        assertEquals(new Coordinate(101.0, 0.0), polygon.getExteriorRing().getPointN(1).getCoordinate());
        assertEquals(new Coordinate(101.0, 1.0), polygon.getExteriorRing().getPointN(2).getCoordinate());
        assertEquals(new Coordinate(100.0, 1.0), polygon.getExteriorRing().getPointN(3).getCoordinate());
        assertEquals(new Coordinate(100.0, 0.0), polygon.getExteriorRing().getPointN(4).getCoordinate());
        assertEquals(0, polygon.getNumInteriorRing());
    }

    public void testPolygonWithHoles() throws JSONException {
        MfGeo result = reader.decode("{ \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ], [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ] ] }");
        assertSame(MfGeometry.class, result.getClass());
        MfGeometry geom = (MfGeometry) result;
        Geometry jts = geom.getInternalGeometry();
        assertSame(Polygon.class, jts.getClass());
        Polygon polygon = (Polygon) jts;
        assertEquals(5, polygon.getExteriorRing().getNumPoints());
        assertEquals(new Coordinate(100.0, 0.0), polygon.getExteriorRing().getPointN(0).getCoordinate());
        assertEquals(new Coordinate(101.0, 0.0), polygon.getExteriorRing().getPointN(1).getCoordinate());
        assertEquals(new Coordinate(101.0, 1.0), polygon.getExteriorRing().getPointN(2).getCoordinate());
        assertEquals(new Coordinate(100.0, 1.0), polygon.getExteriorRing().getPointN(3).getCoordinate());
        assertEquals(new Coordinate(100.0, 0.0), polygon.getExteriorRing().getPointN(4).getCoordinate());
        assertEquals(1, polygon.getNumInteriorRing());

        final LineString hole = polygon.getInteriorRingN(0);
        assertEquals(5, hole.getNumPoints());
        assertEquals(new Coordinate(100.2, 0.2), hole.getPointN(0).getCoordinate());
        assertEquals(new Coordinate(100.8, 0.2), hole.getPointN(1).getCoordinate());
        assertEquals(new Coordinate(100.8, 0.8), hole.getPointN(2).getCoordinate());
        assertEquals(new Coordinate(100.2, 0.8), hole.getPointN(3).getCoordinate());
        assertEquals(new Coordinate(100.2, 0.2), hole.getPointN(4).getCoordinate());
    }

    public void testMultiPoint() throws JSONException {
        MfGeo result = reader.decode("{ \"type\": \"MultiPoint\", \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ] }");
        assertSame(MfGeometry.class, result.getClass());
        MfGeometry geom = (MfGeometry) result;
        Geometry jts = geom.getInternalGeometry();
        assertSame(MultiPoint.class, jts.getClass());
        MultiPoint points = (MultiPoint) jts;
        assertEquals(2, points.getNumGeometries());
        assertEquals(new Coordinate(100.0, 0.0), points.getGeometryN(0).getCoordinate());
        assertEquals(new Coordinate(101.0, 1.0), points.getGeometryN(1).getCoordinate());
    }

    public void testMultiLineString() throws JSONException {
        MfGeo result = reader.decode("{ \"type\": \"MultiLineString\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 1.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ] }");
        assertSame(MfGeometry.class, result.getClass());
        MfGeometry geom = (MfGeometry) result;
        Geometry jts = geom.getInternalGeometry();
        assertSame(MultiLineString.class, jts.getClass());
        MultiLineString lineStrings = (MultiLineString) jts;
        assertEquals(2, lineStrings.getNumGeometries());

        final LineString lineString1 = (LineString) lineStrings.getGeometryN(0);
        assertEquals(2, lineString1.getNumPoints());
        assertEquals(new Coordinate(100.0, 0.0), lineString1.getPointN(0).getCoordinate());
        assertEquals(new Coordinate(101.0, 1.0), lineString1.getPointN(1).getCoordinate());

        final LineString lineString2 = (LineString) lineStrings.getGeometryN(1);
        assertEquals(2, lineString2.getNumPoints());
        assertEquals(new Coordinate(102.0, 2.0), lineString2.getPointN(0).getCoordinate());
        assertEquals(new Coordinate(103.0, 3.0), lineString2.getPointN(1).getCoordinate());
    }

    public void testMultiPolygonNoHoles() throws JSONException {
        MfGeo result = reader.decode("{ \"type\": \"MultiPolygon\", \"coordinates\": [ [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]], [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]], [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]] ] }");
        assertSame(MfGeometry.class, result.getClass());
        MfGeometry geom = (MfGeometry) result;
        Geometry jts = geom.getInternalGeometry();
        assertSame(MultiPolygon.class, jts.getClass());
        MultiPolygon polygons = (MultiPolygon) jts;
        assertEquals(2, polygons.getNumGeometries());

        Polygon polygon1= (Polygon) polygons.getGeometryN(0);
        assertEquals(5, polygon1.getExteriorRing().getNumPoints());
        assertEquals(new Coordinate(102.0, 2.0), polygon1.getExteriorRing().getPointN(0).getCoordinate());
        assertEquals(new Coordinate(103.0, 2.0), polygon1.getExteriorRing().getPointN(1).getCoordinate());
        assertEquals(new Coordinate(103.0, 3.0), polygon1.getExteriorRing().getPointN(2).getCoordinate());
        assertEquals(new Coordinate(102.0, 3.0), polygon1.getExteriorRing().getPointN(3).getCoordinate());
        assertEquals(new Coordinate(102.0, 2.0), polygon1.getExteriorRing().getPointN(4).getCoordinate());
        assertEquals(0, polygon1.getNumInteriorRing());

        Polygon polygon2= (Polygon) polygons.getGeometryN(1);
        assertEquals(5, polygon2.getExteriorRing().getNumPoints());
        assertEquals(new Coordinate(100.0, 0.0), polygon2.getExteriorRing().getPointN(0).getCoordinate());
        assertEquals(new Coordinate(101.0, 0.0), polygon2.getExteriorRing().getPointN(1).getCoordinate());
        assertEquals(new Coordinate(101.0, 1.0), polygon2.getExteriorRing().getPointN(2).getCoordinate());
        assertEquals(new Coordinate(100.0, 1.0), polygon2.getExteriorRing().getPointN(3).getCoordinate());
        assertEquals(new Coordinate(100.0, 0.0), polygon2.getExteriorRing().getPointN(4).getCoordinate());
        assertEquals(1, polygon2.getNumInteriorRing());

        final LineString hole = polygon2.getInteriorRingN(0);
        assertEquals(5, hole.getNumPoints());
        assertEquals(new Coordinate(100.2, 0.2), hole.getPointN(0).getCoordinate());
        assertEquals(new Coordinate(100.8, 0.2), hole.getPointN(1).getCoordinate());
        assertEquals(new Coordinate(100.8, 0.8), hole.getPointN(2).getCoordinate());
        assertEquals(new Coordinate(100.2, 0.8), hole.getPointN(3).getCoordinate());
        assertEquals(new Coordinate(100.2, 0.2), hole.getPointN(4).getCoordinate());
    }

    public void testGeometryCollection() throws JSONException {
        MfGeo result = reader.decode("{ \"type\": \"GeometryCollection\", \"geometries\": [ { \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }, { \"type\": \"LineString\", \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ] } ] }");
        assertSame(MfGeometry.class, result.getClass());
        MfGeometry geom = (MfGeometry) result;
        Geometry jts = geom.getInternalGeometry();
        assertSame(GeometryCollection.class, jts.getClass());
        GeometryCollection collection = (GeometryCollection) jts;
        Point point= (Point) collection.getGeometryN(0);
        assertEquals(100.0, point.getX());
        assertEquals(0.0, point.getY());

        LineString lineString = (LineString) collection.getGeometryN(1);
        assertEquals(2, lineString.getNumPoints());
        assertEquals(new Coordinate(101.0, 0.0), lineString.getPointN(0).getCoordinate());
        assertEquals(new Coordinate(102.0, 1.0), lineString.getPointN(1).getCoordinate());
    }

    public void testFeature() throws JSONException {
        MfGeo result = reader.decode("{ \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"} }");
        assertSame(MyFeature.class, result.getClass());
        MyFeature feature = (MyFeature) result;
        MfGeometry geom = feature.getMfGeometry();
        Geometry jts = geom.getInternalGeometry();
        assertSame(Point.class, jts.getClass());
        Point point = (Point) jts;
        assertEquals(102.0, point.getX());
        assertEquals(0.5, point.getY());

        assertNull(feature.id);
        assertEquals(1, feature.properties.length());
        assertEquals("value0", feature.properties.getString("prop0"));
    }

    public void testFeatureCollection() throws JSONException {
        MfGeo result = reader.decode("{ \"type\": \"FeatureCollection\", \"features\": [ { \"type\": \"Feature\", id: \"toto\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"} }, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ] }, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0 } } ] }");
        assertSame(MfFeatureCollection.class, result.getClass());
        MfFeatureCollection collection= (MfFeatureCollection) result;
        
        final ArrayList<MfFeature> coll = (ArrayList<MfFeature>) collection.getCollection();
        assertEquals(2, coll.size());

        assertSame(MyFeature.class, coll.get(0).getClass());
        MyFeature feature1 = (MyFeature) coll.get(0);
        MfGeometry geom1 = feature1.getMfGeometry();
        Geometry jts = geom1.getInternalGeometry();
        assertSame(Point.class, jts.getClass());
        Point point = (Point) jts;
        assertEquals(102.0, point.getX());
        assertEquals(0.5, point.getY());
        assertEquals("toto", feature1.id);
        assertEquals(1, feature1.properties.length());
        assertEquals("value0", feature1.properties.getString("prop0"));

        assertSame(MyFeature.class, coll.get(1).getClass());
        MyFeature feature2 = (MyFeature) coll.get(1);
        MfGeometry geom2 = feature2.getMfGeometry();
        Geometry jts2 = geom2.getInternalGeometry();
        assertSame(LineString.class, jts2.getClass());
        LineString lineString = (LineString) jts2;
        assertEquals(4, lineString.getNumPoints());
        assertEquals(new Coordinate(102.0, 0.0), lineString.getPointN(0).getCoordinate());
        assertEquals(new Coordinate(103.0, 1.0), lineString.getPointN(1).getCoordinate());
        assertEquals(new Coordinate(104.0, 0.0), lineString.getPointN(2).getCoordinate());
        assertEquals(new Coordinate(105.0, 1.0), lineString.getPointN(3).getCoordinate());
        assertNull(feature2.id);
        assertEquals(2, feature2.properties.length());
        assertEquals("value0", feature2.properties.getString("prop0"));
        assertEquals(0.0, feature2.properties.getDouble("prop1"));
    }

    private class MyFeature extends MfFeature {
        private final String id;
        private final MfGeometry geometry;
        private final JSONObject properties;

        public MyFeature(String id, MfGeometry geometry, JSONObject properties) {
            this.id = id;
            this.geometry = geometry;
            this.properties = properties;
        }

        public String getFeatureId() {
            return id;
        }

        public MfGeometry getMfGeometry() {
            return geometry;
        }

        public void toJSON(JSONWriter builder) throws JSONException {
            throw new RuntimeException("Not implemented");
        }
    }
}
