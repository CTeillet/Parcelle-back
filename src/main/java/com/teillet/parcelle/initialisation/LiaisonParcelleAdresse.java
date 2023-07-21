package com.teillet.parcelle.initialisation;

import com.teillet.parcelle.model.Adresse;
import com.teillet.parcelle.model.Parcelle;
import com.teillet.parcelle.service.IAdresseService;
import com.teillet.parcelle.service.IParcelleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(4)
@Component
@AllArgsConstructor
@Slf4j
public class LiaisonParcelleAdresse implements CommandLineRunner {
    private IParcelleService parcelleService;
    private IAdresseService adresseService;

    @Override
    public void run(String... args) {
        if (parcelleService.nombreParcelleLieesAdresse() > 0) {
            return;
        }
        log.info("Début liaison parcelle adresse");
        parcelleService.recuperationParcellesNonLieesAdresse().parallelStream().forEach(this::miseAJourAdresseParcelle);
        log.info("Fin liaison parcelle adresse");
    }

    private void miseAJourAdresseParcelle(Parcelle parcelle) {
        Adresse adresse = adresseService.recuperationAdresseCorrespondantParcelle(parcelle.getId());
        if (adresse != null) {
            parcelle.setAdresse(adresse);
            parcelleService.enregistrementParcelle(parcelle);
        } else {
            log.error("Aucune adresse correspondant à la parcelle {}", parcelle.getId());
        }
    }
}
