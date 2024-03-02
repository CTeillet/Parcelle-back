package com.teillet.parcelle.utils;

import com.teillet.parcelle.dto.PlotFileDto;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Polygon;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("SameParameterValue")
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

    // Convertir un itérable en flux
    public static Stream<SimpleFeature> toStream(SimpleFeatureIterator iterator) {
        Iterable<SimpleFeature> iterable = () -> new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public SimpleFeature next() {
                return iterator.next();
            }
        };

        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static PlotFileDto transformSimpleFeatureToParcelleDto(SimpleFeature feature) {
	    return PlotFileDto.builder()
                .id(getStringAttribute(feature, "id"))
                .commune(getStringAttribute(feature, "commune"))
                .prefixe(getStringAttribute(feature, "prefixe"))
                .section(getStringAttribute(feature, "section"))
                .numero(getStringAttribute(feature, "numero"))
                .contenance(getIntegerAttribute(feature, "contenance"))
                .arpente(getBooleanAttribute(feature, "arpente"))
                .created(getDateAttribute(feature, "created"))
                .updated(getDateAttribute(feature, "updated"))
                .geom(getPolygonAttribute(feature, "geometry"))
                .build();
    }

    private static String getStringAttribute(SimpleFeature feature, String attributeName) {
        return (String) feature.getAttribute(attributeName);
    }

    private static Integer getIntegerAttribute(SimpleFeature feature, String attributeName) {
        Object value = feature.getAttribute(attributeName);
        return value != null ? Integer.parseInt(value.toString()) : null;
    }

    private static Boolean getBooleanAttribute(SimpleFeature feature, String attributeName) {
        Object value = feature.getAttribute(attributeName);
        return value != null ? (Boolean) value : null;
    }

    private static Date getDateAttribute(SimpleFeature feature, String attributeName) {
        return (Date) feature.getAttribute(attributeName);
    }

    private static Polygon getPolygonAttribute(SimpleFeature feature, String attributeName) {
        return (Polygon) feature.getAttribute(attributeName);
    }
}
