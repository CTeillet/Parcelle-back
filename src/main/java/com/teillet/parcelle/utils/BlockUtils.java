package com.teillet.parcelle.utils;

import com.teillet.parcelle.model.Block;
import org.geotools.data.simple.SimpleFeatureCollection;

import java.io.IOException;
import java.util.List;

import static com.teillet.parcelle.utils.GeoJsonUtils.createBlockFeatureType;
import static com.teillet.parcelle.utils.GeoJsonUtils.createFeatureCollection;

public class BlockUtils {
	public static String blocksToGeoJson(List<Block> blocks) throws IOException {
		// Créer une collection des entités GeoJSON
		SimpleFeatureCollection featureCollection = createFeatureCollection(blocks, createBlockFeatureType(), GeoJsonUtils::createBlockSimpleFeature);

		// Convertir la collection des entités en GeoJSON
		return GeoJsonUtils.convertToGeoJSON(featureCollection);
	}
}
