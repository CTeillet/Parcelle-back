package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.model.Plot;
import com.teillet.parcelle.repository.PlotRepository;
import com.teillet.parcelle.service.IPlotService;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
public class PlotService implements IPlotService {
    private final PlotRepository plotRepository;

    @Override
    public Long plotNumber() {
        return plotRepository.count();
    }

	@Override
    @Observed(name = "plot.deletePlots")
    public boolean deletePlots(List<String> ids) {
        log.info("Nombre de parcelles à supprimer : {}", ids.size());
        int nbDeletedPlots = plotRepository.updateDeletedByIdIn(ids);
        log.info("Nombre de parcelles supprimées : {}", nbDeletedPlots);
        return nbDeletedPlots == ids.size();
    }

    @Override
    @Observed(name = "plot.getNonDeletedPlots")
    public CompletableFuture<List<Plot>> getNonDeletedPlots() {
        return CompletableFuture.completedFuture(plotRepository.findByDeletedFalse());
    }

    @Override
    @Observed(name = "plot.getPlotById")
    public CompletableFuture<Plot> getPlotById(String id) {
        return CompletableFuture.completedFuture(plotRepository.findById(id).orElse(null));
    }

    @Override
    @Observed(name = "plot.savePlots")
    public void savePlots(List<Plot> plots) {
        log.info("Enregistrement de {} parcelles", plots.size());
        plotRepository.saveAll(plots);
        log.info("Fin enregistrement de {} parcelles", plots.size());
    }

    @Override
    @Observed(name = "plot.getPlotsByMainDestinationAndDeleted")
    public CompletableFuture<List<Plot>> getPlotsByMainDestinationAndDeleted(String destinationPrincipale, boolean supprimer) {
        log.info("Récupération des parcelles avec destinationPrincipale = {} et supprimer = {}", destinationPrincipale, supprimer);
        return CompletableFuture.completedFuture(plotRepository.findByAddresses_MainDestinationAndDeleted(destinationPrincipale, supprimer));
    }
}
