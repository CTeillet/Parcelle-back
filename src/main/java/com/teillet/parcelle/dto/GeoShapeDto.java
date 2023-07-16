package com.teillet.parcelle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeoShapeDto {
	private String type;
	private GeometryDto geometry;
	private Object properties;
}
