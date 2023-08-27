package com.teillet.parcelle.initialisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teillet.parcelle.dto.AdresseDto;
import com.teillet.parcelle.mapper.AdresseMapper;
import com.teillet.parcelle.repository.CommuneRepository;
import com.teillet.parcelle.service.IAdresseService;
import com.teillet.parcelle.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


@Order(3)
@Component
@Slf4j
@RequiredArgsConstructor
public class InitialisationAdresse implements CommandLineRunner {
    private final IAdresseService adresseService;
    private final CommuneRepository communeRepository;

    @Value("${fichier.adresse}")
    private String fichierAdresse;

    @Value("${code-postal.code-insee}")
    private String codePostalCodeInsee;

    @Override
    public void run(String... args) throws IOException {
        if (adresseService.nombreAdresse() > 0) {
            return;
        }

        List<String> codePostauxValides = List.of(codePostalCodeInsee.split(";"));
        log.info("Début import adresses");
        importAdresse(fichierAdresse, codePostauxValides);
        log.info("Fin import adresses");
    }

    private void importAdresse(String cheminFichier, List<String> codePostauxValides) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        File file = new ClassPathResource(cheminFichier).getFile();
        try (BufferedReader br = FileUtils.openGzFile(file)) {
            br.lines()
                    .parallel()
                    .map(line -> getAdresseDto(mapper, line))
                    .filter(Objects::nonNull)
                    .filter(adresseDto -> codePostauxValides.contains(adresseDto.getCodeCommune()))
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
