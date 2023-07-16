package com.teillet.parcelle.mapper;

import com.teillet.parcelle.dto.CommuneDto;
import com.teillet.parcelle.model.Commune;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommuneMapper {
	CommuneMapper MAPPER = Mappers.getMapper(CommuneMapper.class);

	@Mapping(target = "parcelles", ignore = true)
	Commune toEntity(CommuneDto communeDto);

	List<Commune> toEntity(List<CommuneDto> communeDtos);
}
