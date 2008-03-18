/*
 * Copyright (C) 2008  Camptocamp
 *
 * This file is part of MapFish
 *
 * MapFish is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MapFish is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MapFish.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * MfGeometry.java
 *
 * Created on January 21, 2008, 8:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mapfish.geo;

import com.vividsolutions.jts.geom.Geometry;

/**
 *
 * @author Eric Lemoine, Camptocamp.
 */
public class MfGeometry implements MfGeo {
    private final GeoType geoType;
    private final Geometry jtsGeometry;
    
    /**
     * Creates a new instance of MfGeometry
     */
    public MfGeometry(Geometry jtsGeometry) {
        this.geoType = GeoType.GEOMETRY;
        this.jtsGeometry = jtsGeometry;
    }
    
    public GeoType getGeoType() {
        return geoType;
    }
    
    public Geometry getInternalGeometry() {
        return jtsGeometry;
    }
}
