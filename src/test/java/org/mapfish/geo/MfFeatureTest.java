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

import junit.framework.TestCase;

import org.json.JSONWriter;

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
            public void toJSON(JSONWriter builder) {}

        };
        assertTrue(f.getGeoType().equals(MfGeo.GeoType.FEATURE));
    }
}
