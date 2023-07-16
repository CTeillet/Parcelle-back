package com.teillet.parcelle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PositionDto {
	private String type;
	private GeometryDto geometry;
	private String codeParcelle;
	private Double errorMargin;
}
