package com.teillet.parcelle.mapper;

import com.teillet.parcelle.dto.AddressFileDto;
import com.teillet.parcelle.model.Address;
import com.teillet.parcelle.model.City;
import com.teillet.parcelle.repository.CityRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {

	AddressMapper MAPPER = Mappers.getMapper(AddressMapper.class);

	@Mapping(target = "pseudoNumber", source = "pseudoNumero")
	@Mapping(target = "plotIds", ignore = true)
	@Mapping(target = "originalNameLane", source = "nomVoieOriginal")
	@Mapping(target = "number", source = "numero")
	@Mapping(target = "mainDestination", source = "destinationPrincipale")
	@Mapping(target = "laneNameType", source = "nomVoieFantoir")
	@Mapping(target = "laneNameFantoir", source = "nomVoieFantoir")
	@Mapping(target = "laneName", source = "nomVoie")
	@Mapping(target = "fullNumber", source = "numeroComplet")
	@Mapping(target = "city", source = "codeCommune")
	Address toEntity(AddressFileDto addressFileDto, @Context CityRepository cityRepository);

	default City toCity(String cityCode, @Context CityRepository cityRepository) {
		return cityRepository.findById(cityCode).orElse(null);
	}
}
