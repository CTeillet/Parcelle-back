package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.dto.PlotClusterDto;
import com.teillet.parcelle.repository.PlotRepository;
import com.teillet.parcelle.service.IBlockService;
import com.teillet.parcelle.utils.PlotClusterUtils;
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
        List<PlotClusterDto> plotClusterDtos = PlotClusterUtils.getPlotClusters(plotRepository.getParcelleClustersQuery());
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
}
