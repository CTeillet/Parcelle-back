package com.teillet.parcelle.service;

import com.teillet.parcelle.model.Plot;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IPlotService {
    Long plotNumber();

    boolean deletePlots(List<String> ids);

    @Async
    CompletableFuture<List<Plot>> getNonDeletedPlots();

    @Async
    CompletableFuture<Plot> getPlotById(String id);

    List<Plot> recuperationParcellesNonLieesAdresse();

    void savePlot(Plot plot);

    Long nombreParcelleLieesAdresse();

    CompletableFuture<List<Plot>> recuperationParcellesParDestinationPrincipaleEtSupprime(String destinationPrincipale, boolean supprimer);
}
