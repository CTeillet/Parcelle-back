package com.teillet.parcelle.initialization;


import com.teillet.parcelle.dto.CommuneDto;
import com.teillet.parcelle.mapper.TownMapper;
import com.teillet.parcelle.model.Commune;
import com.teillet.parcelle.service.ITownService;
import com.teillet.parcelle.service.ISupabaseBucketService;
import com.teillet.parcelle.service.ITemporaryFileService;
import com.teillet.parcelle.utils.FileUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TownInitialization implements CommandLineRunner {
	private final ITownService townService;
	private final ITemporaryFileService temporaryFileService;
	private final ISupabaseBucketService supabaseBucketService;

	@Value("${file.zip-code}")
	private String fichierCodePostal;
	@Value("${department.name}")
	private String departement;

	@Override
	public void run(String... args) throws Exception {
		if (townService.townNumber() > 0) {
			log.info("Il y a déjà des communes enregistrées. L'import n'est pas nécessaire.");
			return;
		}

		log.info("Début import communes");
		//Téléchargement du fichier depuis le bucket supabase
		String pathDownloadedFile = FileUtils.downloadFile(fichierCodePostal, supabaseBucketService, temporaryFileService);
		//Lecture du fichier
		List<CommuneDto> communeDtos = FileUtils.readFile(pathDownloadedFile, CommuneDto.class);
		//Filter les communeDtos pour ne garder que celles qui concernent le département choisi
		List<CommuneDto> communeDtosFiltre = communeDtos.parallelStream().filter(communeDto -> communeDto.getNomDept().equals(departement)).toList();
		//Conversion des communeDtos en communes
		List<Commune> towns = TownMapper.MAPPER.toEntity(communeDtosFiltre);
		log.info("Nombre de communes : " + towns.size());
		//Enregistrement des communes
		List<Commune> savedTowns = townService.saveTowns(towns);
		log.info("Nombre de communes sauvegardées : " + savedTowns.size());
	}

}
