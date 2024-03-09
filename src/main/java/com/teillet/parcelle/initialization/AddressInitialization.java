package com.teillet.parcelle.initialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teillet.parcelle.dto.AddressFileDto;
import com.teillet.parcelle.mapper.AddressMapper;
import com.teillet.parcelle.model.Address;
import com.teillet.parcelle.model.City;
import com.teillet.parcelle.repository.CityRepository;
import com.teillet.parcelle.repository.PlotRepository;
import com.teillet.parcelle.service.IAddressService;
import com.teillet.parcelle.service.ISupabaseBucketService;
import com.teillet.parcelle.service.ITemporaryFileService;
import com.teillet.parcelle.utils.FileUtils;
import io.micrometer.observation.annotation.Observed;
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
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Order(3)
@Component
@Slf4j
@RequiredArgsConstructor
public class AddressInitialization implements CommandLineRunner {
    private final PlotRepository plotRepository;
    private final IAddressService addressService;
    private final CityRepository cityRepository;
    private final ITemporaryFileService temporaryFileService;
    private final ISupabaseBucketService supabaseBucketService;

    @Value("${file.address}")
    private String fichierAdresse;
    @Value("${zip-code.insee-code}")
    private String codePostalCodeInsee;
    @Value("${file.delimiter}")
    private String delimiter;

    @Override
    @Observed(name = "initialization.address")
    public void run(String... args) throws IOException, ExecutionException, InterruptedException {
        if (addressService.addressNumber() > 0) {
            log.info("Il y a déjà des adresses enregistrées. L'import n'est pas nécessaire.");
            return;
        }
        log.info("Début import adresses");
        List<String> codePostauxValides = List.of(codePostalCodeInsee.split(delimiter));
        Map<String, City> citiesByInseeCode = cityRepository.findAll().stream().collect(Collectors.toMap(City::getInseeCityCode, city -> city));

        log.info("Téléchargement du fichier d'adresses");
        String pathDownloadedFile = FileUtils.downloadFile(fichierAdresse, supabaseBucketService, temporaryFileService);

        log.info("Lecture des adresses");
        List<Address> addresses = readAddress(codePostauxValides, pathDownloadedFile, citiesByInseeCode);

        log.info("Sauvegarde des adresses");
        addressService.saveAddresses(addresses);

        log.info("Fin import adresses");
    }

    private List<Address> readAddress(List<String> codePostauxValides, String pathDownloadedFile,  Map<String, City> citiesByInseeCode) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(pathDownloadedFile);
        try (BufferedReader br = FileUtils.openGzFile(file)) {
            return br.lines()
                    .parallel()
                    .map(line -> getAddressDto(mapper, line))
                    .filter(Objects::nonNull)
                    .filter(addressFileDto -> codePostauxValides.contains(addressFileDto.getCodeCommune()))
                    .map(addressFileDto -> AddressMapper.MAPPER.toEntity(addressFileDto, citiesByInseeCode, plotRepository))
                    .toList();
        } catch (Exception e) {
            log.error("Erreur lors de l'import des adresses", e);
        }
        return null;
    }

    private AddressFileDto getAddressDto(ObjectMapper mapper, String line) {
        try {
            return mapper.readValue(line, AddressFileDto.class);
        } catch (IOException e) {
            log.error("Erreur lors de la lecture d'une ligne", e);
            return null;
        }
    }
}
