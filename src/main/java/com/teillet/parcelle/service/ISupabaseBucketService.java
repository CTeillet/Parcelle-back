package com.teillet.parcelle.service;

import io.supabase.data.file.FileDownload;

public interface ISupabaseBucketService {
	FileDownload downloadFile(String fileName) throws Exception;

}
