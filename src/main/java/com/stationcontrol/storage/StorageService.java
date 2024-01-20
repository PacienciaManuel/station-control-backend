package com.stationcontrol.storage;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.stationcontrol.model.Arquivo;

public interface StorageService {

	void init();

	Arquivo store(MultipartFile file);

	List<Arquivo> store(MultipartFile[] files);

	void delete(String ... filename);

}
