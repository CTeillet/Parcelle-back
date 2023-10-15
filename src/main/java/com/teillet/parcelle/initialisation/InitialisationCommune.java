package com.teillet.parcelle.initialisation;


import com.teillet.parcelle.dto.CommuneDto;
import com.teillet.parcelle.mapper.CommuneMapper;
import com.teillet.parcelle.model.Commune;
import com.teillet.parcelle.service.ICommuneService;
import com.teillet.parcelle.service.ISupabaseBucketService;
import com.teillet.parcelle.service.ITemporaryFileService;
import com.teillet.parcelle.utils.FileUtils;
import io.supabase.data.file.FileDownload;
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


	@Value("${departement.nom}")
	private String departement;
	@Value("${fichier.code-postal}")
	private String fichierCodePostal;

	@Override
	public void run(String... args) throws Exception {
		if (communeService.nombreCommune() > 0) {
			return;
		}
		log.info(departement);

		log.info("Début import communes");
		//Dowlnoad file from supabase bucket
		String pathDownloadedFile = downloadFile();
		List<CommuneDto> communeDtos = FileUtils.readFile(pathDownloadedFile, CommuneDto.class);
		//Read Json from file ressources/json/code-postal-code-insee-2015.json
		//Filter communeDtos to keep only communes from Essonne
		List<CommuneDto> communeDtosFiltre = communeDtos.parallelStream().filter(communeDto -> communeDto.getNomDept().equals(departement)).toList();
		List<Commune> communes = CommuneMapper.MAPPER.toEntity(communeDtosFiltre);
		log.info("Nombre de communes : " + communes.size());
		List<Commune> communesSauvegardes = communeService.enregistrementLotCommune(communes);
		log.info("Nombre de communes sauvegardées : " + communesSauvegardes.size());
	}

	private String downloadFile() throws Exception {
		log.info("Téléchargement du fichier json");
		FileDownload downloadFile = supabaseBucketService.downloadFile(fichierCodePostal);
		String pathDownloadedFile = temporaryFileService.saveTemporaryFile(downloadFile.getBytes(), fichierCodePostal);
		log.info("Fin téléchargement du fichier json");
		return pathDownloadedFile;
	}
}
