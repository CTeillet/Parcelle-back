package com.teillet.parcelle.mapper;

import com.teillet.parcelle.dto.CityFileDto;
import com.teillet.parcelle.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CityMapper {
	CityMapper MAPPER = Mappers.getMapper(CityMapper.class);

	@Mapping(target = "zipCode", source = "codePostal")
	@Mapping(target = "inseeCityCode", source = "inseeCom")
	@Mapping(target = "countyName", source = "codeDept")
	@Mapping(target = "cityName", source = "nomDeLaCommune")
	@Mapping(target = "plots", ignore = true)
	City toEntity(CityFileDto cityFileDto);

	List<City> toEntity(List<CityFileDto> cityFileDtos);

}
