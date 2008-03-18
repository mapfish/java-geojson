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
 * MfFeature.java
 *
 * Created on January 21, 2008, 8:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mapfish.geo;

import net.sf.json.util.JSONBuilder;

/**
 *
 * @author Eric Lemoine, Camptocamp.
 */
public abstract class MfFeature implements MfGeo {
    private final GeoType geoType;
    
    /**
     * Creates a new instance of MfFeature
     */
    protected MfFeature() {
        geoType = GeoType.FEATURE;
    }
    
    public GeoType getGeoType() {
        return geoType;
    }
    
    // Subclasses must implement these methods. -----------

    public abstract String getFeatureId();
    public abstract MfGeometry getMfGeometry();

    /**
     * Add the "key" and "value" pairs to the provided builder.
     */
    public abstract void toJSON(JSONBuilder builder);
}
