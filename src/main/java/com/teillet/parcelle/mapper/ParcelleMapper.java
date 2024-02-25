package com.teillet.parcelle.mapper;

import com.teillet.parcelle.dto.ParcelleDto;
import com.teillet.parcelle.model.Commune;
import com.teillet.parcelle.model.Plot;
import com.teillet.parcelle.repository.TownRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParcelleMapper {

    ParcelleMapper MAPPER = Mappers.getMapper(ParcelleMapper.class);

    @Mapping(target = "adresse", ignore = true)
    @Mapping(target = "supprime", constant = "false")
    @Mapping(target = "block", ignore = true)
    @Mapping(target = "surface", source = "contenance")
    Plot toEntity(ParcelleDto parcelleDto, @Context TownRepository townRepository);

    default Commune toCommune(String inseeCom, @Context TownRepository townRepository) {
        return townRepository.findById(inseeCom).orElse(null);
    }

}
