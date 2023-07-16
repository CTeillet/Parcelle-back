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
public class ParcelleDto {
	private final String id;
	private final String commune;
	private final String prefixe;
	private final String section;
	private final String numero;
	private final Integer contenance;
	private final Boolean arpente;
	private Date created;
	private Date updated;
	private Polygon geom;
}
