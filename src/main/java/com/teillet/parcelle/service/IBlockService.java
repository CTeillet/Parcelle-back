package com.teillet.parcelle.service;

import com.teillet.parcelle.dto.PlotClusterDto;
import com.teillet.parcelle.model.Block;

import java.util.List;

public interface IBlockService {

	List<PlotClusterDto> generationPateTemporaires();

	List<PlotClusterDto> generateBlock(List<String> plotIds);

	List<Block> saveBlocks(List<Block> blocks);
}
