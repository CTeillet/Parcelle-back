package com.teillet.parcelle.initialisation;

import com.teillet.parcelle.model.Address;
import com.teillet.parcelle.model.Plot;
import com.teillet.parcelle.service.IAddressService;
import com.teillet.parcelle.service.IPlotService;
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
    private IPlotService parcelleService;
    private IAddressService adresseService;

    @Override
    public void run(String... args) {
        if (adresseService.addressNumber() == 0 || parcelleService.nombreParcelleLieesAdresse() > 0) {
            log.info("Il n'y a pas d'adresses ou il y a déjà des parcelles liées à une adresse. La liaison n'est pas nécessaire.");
            return;
        }
        log.info("Début liaison parcelle adresse");
        parcelleService.recuperationParcellesNonLieesAdresse().forEach(this::miseAJourAdresseParcelle);
        log.info("Fin liaison parcelle adresse");
    }

    private void miseAJourAdresseParcelle(Plot plot) {
        log.info("Mise à jour de l'adresse de la parcelle {}", plot.getId());
        Address adresse = adresseService.getAddressByPlotId(plot.getId());
        if (adresse != null) {
            log.info("Adresse {} correspondant à la parcelle {}", adresse.getId(), plot.getId());
            plot.setAdresse(adresse);
            parcelleService.savePlot(plot);
        } else {
            log.error("Aucune adresse correspondant à la parcelle {}", plot.getId());
        }
    }
}
