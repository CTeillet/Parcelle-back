package com.teillet.parcelle.service;

import io.supabase.data.file.FileDownload;

import java.util.concurrent.ExecutionException;

public interface ISupabaseBucketService {
	FileDownload downloadFile(String fileName) throws InterruptedException, ExecutionException;

}
