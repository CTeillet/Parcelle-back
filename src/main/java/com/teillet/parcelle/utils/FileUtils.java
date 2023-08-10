package com.teillet.parcelle.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class FileUtils {

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
}
