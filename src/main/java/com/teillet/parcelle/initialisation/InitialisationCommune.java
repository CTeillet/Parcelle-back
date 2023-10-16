package com.teillet.parcelle.initialisation;


import com.teillet.parcelle.dto.CommuneDto;
import com.teillet.parcelle.mapper.CommuneMapper;
import com.teillet.parcelle.model.Commune;
import com.teillet.parcelle.service.ICommuneService;
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
public class InitialisationCommune implements CommandLineRunner {
	private final ICommuneService communeService;
	private final ITemporaryFileService temporaryFileService;
	private final ISupabaseBucketService supabaseBucketService;

	@Value("${fichier.code-postal}")
	private String fichierCodePostal;
	@Value("${departement.nom}")
	private String departement;

	@Override
	public void run(String... args) throws Exception {
		if (communeService.nombreCommune() > 0) {
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
		List<Commune> communes = CommuneMapper.MAPPER.toEntity(communeDtosFiltre);
		log.info("Nombre de communes : " + communes.size());
		//Enregistrement des communes
		List<Commune> communesSauvegardes = communeService.enregistrementLotCommune(communes);
		log.info("Nombre de communes sauvegardées : " + communesSauvegardes.size());
	}

}
