package com.stationcontrol.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init();

	String store(MultipartFile file);

	void delete(String ... filename);

}
