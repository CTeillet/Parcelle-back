package com.teillet.parcelle.controller;

import com.teillet.parcelle.model.Plot;
import com.teillet.parcelle.service.IPlotService;
import com.teillet.parcelle.utils.PlotUtils;
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
@RequestMapping("/api/private/parcelle")
public class ParcelleController {
    private final IPlotService parcelleService;

    @GetMapping
    public ResponseEntity<String> getParcelle() throws IOException, ExecutionException, InterruptedException {
        log.info("Récupération des parcelles");
        CompletableFuture<List<Plot>> parcelles = parcelleService.getNonDeletedPlots();
        String string = PlotUtils.parcellesToGeoJson(parcelles.get());
        log.info("Récupération des parcelles terminée");
        return ResponseEntity.ok(string);
    }

    //Delete a list of parcelles by id of String
    @DeleteMapping
    public ResponseEntity<String> deleteParcelles(@RequestBody List<String> ids) {
        log.info("Suppression des parcelles " + ids);
        if (parcelleService.deletePlots(ids)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    //Get one parcelle
    @GetMapping("/{id}")
    public ResponseEntity<String> getParcelleById(@PathVariable String id) throws IOException, ExecutionException, InterruptedException {
        log.info("Récupération de la parcelle " + id);
        CompletableFuture<Plot> parcelle = parcelleService.getPlotById(id);
        String string = PlotUtils.parcelleToGeoJson(parcelle.get());
        return ResponseEntity.ok(string);
    }

    // Récupération des parcelles qui ont comme adresse la valeur destinationPrincipale en param et la valeur donnée pour supprimer
    @GetMapping("/{destinationPrincipale}/{supprimer}")
    public ResponseEntity<String> getParcelleByDestinationPrincipaleAndSupprime(@PathVariable String destinationPrincipale, @PathVariable boolean supprimer) throws IOException, ExecutionException, InterruptedException {
        log.info("Récupération des parcelles avec destinationPrincipale = " + destinationPrincipale + " et supprimer = " + supprimer);
        CompletableFuture<List<Plot>> parcelles = parcelleService.recuperationParcellesParDestinationPrincipaleEtSupprime(destinationPrincipale, supprimer);
        String string = PlotUtils.parcellesToGeoJson(parcelles.get());
        return ResponseEntity.ok(string);
    }

}
