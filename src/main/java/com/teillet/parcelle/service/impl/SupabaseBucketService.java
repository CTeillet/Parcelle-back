package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.service.ISupabaseBucketService;
import io.supabase.IStorageClient;
import io.supabase.StorageClient;
import io.supabase.api.IStorageFileAPI;
import io.supabase.data.file.FileDownload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class SupabaseBucketService implements ISupabaseBucketService {
	@Value("${supabase.storage.url}")
	private String supabaseStorageUrl;
	@Value("${supabase.service.token}")
	private String supabaseServiceToken;
	@Value("${supabase.bucket.name}")
	private String supabaseBucketName;

	@Override
	public FileDownload downloadFile(String fileName) throws InterruptedException, ExecutionException {
		log.info("Téléchargement du fichier " + fileName);
		return getStorageFileAPI().download(fileName, null).get();
	}

	private IStorageClient getStorageClient() {
		return new StorageClient(supabaseServiceToken, supabaseStorageUrl);
	}

	private IStorageFileAPI getStorageFileAPI() {
		return getStorageClient().from(supabaseBucketName);
	}


}
