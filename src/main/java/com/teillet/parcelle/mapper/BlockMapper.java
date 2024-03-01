package com.teillet.parcelle.mapper;

import com.teillet.parcelle.dto.BlockDto;
import com.teillet.parcelle.model.Block;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlockMapper {
	BlockMapper MAPPER = Mappers.getMapper(BlockMapper.class);

	Block toEntity(BlockDto blockDto);
}
