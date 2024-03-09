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
    public boolean deletePlots(List<String> ids) {
        log.info("Nombre de parcelles à supprimer : " + ids.size());
        int nbParcellesSupprimees = plotRepository.updateDeletedByIdIn(ids);
        log.info("Nombre de parcelles supprimées : " + nbParcellesSupprimees);
        return nbParcellesSupprimees == ids.size();
    }

    @Override
    @Observed(name = "plot.getNonDeletedPlots")
    public CompletableFuture<List<Plot>> getNonDeletedPlots() {
        return CompletableFuture.completedFuture(plotRepository.findByDeletedFalse());
    }

    @Override
    public CompletableFuture<Plot> getPlotById(String id) {
        return CompletableFuture.completedFuture(plotRepository.findById(id).orElse(null));
    }

    @Override
    @Observed(name = "plot.savePlots")
    public void savePlots(List<Plot> plots) {
        log.info("Enregistrement de " + plots.size() + " parcelles");
        plotRepository.saveAll(plots);
        log.info("Fin enregistrement de " + plots.size() + " parcelles");
    }

    @Override
    public CompletableFuture<List<Plot>> recuperationParcellesParDestinationPrincipaleEtSupprime(String destinationPrincipale, boolean supprimer) {
        log.info("Récupération des parcelles avec destinationPrincipale = {} et supprimer = {}", destinationPrincipale, supprimer);
        return CompletableFuture.completedFuture(plotRepository.findByAddresses_MainDestinationAndDeleted(destinationPrincipale, supprimer));
    }
}
