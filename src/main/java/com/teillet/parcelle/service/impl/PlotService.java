package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.model.Plot;
import com.teillet.parcelle.repository.ParcelleRepository;
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
    private final ParcelleRepository parcelleRepository;

    @Override
    public Long plotNumber() {
        return parcelleRepository.count();
    }

	@Override
    public boolean deletePlots(List<String> ids) {
        log.info("Nombre de parcelles à supprimer : " + ids.size());
        int nbParcellesSupprimees = parcelleRepository.updateSupprimeByIdIn(ids);
        log.info("Nombre de parcelles supprimées : " + nbParcellesSupprimees);
        return nbParcellesSupprimees == ids.size();
    }

    @Override
    public CompletableFuture<List<Plot>> getNonDeletedPlots() {
        return CompletableFuture.completedFuture(parcelleRepository.findBySupprimeFalse());
    }

    @Override
    public CompletableFuture<Plot> getPlotById(String id) {
        return CompletableFuture.completedFuture(parcelleRepository.findById(id).orElse(null));
    }

    @Override
    public List<Plot> recuperationParcellesNonLieesAdresse() {
        return parcelleRepository.findByAdresseIsNull();
    }

    @Override
    public void savePlot(Plot plot) {
        log.info("Enregistrement de la parcelle " + plot.getId());
        parcelleRepository.save(plot);
        log.info("Fin enregistrement de la parcelle " + plot.getId());
    }

    @Override
    public Long nombreParcelleLieesAdresse() {
        return parcelleRepository.countByAdresseIsNotNull();
    }

    @Override
    public CompletableFuture<List<Plot>> recuperationParcellesParDestinationPrincipaleEtSupprime(String destinationPrincipale, boolean supprimer) {
        log.info("Récupération des parcelles avec destinationPrincipale = {} et supprimer = {}", destinationPrincipale, supprimer);
        return CompletableFuture.completedFuture(parcelleRepository.findByAdresse_DestinationPrincipaleAndSupprime(destinationPrincipale, supprimer));
    }
}
