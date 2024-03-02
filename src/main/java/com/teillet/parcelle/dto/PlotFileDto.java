package com.teillet.parcelle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PlotFileDto {
	private final String id;
	private final String commune;
	private final String prefixe;
	private final String section;
	private final String numero;
	private final Integer contenance;
	private final Boolean arpente;
	private final Date created;
	private final Date updated;
	private final Polygon geom;
}
