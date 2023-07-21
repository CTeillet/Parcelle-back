package com.teillet.parcelle.initialisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teillet.parcelle.dto.AdresseDto;
import com.teillet.parcelle.mapper.AdresseMapper;
import com.teillet.parcelle.repository.CommuneRepository;
import com.teillet.parcelle.service.IAdresseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Objects;


@Order(3)
@Component
@AllArgsConstructor
@Slf4j
public class InitialisationAdresse implements CommandLineRunner {
    private final IAdresseService adresseService;
    private final CommuneRepository communeRepository;

    private static AdresseDto getAdresseDto(ObjectMapper mapper, String line) {
        try {
            return mapper.readValue(line, AdresseDto.class);
        } catch (IOException e) {
            log.error("Erreur lors de la lecture d'une ligne");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run(String... args) throws IOException {
        if (adresseService.nombreAdresse() > 0) {
            return;
        }

        log.info("Début import adresses");
        String path = "json/adresses-cadastre-91.ndjson";
        importAdresse(path);
        log.info("Fin import adresses");
    }

    void importAdresse(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        File file = new ClassPathResource(path).getFile();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            br.lines()
                    .parallel()
                    .map(line -> getAdresseDto(mapper, line))
                    .filter(Objects::nonNull)
                    .map(adresseDto -> AdresseMapper.MAPPER.toEntity(adresseDto, communeRepository))
                    .forEach(adresseService::enregistrementAdresse);
        } catch (Exception e) {
            log.error("Erreur lors de l'import des adresses");
            e.printStackTrace();
        }
    }
}
