package com.teillet.parcelle.mapper;

import com.teillet.parcelle.dto.PlotFileDto;
import com.teillet.parcelle.model.City;
import com.teillet.parcelle.model.Plot;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlotMapper {
	PlotMapper MAPPER = Mappers.getMapper(PlotMapper.class);

	@Mapping(target = "surface", source = "contenance")
	@Mapping(target = "city", source = "commune")
	@Mapping(target = "addresses", ignore = true)
	@Mapping(target = "deleted", constant = "false")
	@Mapping(target = "block", ignore = true)
	Plot toEntity(PlotFileDto plotFileDto, @Context City city);

	default City toCity(String ignoredCommune, @Context City city) {
		return city;
	}

}
