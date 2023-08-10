package com.teillet.parcelle.initialisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teillet.parcelle.dto.AdresseDto;
import com.teillet.parcelle.mapper.AdresseMapper;
import com.teillet.parcelle.repository.CommuneRepository;
import com.teillet.parcelle.service.IAdresseService;
import com.teillet.parcelle.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


@Order(3)
@Component
@AllArgsConstructor
@Slf4j
public class InitialisationAdresse implements CommandLineRunner {
    private final IAdresseService adresseService;
    private final CommuneRepository communeRepository;

    @Override
    public void run(String... args) throws IOException {
        if (adresseService.nombreAdresse() > 0) {
            return;
        }

        log.info("Début import adresses");
        String path = "json/adresses-cadastre-91.ndjson.gz";


        importAdresse(path);
        log.info("Fin import adresses");
    }

    private void importAdresse(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        File file = new ClassPathResource(path).getFile();
        try (BufferedReader br = FileUtils.openGzFile(file)) {
            br.lines()
                    .parallel()
                    .map(line -> getAdresseDto(mapper, line))
                    .filter(Objects::nonNull)
                    // Only keep adresses with commune code 91027 91201 91326 91432 91479 91589
                    .filter(adresseDto -> adresseDto.getCodeCommune().equals("91027")
                            || adresseDto.getCodeCommune().equals("91201")
                            || adresseDto.getCodeCommune().equals("91326")
                            || adresseDto.getCodeCommune().equals("91432")
                            || adresseDto.getCodeCommune().equals("91479")
                            || adresseDto.getCodeCommune().equals("91589"))
                    .map(adresseDto -> AdresseMapper.MAPPER.toEntity(adresseDto, communeRepository))
                    .forEach(adresseService::enregistrementAdresse);
        } catch (Exception e) {
            log.error("Erreur lors de l'import des adresses", e);
        }
    }

    private AdresseDto getAdresseDto(ObjectMapper mapper, String line) {
        try {
            return mapper.readValue(line, AdresseDto.class);
        } catch (IOException e) {
            log.error("Erreur lors de la lecture d'une ligne", e);
            return null;
        }
    }
}
