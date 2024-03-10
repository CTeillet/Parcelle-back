package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.dto.PlotClusterDto;
import com.teillet.parcelle.model.Block;
import com.teillet.parcelle.repository.BlockRepository;
import com.teillet.parcelle.repository.PlotRepository;
import com.teillet.parcelle.service.IBlockService;
import com.teillet.parcelle.utils.PlotClusterUtils;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockService implements IBlockService {
    private final PlotRepository plotRepository;
    private final BlockRepository blockRepository;

    @Override
    public List<PlotClusterDto> generationPateTemporaires() {
        log.info("Génération des pates temporaires");
        List<PlotClusterDto> plotClusterDtos = PlotClusterUtils.getPlotClusters(plotRepository.getPlotClustersQuery());
        log.info("Génération des pates temporaires terminée");
        return plotClusterDtos;
    }

    @Override
    public List<PlotClusterDto> generateBlock(List<String> plotIds) {
        log.info("Génération du pâté avec les parcelles " + plotIds);
        List<PlotClusterDto> plotClusterDto = PlotClusterUtils.getPlotClusters(plotRepository.getPlotCluster(plotIds));
        log.info("Génération du pâté terminée");
        return plotClusterDto;
    }

    @Override
    public List<Block> saveBlocks(List<Block> blocks) {
        log.info("Sauvegarde des blocs");
        List<Block> savedBlocks = blockRepository.saveAll(blocks);
        blocks.stream().map(block -> block.getPlots().stream().peek(plot -> plot.setBlock(block)).toList()).forEach(plotRepository::saveAll);
        log.info("Sauvegarde des blocs terminée");
        return savedBlocks;
    }

    @Override
    @Observed(name = "block.service.getAllBlocks")
    public CompletableFuture<List<Block>> getAllBlocks() {
	    return CompletableFuture.completedFuture(blockRepository.findAll());
    }

}
