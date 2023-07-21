package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.dto.ParcelleClusterDto;
import com.teillet.parcelle.repository.ParcelleRepository;
import com.teillet.parcelle.service.IPateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PateService implements IPateService {
    private ParcelleRepository parcelleRepository;

    @Override
    public List<ParcelleClusterDto> generationPateTemporaires() {
        log.info("Génération des pates temporaires");
        List<ParcelleClusterDto> parcelleClusterDtos = parcelleRepository.getParcelleClusters();
        log.info("Génération des pates temporaires terminée");
        return parcelleClusterDtos;
    }
}
