package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.model.Parcelle;
import com.teillet.parcelle.repository.ParcelleRepository;
import com.teillet.parcelle.service.IParcelleService;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ParcelleService implements IParcelleService {
    private final ParcelleRepository parcelleRepository;

    @Override
    public Long nombreParcelle() {
        return parcelleRepository.count();
    }

    @Override
    @Timed(value = "parcelleService.enregistrementLotParcelle", description = "Time taken to save a list of parcelles.")
    public List<Parcelle> enregistrementLotParcelle(List<Parcelle> parcelles) {
        return parcelleRepository.saveAll(parcelles);
    }

    @Override
    @Timed(value = "parcelleService.recuperationParcelles", description = "Time taken to delete a list of parcelles.")
    public boolean suppressionParcelles(List<String> ids) {
        log.info("Nombre de parcelles à supprimer : " + ids.size());
        int nbParcellesSupprimees = parcelleRepository.updateSupprimeByIdIn(ids);
        log.info("Nombre de parcelles supprimées : " + nbParcellesSupprimees);
        return nbParcellesSupprimees == ids.size();
    }

    @Override
    @Timed(value = "parcelleService.recuperationParcellesNonSupprimees", description = "Time taken to get all parcelles not deleted.")
    public List<Parcelle> recuperationParcellesNonSupprimees() {
        return parcelleRepository.findBySupprimeFalse();
    }

    @Override
    @Timed(value = "parcelleService.recuperationParcelleParId", description = "Time taken to get a parcelle by id.")
    public Parcelle recuperationParcelleParId(String id) {
        return parcelleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Parcelle> recuperationParcellesNonLieesAdresse() {
        return parcelleRepository.findByAdresseIsNull();
    }

    @Override
    public void enregistrementParcelle(Parcelle parcelle) {
        log.info("Enregistrement de la parcelle " + parcelle.getId());
        parcelleRepository.save(parcelle);
        log.info("Fin enregistrement de la parcelle " + parcelle.getId());
    }

    @Override
    public Long nombreParcelleLieesAdresse() {
        return parcelleRepository.countByAdresseIsNotNull();
    }
}
