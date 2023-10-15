package com.teillet.parcelle.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Slf4j
public class FileUtils {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    // Create a method that open gz fil
    public static BufferedReader openGzFile(File file) throws IOException {
        try {
            InputStream fileStream = new FileInputStream(file);
            InputStream gzipStream = new GZIPInputStream(fileStream);
            return new BufferedReader(new InputStreamReader(gzipStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IOException("Erreur lors de l'ouverture du fichier " + file, e);
        }
    }

    //Lis tout le fichier et renvoie son contenu dans un String depuis un BufferedReader
    public static String readAll(BufferedReader br) {
        return br.lines().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    public static <T> List<T> readFile(String pathDownloadedFile, Class<T> elementType) throws IOException {
        log.info("Lecture du fichier json {}", pathDownloadedFile);
        try (BufferedReader reader = openGzFile(new File(pathDownloadedFile))) {
            String contenue = readAll(reader);
            log.info("Transformation du fichier {} en {}", pathDownloadedFile, elementType.getSimpleName());
            List<T> results = MAPPER.readValue(contenue, MAPPER.getTypeFactory().constructCollectionType(List.class, elementType));
            log.info("Fin lecture du fichier json. Nb éléments : {}", results.size());
            return results;
        }
    }
}
