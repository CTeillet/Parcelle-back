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
        if (adresseService.nombreAdresse() == 0 || parcelleService.nombreParcelleLieesAdresse() > 0) {
            log.info("Il n'y a pas d'adresses ou il y a déjà des parcelles liées à une adresse. La liaison n'est pas nécessaire.");
            return;
        }
        log.info("Début liaison parcelle adresse");
        parcelleService.recuperationParcellesNonLieesAdresse().forEach(this::miseAJourAdresseParcelle);
        log.info("Fin liaison parcelle adresse");
    }

    private void miseAJourAdresseParcelle(Parcelle parcelle) {
        log.info("Mise à jour de l'adresse de la parcelle {}", parcelle.getId());
        Adresse adresse = adresseService.recuperationAdresseCorrespondantParcelle(parcelle.getId());
        if (adresse != null) {
            log.info("Adresse {} correspondant à la parcelle {}", adresse.getId(), parcelle.getId());
            parcelle.setAdresse(adresse);
            parcelleService.enregistrementParcelle(parcelle);
        } else {
            log.error("Aucune adresse correspondant à la parcelle {}", parcelle.getId());
        }
    }
}
