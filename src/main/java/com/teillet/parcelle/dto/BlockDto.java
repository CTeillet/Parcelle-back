package com.teillet.parcelle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class BlockDto {
	private final Long id;
	private final Polygon geom;
	private final TerritoryDto territory;
	private final List<PlotFileDto> plots;
}
