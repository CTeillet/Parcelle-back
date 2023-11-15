package com.teillet.parcelle.service;

import com.teillet.parcelle.model.Parcelle;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IParcelleService {
    Long nombreParcelle();

    List<Parcelle> enregistrementLotParcelle(List<Parcelle> parcelles);

    boolean suppressionParcelles(List<String> ids);

    @Async
    CompletableFuture<List<Parcelle>> recuperationParcellesNonSupprimees();

    @Async
    CompletableFuture<Parcelle> recuperationParcelleParId(String id);

    List<Parcelle> recuperationParcellesNonLieesAdresse();

    void enregistrementParcelle(Parcelle parcelle);

    Long nombreParcelleLieesAdresse();

    CompletableFuture<List<Parcelle>> recuperationParcellesParDestinationPrincipaleEtSupprime(String destinationPrincipale, boolean supprimer);
}
