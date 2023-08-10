package com.teillet.parcelle.utils;

import com.teillet.parcelle.model.Adresse;
import com.teillet.parcelle.model.Parcelle;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.util.List;

public class ParcelleUtils {

    private ParcelleUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String parcellesToGeoJson(List<Parcelle> parcelles) throws IOException {
        // Créer la structure du schéma des entités GeoJSON
        SimpleFeatureType featureType = createFeatureType();

        // Créer une collection des entités GeoJSON
        SimpleFeatureCollection featureCollection = createFeatureCollection(parcelles, featureType);

        // Convertir la collection des entités en GeoJSON
        return GeoJsonUtils.convertToGeoJSON(featureCollection);
    }

    private static SimpleFeatureCollection createFeatureCollection(List<Parcelle> parcelles, SimpleFeatureType featureType) {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);

        return parcelles
                .stream()
                .map(parcelle -> createSimpleFeature(featureBuilder, parcelle))
                .collect(() -> new ListFeatureCollection(featureType), ListFeatureCollection::add, ListFeatureCollection::addAll);
    }

    private static SimpleFeature createSimpleFeature(SimpleFeatureBuilder featureBuilder, Parcelle parcelle) {
        featureBuilder.add(parcelle.getId());
        featureBuilder.add(parcelle.getSurface());
        featureBuilder.add(parcelle.getCommune().getNomCom());
        featureBuilder.add(parcelle.getGeom());
        featureBuilder.add(parcelle.getAdresse());

        return featureBuilder.buildFeature(null);
    }

    private static SimpleFeatureType createFeatureType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Parcelle");
        builder.add("id", String.class);
        builder.add("surface", Integer.class);
        builder.add("commune", String.class);
        builder.add("geometry", Geometry.class);
        builder.add("adresse", Adresse.class);
        return builder.buildFeatureType();
    }

    public static String parcelleToGeoJson(Parcelle p) throws IOException {
        return parcellesToGeoJson(List.of(p));
    }

}
