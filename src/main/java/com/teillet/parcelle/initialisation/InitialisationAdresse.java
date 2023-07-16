package com.teillet.parcelle.initialisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teillet.parcelle.dto.AdresseDto;
import com.teillet.parcelle.mapper.AdresseMapper;
import com.teillet.parcelle.repository.CommuneRepository;
import com.teillet.parcelle.service.IAdresseService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Objects;
import java.util.logging.Logger;


@Order(3)
@Component
@AllArgsConstructor
public class InitialisationAdresse implements CommandLineRunner {
	private final IAdresseService adresseService;
	private final CommuneRepository communeRepository;


	private static final Logger LOGGER = Logger.getLogger(String.valueOf(InitialisationAdresse.class));

	@Override
	public void run(String... args) throws IOException {
		if (adresseService.nombreAdresse() > 0) {
			return;
		}

		LOGGER.info("Début import adresses");
		String path = "json/adresses-cadastre-91.ndjson";
		importAdresse(path);
		LOGGER.info("Fin import adresses");
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
			LOGGER.severe("Erreur lors de l'import des adresses");
			e.printStackTrace();
		}
	}

	private static AdresseDto getAdresseDto(ObjectMapper mapper, String line) {
		try {
			return mapper.readValue(line, AdresseDto.class);
		} catch (IOException e) {
			LOGGER.severe("Erreur lors de la lecture d'une ligne");
			e.printStackTrace();
			return null;
		}
	}
}
