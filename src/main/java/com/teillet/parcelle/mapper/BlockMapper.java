package com.teillet.parcelle.mapper;

import com.teillet.parcelle.dto.BlockDto;
import com.teillet.parcelle.dto.PlotClusterDto;
import com.teillet.parcelle.model.Block;
import com.teillet.parcelle.model.Plot;
import com.teillet.parcelle.repository.PlotRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BlockMapper {
	BlockMapper MAPPER = Mappers.getMapper(BlockMapper.class);

	Block toEntity(BlockDto blockDto);

	@Mapping(target = "territory", ignore = true)
	@Mapping(target = "plots", source = "intersectingIds")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "geom", source = "geom")
	Block plotClusterDtoToEntity(PlotClusterDto plotClusterDto, @Context PlotRepository plotRepository);

	@Mapping(target = "territory", ignore = true)
	@Mapping(target = "plots", source = "intersectingIds")
	@Mapping(target = "id", ignore = true)
	List<Block> plotClusterDtosToEntity(List<PlotClusterDto> plotClusterDto, @Context PlotRepository plotRepository);

	default List<Plot> idsToPlots(List<String> ids, @Context PlotRepository plotRepository) {
		return plotRepository.findAllById(ids);
	}
}
