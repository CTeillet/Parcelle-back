package com.teillet.parcelle.initialisation;

import com.teillet.parcelle.repository.CommuneRepository;
import com.teillet.parcelle.dto.ParcelleDto;
import com.teillet.parcelle.mapper.ParcelleMapper;
import com.teillet.parcelle.model.Parcelle;
import com.teillet.parcelle.service.IParcelleService;
import lombok.AllArgsConstructor;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Component
@Order(2)
@AllArgsConstructor
public class InitialisationParcelle implements CommandLineRunner {
	public static final Logger LOGGER = Logger.getLogger(String.valueOf(InitialisationParcelle.class));
	public static final int TAILLE_TAMPON = 5000;

	private final IParcelleService parcelleService;

	private final CommuneRepository communeRepository;

	@Override
	public void run(String... args) {
		if (parcelleService.nombreParcelle() > 0) {
			return;
		}

		LOGGER.info("Début import parcelles");
		String path  = "json/cadastre-91027-parcelles.json";
		String path1 = "json/cadastre-91201-parcelles.json";
		String path2 = "json/cadastre-91326-parcelles.json";
		String path3 = "json/cadastre-91432-parcelles.json";
		String path4 = "json/cadastre-91479-parcelles.json";
		String path5 = "json/cadastre-91589-parcelles.json";
		List<String> fichiers = Arrays.asList(path, path1, path2, path3, path4, path5);
		fichiers.forEach(this::lectureEtSauvegardeParcelles);
	}

	private void lectureEtSauvegardeParcelles(String path) {
		try(GeoJSONReader geoJSONReader = new GeoJSONReader(new ClassPathResource(path).getFile().toURI().toURL())) {
			SimpleFeatureCollection features = geoJSONReader.getFeatures();
			LOGGER.info("Nombre de parcelles a importées " + features.size());
			int i = 0;
			List<Parcelle> parcellesTampon = new ArrayList<>(TAILLE_TAMPON);
			try (SimpleFeatureIterator iterator = features.features()) {
				while (iterator.hasNext()) {
					SimpleFeature feature = iterator.next();
					ParcelleDto parcelleDto =
							ParcelleDto.builder()
									.id((String) feature.getAttribute("id"))
									.commune((String) feature.getAttribute("commune"))
									.prefixe((String) feature.getAttribute("prefixe"))
									.section((String) feature.getAttribute("section"))
									.numero((String) feature.getAttribute("numero"))
									.contenance((Integer) feature.getAttribute("contenance"))
									.arpente((Boolean) feature.getAttribute("arpente"))
									.created((Date) feature.getAttribute("created"))
									.updated((Date) feature.getAttribute("updated"))
									.geom((Polygon) feature.getAttribute("geometry"))
									.build();
					Parcelle parcelle = ParcelleMapper.MAPPER.toEntity(parcelleDto, communeRepository);
					parcellesTampon.add(parcelle);
					if (parcellesTampon.size() == TAILLE_TAMPON) {
						i = sauvegardeParcelleEtIncrementeI(i, parcellesTampon);
					}
				}
				i = sauvegardeParcelleEtIncrementeI(i, parcellesTampon);
			}
			LOGGER.info("Nombre de parcelles sauvegardées : " + i);
		} catch (IOException ioException) {
			LOGGER.severe("Problème lors de la lecture du fichier " + path + ": " + Arrays.toString(ioException.getStackTrace()));
		}
	}

	private int sauvegardeParcelleEtIncrementeI(int i, List<Parcelle> parcellesTampon) {
		List<Parcelle> parcelleSauvegarde = parcelleService.enregistrementLotParcelle(parcellesTampon);
		i += parcelleSauvegarde.size();
		if (parcelleSauvegarde.size() == parcellesTampon.size()) {
			LOGGER.info("Nombre de parcelles sauvegardées : " + i);
		} else {
			LOGGER.severe("Problème lors de la sauvegarde des parcelles : " + parcellesTampon.size() + " parcelles à sauvegarder et " + parcelleSauvegarde.size() + " parcelles sauvegardées");
		}
		parcellesTampon.clear();
		return i;
	}

}

