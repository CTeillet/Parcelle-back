package com.teillet.parcelle.mapper;

import com.teillet.parcelle.dto.AdresseDto;
import com.teillet.parcelle.model.Adresse;
import com.teillet.parcelle.model.Commune;
import com.teillet.parcelle.repository.CommuneRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdresseMapper {

	AdresseMapper MAPPER = Mappers.getMapper(AdresseMapper.class);

	@Mapping(target = "commune", source = "codeCommune")
	Adresse toEntity(AdresseDto adresseDto, @Context CommuneRepository communeRepository);

	default Commune toCommune(String codeCommune, @Context CommuneRepository communeRepository) {
		return communeRepository.findById(codeCommune).orElse(null);
	}
}
