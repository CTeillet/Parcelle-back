package com.teillet.parcelle.mapper;

import com.teillet.parcelle.dto.AdresseDto;
import com.teillet.parcelle.model.Address;
import com.teillet.parcelle.model.Commune;
import com.teillet.parcelle.repository.TownRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {

	AddressMapper MAPPER = Mappers.getMapper(AddressMapper.class);

	@Mapping(target = "commune", source = "codeCommune")
	Address toEntity(AdresseDto adresseDto, @Context TownRepository townRepository);

	default Commune toCommune(String codeCommune, @Context TownRepository townRepository) {
		return townRepository.findById(codeCommune).orElse(null);
	}
}
