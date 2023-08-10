package com.teillet.parcelle.utils;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;

public class GeoJsonUtils {
    public static final int DECIMAL_PRECISION = 15;

    private GeoJsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    static String convertToGeoJSON(SimpleFeatureCollection featureCollection) throws IOException {
        GeometryJSON geometryJSON = new GeometryJSON(DECIMAL_PRECISION);
        FeatureJSON featureJSON = new FeatureJSON(geometryJSON);
        return featureJSON.toString(featureCollection);
    }
}
