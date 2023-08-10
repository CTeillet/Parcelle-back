package com.teillet.parcelle.mapper;

import com.teillet.parcelle.dto.ParcelleDto;
import com.teillet.parcelle.model.Commune;
import com.teillet.parcelle.model.Parcelle;
import com.teillet.parcelle.repository.CommuneRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParcelleMapper {

    ParcelleMapper MAPPER = Mappers.getMapper(ParcelleMapper.class);

    @Mapping(target = "adresse", ignore = true)
    @Mapping(target = "supprime", constant = "false")
    @Mapping(target = "pate", ignore = true)
    @Mapping(target = "surface", source = "contenance")
    Parcelle toEntity(ParcelleDto parcelleDto, @Context CommuneRepository communeRepository);

    default Commune toCommune(String inseeCom, @Context CommuneRepository communeRepository) {
        return communeRepository.findById(inseeCom).orElse(null);
    }

}
