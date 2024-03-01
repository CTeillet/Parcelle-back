package com.teillet.parcelle.utils;

import com.teillet.parcelle.dto.PlotClusterDto;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Geometry;

import java.io.IOException;
import java.util.List;

public class PlotClusterUtils {

    private PlotClusterUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String plotsClusterToGeoJson(List<PlotClusterDto> plotClusterDtos) throws IOException {
        SimpleFeatureType featureType = createFeatureType();

        SimpleFeatureCollection featureCollection = createFeatureCollection(plotClusterDtos, featureType);

        return GeoJsonUtils.convertToGeoJSON(featureCollection);
    }

    private static SimpleFeatureType createFeatureType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("ParcelleCluster");
        builder.add("ids", List.class);
        builder.add("geometry", Geometry.class);
        return builder.buildFeatureType();
    }

    private static SimpleFeatureCollection createFeatureCollection(List<PlotClusterDto> plotClusterDtos, SimpleFeatureType featureType) {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);

        return plotClusterDtos
                .stream()
                .map(parcelleClusterDto -> createSimpleFeature(featureBuilder, parcelleClusterDto))
                .collect(() -> new ListFeatureCollection(featureType), ListFeatureCollection::add, ListFeatureCollection::addAll);
    }

    private static SimpleFeature createSimpleFeature(SimpleFeatureBuilder featureBuilder, PlotClusterDto plotClusterDtos) {
        featureBuilder.add(plotClusterDtos.getIntersectingIds());
        featureBuilder.add(plotClusterDtos.getGeometry());

        return featureBuilder.buildFeature(null);
    }

}
