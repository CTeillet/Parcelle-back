package com.teillet.parcelle.utils;

import com.teillet.parcelle.model.Plot;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.simple.SimpleFeatureCollection;

import java.io.IOException;
import java.util.List;

public class PlotUtils {

	private PlotUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String plotsToGeoJson(List<Plot> plots) throws IOException {
		// Créer la structure du schéma des entités GeoJSON
		SimpleFeatureType featureType = GeoJsonUtils.createPlotFeatureType();

		// Créer une collection des entités GeoJSON
		SimpleFeatureCollection featureCollection = GeoJsonUtils.createFeatureCollection(plots, featureType, GeoJsonUtils::createPlotSimpleFeature);

		// Convertir la collection des entités en GeoJSON
		return GeoJsonUtils.convertToGeoJSON(featureCollection);
	}

	public static String plotToGeoJson(Plot p) throws IOException {
		return plotsToGeoJson(List.of(p));
	}

}
