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
    private final ICommuneService communeService;

    @Override
    public void run(String... args) throws Exception {
        if (communeService.nombreCommune() > 0) {
            return;
        }

        log.info("Début import communes");
        //Read Json from file ressources/json/code-postal-code-insee-2015@public.json
        ObjectMapper mapper = new ObjectMapper();
        List<CommuneDto> communeDtos = mapper.readValue(new ClassPathResource("json/code-postal-code-insee-2015.json").getFile(),
                new TypeReference<>() {
                });
        List<Commune> communes = CommuneMapper.MAPPER.toEntity(communeDtos);
        List<Commune> communesSauvegardes = communeService.enregistrementLotCommune(communes);
        log.info("Nombre de communes sauvegardées : " + communesSauvegardes.size());
    }
}
