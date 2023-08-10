package com.teillet.parcelle.initialisation;

import com.teillet.parcelle.dto.ParcelleDto;
import com.teillet.parcelle.mapper.ParcelleMapper;
import com.teillet.parcelle.model.Parcelle;
import com.teillet.parcelle.repository.CommuneRepository;
import com.teillet.parcelle.service.IParcelleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Component
@Order(2)
@AllArgsConstructor
@Slf4j
public class InitialisationParcelle implements CommandLineRunner {
    public static final int TAILLE_TAMPON = 1000;

    private final IParcelleService parcelleService;

    private final CommuneRepository communeRepository;

    @Override
    public void run(String... args) {
        if (parcelleService.nombreParcelle() > 0) {
            return;
        }

        log.info("Début import parcelles");
        String path = "json/cadastre-91027-parcelles.json";
        String path1 = "json/cadastre-91201-parcelles.json";
        String path2 = "json/cadastre-91326-parcelles.json";
        String path3 = "json/cadastre-91432-parcelles.json";
        String path4 = "json/cadastre-91479-parcelles.json";
        String path5 = "json/cadastre-91589-parcelles.json";
        List<String> fichiers = Arrays.asList(path, path1, path2, path3, path4, path5);
        fichiers.forEach(this::lectureEtSauvegardeParcelles);
    }

    private void lectureEtSauvegardeParcelles(String path) {
        log.info("Lecture du fichier {}", path);
        try (GeoJSONReader geoJSONReader = new GeoJSONReader(new ClassPathResource(path).getFile().toURI().toURL())) {
            SimpleFeatureCollection features = geoJSONReader.getFeatures();
            log.info("Nombre de parcelles a importées " + features.size());
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
                        log.info("Tampon atteint, sauvegarde des parcelles");
                        i = sauvegardeParcelleEtIncrementeI(i, parcellesTampon);
                    }
                }
                log.info("Fin de la lecture des parcelles");
                i = sauvegardeParcelleEtIncrementeI(i, parcellesTampon);
                log.info("Fin de la sauvegarde des parcelles");
            }
            log.info("Nombre de parcelles sauvegardées : " + i);
        } catch (IOException ioException) {
            log.error("Problème lors de la lecture du fichier {}: {}", path, Arrays.toString(ioException.getStackTrace()));
        }
    }

    private int sauvegardeParcelleEtIncrementeI(int i, List<Parcelle> parcellesTampon) {
        log.info("Nombre de parcelles à sauvegarder : " + parcellesTampon.size());
        List<Parcelle> parcelleSauvegarde = parcelleService.enregistrementLotParcelle(parcellesTampon);
        int nombreParcellesSauvegardees = parcelleSauvegarde.size();
        i += nombreParcellesSauvegardees;
        if (nombreParcellesSauvegardees == parcellesTampon.size()) {
            log.info("Nombre de parcelles sauvegardées : " + i);
        } else {
            log.error("Problème lors de la sauvegarde des parcelles : {} parcelles à sauvegarder et {} parcelles sauvegardées", parcellesTampon.size(), nombreParcellesSauvegardees);
        }
        parcellesTampon.clear();
        return i;
    }

}

