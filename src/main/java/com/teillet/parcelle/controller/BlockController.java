package com.teillet.parcelle.controller;

import com.teillet.parcelle.dto.PlotClusterDto;
import com.teillet.parcelle.mapper.BlockMapper;
import com.teillet.parcelle.model.Block;
import com.teillet.parcelle.repository.PlotRepository;
import com.teillet.parcelle.service.impl.BlockService;
import com.teillet.parcelle.utils.PlotClusterUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/private/block")
public class BlockController {
    private final BlockService blockService;
    private final PlotRepository plotRepository;

    @GetMapping("/generatePateTemporaires")
    public ResponseEntity<String> generatePate() throws IOException {
        log.info("Génération des pates temporaires");
        List<PlotClusterDto> pate = blockService.generationPateTemporaires();
        log.info("Génération des pates temporaires terminée");
        return ResponseEntity.ok(PlotClusterUtils.plotsClusterToGeoJson(pate));
    }

//    generate a new Block from a list of plots
    @PostMapping("/generate")
    public ResponseEntity<String> generateBlock(@RequestBody List<String> ids) throws IOException {
        log.info("Génération des blocs");
        List<PlotClusterDto> blocks = blockService.generateBlock(ids);
        List<Block> savedBlocks = blockService.saveBlocks(BlockMapper.MAPPER.plotClusterDtosToEntity(blocks, plotRepository));
        log.info("Génération des blocs terminée : {}", savedBlocks.size());
        return ResponseEntity.ok(PlotClusterUtils.plotsClusterToGeoJson(blocks));
    }

}
