package com.teillet.parcelle.mapper;

import com.teillet.parcelle.dto.AddressFileDto;
import com.teillet.parcelle.model.Address;
import com.teillet.parcelle.model.City;
import com.teillet.parcelle.model.Plot;
import com.teillet.parcelle.repository.PlotRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface AddressMapper {

	AddressMapper MAPPER = Mappers.getMapper(AddressMapper.class);

	@Mapping(target = "pseudoNumber", source = "pseudoNumero")
	@Mapping(target = "plots", source = "codesParcelles")
	@Mapping(target = "originalNameLane", source = "nomVoieOriginal")
	@Mapping(target = "number", source = "numero")
	@Mapping(target = "mainDestination", source = "destinationPrincipale")
	@Mapping(target = "laneNameType", source = "nomVoieFantoir")
	@Mapping(target = "laneNameFantoir", source = "nomVoieFantoir")
	@Mapping(target = "laneName", source = "nomVoie")
	@Mapping(target = "fullNumber", source = "numeroComplet")
	@Mapping(target = "city", source = "codeCommune")
	Address toEntity(AddressFileDto addressFileDto, @Context  Map<String, City> citiesByInseeCode, @Context PlotRepository plotRepository);

	default City toCity(String cityCode, @Context Map<String, City> citiesByInseeCode) {
		return citiesByInseeCode.getOrDefault(cityCode, null);
	}

	default Set<Plot> toPlots(List<String> codesParcelles, @Context PlotRepository plotRepository) {
		return new HashSet<>( plotRepository.findAllById(codesParcelles));
	}
}
