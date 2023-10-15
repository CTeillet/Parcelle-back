package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.service.ITemporaryFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class TemporaryFileService implements ITemporaryFileService {
	@Override
	public String saveTemporaryFile(byte[] fileBytes, String fileName) throws IOException {
		log.info("Sauvegarde du fichier temporaire " + fileName);

		// Créer un répertoire temporaire
		Path tempDirPath = Files.createTempDirectory("prefix_");

		// Créer un chemin pour le fichier temporaire dans le répertoire temporaire
		Path tempFilePath = tempDirPath.resolve(fileName);

		// Récupérez le répertoire parent du fichier
		Path parentDir = tempFilePath.getParent();

		// Vérifiez si le répertoire parent existe, sinon, créez-le
		if (!Files.exists(parentDir)) {
			Files.createDirectories(parentDir);
		}

		// Écrire le contenu du fichier dans le fichier temporaire
		Files.write(tempFilePath, fileBytes);

		// Le répertoire temporaire sera automatiquement supprimé lorsque le programme se termine

		return tempFilePath.toString();
	}
}
