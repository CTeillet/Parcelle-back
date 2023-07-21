package com.teillet.parcelle.controller;

import com.teillet.parcelle.dto.ParcelleClusterDto;
import com.teillet.parcelle.service.impl.PateService;
import com.teillet.parcelle.utils.ParcelleClusterUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class PateController {
    private final PateService pateService;

    @GetMapping("/generatePateTemporaires")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<String> generatePate() throws IOException {
        log.info("Génération des pates temporaires");
        List<ParcelleClusterDto> pate = pateService.generationPateTemporaires();
        log.info("Génération des pates temporaires terminée");
        return ResponseEntity.ok(ParcelleClusterUtils.parcellesClusterToGeoJson(pate));
    }

}
