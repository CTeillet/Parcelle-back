package com.teillet.parcelle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlotClusterDto {
	private List<String> intersectingIds;
	private Polygon geom;
}
