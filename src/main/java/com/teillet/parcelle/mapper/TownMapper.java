package com.teillet.parcelle.mapper;

import com.teillet.parcelle.dto.CommuneDto;
import com.teillet.parcelle.model.Commune;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TownMapper {
	TownMapper MAPPER = Mappers.getMapper(TownMapper.class);

	@Mapping(target = "plots", ignore = true)
	Commune toEntity(CommuneDto communeDto);

	List<Commune> toEntity(List<CommuneDto> communeDtos);

	CommuneDto toDto(Commune commune);
}
