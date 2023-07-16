package com.teillet.parcelle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParcelleClusterDto {
	private List<String> intersectingIds;
	private Geometry geometry;
}
