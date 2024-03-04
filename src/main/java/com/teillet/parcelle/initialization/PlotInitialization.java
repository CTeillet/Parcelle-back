package com.teillet.parcelle.initialization;

import com.teillet.parcelle.mapper.PlotMapper;
import com.teillet.parcelle.repository.CityRepository;
import com.teillet.parcelle.service.IPlotService;
import com.teillet.parcelle.service.ISupabaseBucketService;
import com.teillet.parcelle.service.ITemporaryFileService;
import com.teillet.parcelle.utils.FileUtils;
import com.teillet.parcelle.utils.GeoJsonUtils;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.geojson.GeoJSONReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class PlotInitialization implements CommandLineRunner {

    private final IPlotService plotService;
    private final CityRepository cityRepository;
    private final ITemporaryFileService temporaryFileService;
    private final ISupabaseBucketService supabaseBucketService;

    @Value("${file.plots}")
    private String plotFiles;
    @Value("${file.delimiter}")
    private String delimiter;

    @Override
    @Observed(name = "initialization.plot")
    public void run(String... args) {
        if (plotService.plotNumber() > 0) {
            log.info("Il y a déjà des parcelles enregistrées. L'import n'est pas nécessaire.");
            return;
        }

        log.info("Début de l'import des parcelles");
        List<String> files = List.of(plotFiles.split(delimiter));
        files.forEach(this::lectureEtSauvegardeParcelles);
        log.info("Fin de l'import des parcelles");
    }

    private void lectureEtSauvegardeParcelles(String path) {
        log.info("Lecture du fichier {}", path);
        try {
            String pathDownloadedFile = FileUtils.downloadFile(path, supabaseBucketService, temporaryFileService);

            try (GeoJSONReader geoJSONReader = new GeoJSONReader(FileUtils.getInputStreamGzFile(new File(pathDownloadedFile)))) {
                try (Stream<SimpleFeature> featureStream = GeoJsonUtils.toStream(geoJSONReader.getIterator())) {
                    featureStream
                            .parallel()
                            .map(GeoJsonUtils::transformSimpleFeatureToParcelleDto)
                            .map(parcelleDto -> PlotMapper.MAPPER.toEntity(parcelleDto, cityRepository))
                            .forEach(plotService::savePlot);
                }
            }

        } catch (IOException | ExecutionException | InterruptedException e) {
            log.error("Problème lors de la lecture du fichier {}: {}", path, e.getMessage());
        }
    }

}

