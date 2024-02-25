package com.teillet.parcelle.utils;

import com.teillet.parcelle.model.Address;
import com.teillet.parcelle.model.Plot;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Geometry;

import java.io.IOException;
import java.util.List;

public class PlotUtils {

    private PlotUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String parcellesToGeoJson(List<Plot> plots) throws IOException {
        // Créer la structure du schéma des entités GeoJSON
        SimpleFeatureType featureType = createFeatureType();

        // Créer une collection des entités GeoJSON
        SimpleFeatureCollection featureCollection = createFeatureCollection(plots, featureType);

        // Convertir la collection des entités en GeoJSON
        return GeoJsonUtils.convertToGeoJSON(featureCollection);
    }

    private static SimpleFeatureCollection createFeatureCollection(List<Plot> plots, SimpleFeatureType featureType) {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);

        return plots
                .stream()
                .map(plot -> createSimpleFeature(featureBuilder, plot))
                .collect(() -> new ListFeatureCollection(featureType), ListFeatureCollection::add, ListFeatureCollection::addAll);
    }

    private static SimpleFeature createSimpleFeature(SimpleFeatureBuilder featureBuilder, Plot plot) {
        featureBuilder.add(plot.getId());
        featureBuilder.add(plot.getSurface());
        featureBuilder.add(plot.getCommune().getNomCom());
        featureBuilder.add(plot.getGeom());
        featureBuilder.add(plot.getAdresse());

        return featureBuilder.buildFeature(null);
    }

    private static SimpleFeatureType createFeatureType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Parcelle");
        builder.add("id", String.class);
        builder.add("area", Integer.class);
        builder.add("town", String.class);
        builder.add("geometry", Geometry.class);
        builder.add("address", Address.class);
        return builder.buildFeatureType();
    }

    public static String parcelleToGeoJson(Plot p) throws IOException {
        return parcellesToGeoJson(List.of(p));
    }

}
