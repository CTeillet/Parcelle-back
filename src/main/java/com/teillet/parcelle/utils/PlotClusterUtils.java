package com.teillet.parcelle.utils;

import com.teillet.parcelle.dto.PlotClusterDto;
import org.geolatte.geom.jts.JTS;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlotClusterUtils {

    private PlotClusterUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String plotsClusterToGeoJson(List<PlotClusterDto> plotClusters) throws IOException {
        SimpleFeatureType featureType = createFeatureType();

        SimpleFeatureCollection featureCollection = createFeatureCollection(plotClusters, featureType);

        return GeoJsonUtils.convertToGeoJSON(featureCollection);
    }

    private static SimpleFeatureType createFeatureType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("ParcelleCluster");
        builder.add("ids", List.class);
        builder.add("geometry", Geometry.class);
        return builder.buildFeatureType();
    }

    private static SimpleFeatureCollection createFeatureCollection(List<PlotClusterDto> plotClusters, SimpleFeatureType featureType) {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);

        return plotClusters
                .stream()
                .map(parcelleClusterDto -> createSimpleFeature(featureBuilder, parcelleClusterDto))
                .collect(() -> new ListFeatureCollection(featureType), ListFeatureCollection::add, ListFeatureCollection::addAll);
    }

    private static SimpleFeature createSimpleFeature(SimpleFeatureBuilder featureBuilder, PlotClusterDto plotClusters) {
        featureBuilder.add(plotClusters.getIntersectingIds());
        featureBuilder.add(plotClusters.getGeom());
        return featureBuilder.buildFeature(null);
    }

    public static List<PlotClusterDto> getPlotClusters(List<Object[]> parcelleClustersQuery) {
        return parcelleClustersQuery
                .parallelStream()
                .map(PlotClusterUtils::creationParcelleCluster)
                .collect(Collectors.toList());
    }

    private static PlotClusterDto creationParcelleCluster(Object[] row) {
        List<String> intersectingIds = Arrays.asList((String[]) row[0]);
        //noinspection unchecked
        Polygon jtsPolygon = JTS.to( (org.geolatte.geom.Polygon<org.geolatte.geom.Position>) row[1]);
        return new PlotClusterDto(intersectingIds, jtsPolygon);
    }
}
