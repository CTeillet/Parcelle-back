package com.teillet.parcelle.initialisation;


import com.teillet.parcelle.dto.CommuneDto;
import com.teillet.parcelle.mapper.CommuneMapper;
import com.teillet.parcelle.model.Commune;
import com.teillet.parcelle.service.ICommuneService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
@Order(1)
@AllArgsConstructor
@Transactional
public class InitialisationCommune implements CommandLineRunner {

	public static final Logger LOGGER = Logger.getLogger(String.valueOf(InitialisationCommune.class));

	private final ICommuneService communeService;

	@Override
	public void run(String... args) throws Exception {
		if (communeService.nombreCommune() > 0) {
			return;
		}

		LOGGER.info("Début import communes");
		//Read Json from file ressources/json/code-postal-code-insee-2015@public.json
		ObjectMapper mapper = new ObjectMapper();
		List<CommuneDto> communeDtos = mapper.readValue(new ClassPathResource("json/code-postal-code-insee-2015.json").getFile(),
				new TypeReference<List<CommuneDto>>(){});
		List<Commune> communes = CommuneMapper.MAPPER.toEntity(communeDtos);
		List<Commune> communesSauvegardes = communeService.enregistrementLotCommune(communes);
		LOGGER.info("Nombre de communes sauvegardées : " + communesSauvegardes.size());

	}
}
