package com.teillet.parcelle.initialisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teillet.parcelle.dto.AdresseDto;
import com.teillet.parcelle.mapper.AdresseMapper;
import com.teillet.parcelle.repository.TownRepository;
import com.teillet.parcelle.service.IAddressService;
import com.teillet.parcelle.service.ISupabaseBucketService;
import com.teillet.parcelle.service.ITemporaryFileService;
import com.teillet.parcelle.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


@Order(3)
@Component
@Slf4j
@RequiredArgsConstructor
public class AddressInitialization implements CommandLineRunner {
    private final IAddressService addressService;
    private final TownRepository townRepository;
    private final ITemporaryFileService temporaryFileService;
    private final ISupabaseBucketService supabaseBucketService;

    @Value("${file.address}")
    private String fichierAdresse;
    @Value("${zip-code.insee-code}")
    private String codePostalCodeInsee;
    @Value("${file.delimiter}")
    private String delimiter;

    @Override
    public void run(String... args) throws IOException, ExecutionException, InterruptedException {
        if (addressService.addressNumber() > 0) {
            log.info("Il y a déjà des adresses enregistrées. L'import n'est pas nécessaire.");
            return;
        }

        List<String> codePostauxValides = List.of(codePostalCodeInsee.split(delimiter));
        log.info("Début import adresses");
        importAddress(fichierAdresse, codePostauxValides);
        log.info("Fin import adresses");
    }

    private void importAddress(String filePath, List<String> codePostauxValides) throws IOException, ExecutionException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String pathDownloadedFile = FileUtils.downloadFile(filePath, supabaseBucketService, temporaryFileService);

        File file = new File(pathDownloadedFile);
        try (BufferedReader br = FileUtils.openGzFile(file)) {
            br.lines()
                    .parallel()
                    .map(line -> getAddressDto(mapper, line))
                    .filter(Objects::nonNull)
                    .filter(adresseDto -> codePostauxValides.contains(adresseDto.getCodeCommune()))
                    .map(adresseDto -> AdresseMapper.MAPPER.toEntity(adresseDto, townRepository))
                    .forEach(addressService::saveAddress);
        } catch (Exception e) {
            log.error("Erreur lors de l'import des adresses", e);
        }
    }

    private AdresseDto getAddressDto(ObjectMapper mapper, String line) {
        try {
            return mapper.readValue(line, AdresseDto.class);
        } catch (IOException e) {
            log.error("Erreur lors de la lecture d'une ligne", e);
            return null;
        }
    }
}
