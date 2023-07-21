package com.teillet.parcelle.utils;

import com.teillet.parcelle.dto.ParcelleClusterDto;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.util.List;

public class ParcelleClusterUtils {

    private ParcelleClusterUtils() {
    }

    public static String parcellesClusterToGeoJson(List<ParcelleClusterDto> parcelleClusterDtos) throws IOException {
        SimpleFeatureType featureType = createFeatureType();

        SimpleFeatureCollection featureCollection = createFeatureCollection(parcelleClusterDtos, featureType);

        return GeoJsonUtils.convertToGeoJSON(featureCollection);
    }

    private static SimpleFeatureType createFeatureType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("ParcelleCluster");
        builder.add("ids", List.class);
        builder.add("geometry", Geometry.class);
        return builder.buildFeatureType();
    }

    private static SimpleFeatureCollection createFeatureCollection(List<ParcelleClusterDto> parcelleClusterDtos, SimpleFeatureType featureType) {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);

        return parcelleClusterDtos
                .stream()
                .map(parcelleClusterDto -> createSimpleFeature(featureBuilder, parcelleClusterDto))
                .collect(() -> new ListFeatureCollection(featureType), ListFeatureCollection::add, ListFeatureCollection::addAll);
    }

    private static SimpleFeature createSimpleFeature(SimpleFeatureBuilder featureBuilder, ParcelleClusterDto parcelleClusterDtos) {
        featureBuilder.add(parcelleClusterDtos.getIntersectingIds());
        featureBuilder.add(parcelleClusterDtos.getGeometry());

        return featureBuilder.buildFeature(null);
    }

}
