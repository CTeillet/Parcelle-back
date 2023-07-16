package com.teillet.parcelle.initialisation;

import com.teillet.parcelle.model.Adresse;
import com.teillet.parcelle.model.Parcelle;
import com.teillet.parcelle.service.IAdresseService;
import com.teillet.parcelle.service.IParcelleService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Order(4)
@Component
@AllArgsConstructor
public class LiaisonParcelleAdresse implements CommandLineRunner {
	private IParcelleService parcelleService;
	private IAdresseService adresseService;

	private static final Logger LOGGER = Logger.getLogger(String.valueOf(LiaisonParcelleAdresse.class));

	@Override
	public void run(String... args) {
		if (parcelleService.nombreParcelleLieesAdresse() > 0) {
			return;
		}
		LOGGER.info("Début liaison parcelle adresse");
		parcelleService.recuperationParcellesNonLieesAdresse().parallelStream().forEach(this::miseAJourAdresseParcelle);
		LOGGER.info("Fin liaison parcelle adresse");
	}

	private void miseAJourAdresseParcelle(Parcelle parcelle) {
		Adresse adresse = adresseService.recuperationAdresseCorrespondantParcelle(parcelle.getId());
		if (adresse != null) {
			parcelle.setAdresse(adresse);
			parcelleService.enregistrementParcelle(parcelle);
		} else {
			LOGGER.severe("Aucune adresse correspondant à la parcelle " + parcelle.getId());
		}
	}
}
