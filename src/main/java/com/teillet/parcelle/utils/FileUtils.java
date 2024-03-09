package com.teillet.parcelle.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teillet.parcelle.service.ISupabaseBucketService;
import com.teillet.parcelle.service.ITemporaryFileService;
import io.supabase.data.file.FileDownload;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;

@Slf4j
public class FileUtils {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     *  Create a method that opens gz file and return a BufferedReader
     *  @param file the file to open
     *  @return a BufferedReader to read the file
      */
    @NotNull
    public static BufferedReader openGzFile(File file) throws IOException {
        try {
            InputStream gzipStream = getInputStreamGzFile(file);
            InputStreamReader in = new InputStreamReader(gzipStream, StandardCharsets.UTF_8);
            return new BufferedReader(in);
        } catch (IOException e) {
            throw new IOException("Erreur lors de l'ouverture du fichier " + file, e);
        }
    }

    /**
     * Create a method that opens gz file and return an InputStream
     * @param file the file to open
     * @return a InputStream to read the file
     */
    @NotNull
    public static InputStream getInputStreamGzFile(File file) throws IOException {
        InputStream fileStream = new FileInputStream(file);
	    return new GZIPInputStream(fileStream);
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

    public static String downloadFile(String fichier, ISupabaseBucketService supabaseBucketService, ITemporaryFileService temporaryFileService) throws InterruptedException, ExecutionException, IOException {
        log.info("Téléchargement du fichier json");
        FileDownload downloadFile = supabaseBucketService.downloadFile(fichier);
        String pathDownloadedFile = temporaryFileService.saveTemporaryFile(downloadFile.getBytes(), fichier);
        log.info("Fin téléchargement du fichier json");
        return pathDownloadedFile;
    }
}
