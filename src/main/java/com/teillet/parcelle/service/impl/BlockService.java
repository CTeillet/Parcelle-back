package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.dto.PlotClusterDto;
import com.teillet.parcelle.repository.PlotRepository;
import com.teillet.parcelle.service.IBlockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockService implements IBlockService {
    private final PlotRepository plotRepository;

    @Override
    public List<PlotClusterDto> generationPateTemporaires() {
        log.info("Génération des pates temporaires");
        List<PlotClusterDto> plotClusterDtos = plotRepository.getPlotClusters();
        log.info("Génération des pates temporaires terminée");
        return plotClusterDtos;
    }

    @Override
    public List<PlotClusterDto> generateBlock(List<String> plotIds) {
        log.info("Génération du pâté avec les parcelles " + plotIds);
        List<PlotClusterDto> plotClusterDto = plotRepository.getBlock(plotIds);
        log.info("Génération du pâté terminée");
        return plotClusterDto;
    }
}
