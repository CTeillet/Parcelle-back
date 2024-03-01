package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.model.Plot;
import com.teillet.parcelle.repository.PlotRepository;
import com.teillet.parcelle.service.IPlotService;
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
        int nbParcellesSupprimees = plotRepository.updateSupprimeByIdIn(ids);
        log.info("Nombre de parcelles supprimées : " + nbParcellesSupprimees);
        return nbParcellesSupprimees == ids.size();
    }

    @Override
    public CompletableFuture<List<Plot>> getNonDeletedPlots() {
        return CompletableFuture.completedFuture(plotRepository.findBySupprimeFalse());
    }

    @Override
    public CompletableFuture<Plot> getPlotById(String id) {
        return CompletableFuture.completedFuture(plotRepository.findById(id).orElse(null));
    }

    @Override
    public List<Plot> recuperationParcellesNonLieesAdresse() {
        return plotRepository.findByAdresseIsNull();
    }

    @Override
    public void savePlot(Plot plot) {
        log.info("Enregistrement de la parcelle " + plot.getId());
        plotRepository.save(plot);
        log.info("Fin enregistrement de la parcelle " + plot.getId());
    }

    @Override
    public Long nombreParcelleLieesAdresse() {
        return plotRepository.countByAdresseIsNotNull();
    }

    @Override
    public CompletableFuture<List<Plot>> recuperationParcellesParDestinationPrincipaleEtSupprime(String destinationPrincipale, boolean supprimer) {
        log.info("Récupération des parcelles avec destinationPrincipale = {} et supprimer = {}", destinationPrincipale, supprimer);
        return CompletableFuture.completedFuture(plotRepository.findByAdresse_DestinationPrincipaleAndSupprime(destinationPrincipale, supprimer));
    }
}
