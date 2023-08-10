package com.teillet.parcelle.initialisation;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teillet.parcelle.dto.CommuneDto;
import com.teillet.parcelle.mapper.CommuneMapper;
import com.teillet.parcelle.model.Commune;
import com.teillet.parcelle.service.ICommuneService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
@AllArgsConstructor
@Transactional
@Slf4j
public class InitialisationCommune implements CommandLineRunner {
    public static final String DEPARTEMENT = "ESSONNE";
    private final ICommuneService communeService;

    @Override
    public void run(String... args) throws Exception {
        if (communeService.nombreCommune() > 0) {
            return;
        }

        log.info("Début import communes");
        //Read Json from file ressources/json/code-postal-code-insee-2015.json
        ObjectMapper mapper = new ObjectMapper();
        log.info("Lecture du fichier json");
        List<CommuneDto> communeDtos = mapper.readValue(
                new ClassPathResource("json/code-postal-code-insee-2015.json").getFile(), new TypeReference<>() {
                }
        );
        //Filter communeDtos to keep only communes from Essonne
        communeDtos = communeDtos.parallelStream().filter(communeDto -> communeDto.getNomDept().equals(DEPARTEMENT)).toList();
        log.info("Fin lecture du fichier json");
        List<Commune> communes = CommuneMapper.MAPPER.toEntity(communeDtos);
        log.info("Nombre de communes : " + communes.size());
        List<Commune> communesSauvegardes = communeService.enregistrementLotCommune(communes);
        log.info("Nombre de communes sauvegardées : " + communesSauvegardes.size());
    }
}
