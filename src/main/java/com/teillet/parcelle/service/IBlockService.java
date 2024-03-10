package com.teillet.parcelle.service;

import com.teillet.parcelle.dto.PlotClusterDto;
import com.teillet.parcelle.model.Block;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IBlockService {

	List<PlotClusterDto> generationPateTemporaires();

	List<PlotClusterDto> generateBlock(List<String> plotIds);

	List<Block> saveBlocks(List<Block> blocks);

	CompletableFuture<List<Block>> getAllBlocks();
}
