package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.dto.ParcelleClusterDto;
import com.teillet.parcelle.repository.ParcelleRepository;
import com.teillet.parcelle.service.IPateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class PateService implements IPateService {
	static final Logger LOGGER = Logger.getLogger(PateService.class.getName());
	private ParcelleRepository parcelleRepository;

	@Override
	public List<ParcelleClusterDto> generationPateTemporaires() {
		LOGGER.info("Génération des pates temporaires");
		List<ParcelleClusterDto> parcelleClusterDtos = parcelleRepository.getParcelleClusters();
		LOGGER.info("Génération des pates temporaires terminée");
		return parcelleClusterDtos;
	}
}
