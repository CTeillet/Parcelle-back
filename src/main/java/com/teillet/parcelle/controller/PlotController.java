package com.teillet.parcelle.controller;

import com.teillet.parcelle.model.Plot;
import com.teillet.parcelle.service.IPlotService;
import com.teillet.parcelle.utils.PlotUtils;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/private/plot")
public class PlotController {
    private final IPlotService plotService;

    @GetMapping
    @Observed(name = "plot.getPlots")
    public ResponseEntity<String> getPlots() throws IOException, ExecutionException, InterruptedException {
        log.info("Récupération des parcelles");
        CompletableFuture<List<Plot>> plots = plotService.getNonDeletedPlots();
        String string = PlotUtils.plotsToGeoJson(plots.get());
        return ResponseEntity.ok(string);
    }

    //Delete a list of plots by id of String
    @DeleteMapping
    @Observed(name = "plot.deletePlots")
    public ResponseEntity<String> deletePlots(@RequestBody List<String> ids) {
        log.info("Suppression des parcelles {}", ids);
        if (plotService.deletePlots(ids)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    //Get one parcelle
    @GetMapping("/{id}")
    @Observed(name = "plot.getPlotById")
    public ResponseEntity<String> getPlotById(@PathVariable String id) throws IOException, ExecutionException, InterruptedException {
        log.info("Récupération de la parcelle {}", id);
        CompletableFuture<Plot> plot = plotService.getPlotById(id);
        String string = PlotUtils.plotToGeoJson(plot.get());
        return ResponseEntity.ok(string);
    }

    // Récupération des parcelles qui ont comme adresse la valeur destinationPrincipale en param et la valeur donnée pour supprimer
    @GetMapping("/{destinationPrincipale}/{supprimer}")
    @Observed(name = "plot.getPlotsByMainDestinationAndDeleted")
    public ResponseEntity<String> getPlotsByMainDestinationAndDeleted(@PathVariable String destinationPrincipale, @PathVariable boolean supprimer) throws IOException, ExecutionException, InterruptedException {
        log.info("Récupération des parcelles avec destinationPrincipale = {} et supprimer = {}", destinationPrincipale, supprimer);
        CompletableFuture<List<Plot>> plots = plotService.getPlotsByMainDestinationAndDeleted(destinationPrincipale, supprimer);
        String string = PlotUtils.plotsToGeoJson(plots.get());
        return ResponseEntity.ok(string);
    }

}
