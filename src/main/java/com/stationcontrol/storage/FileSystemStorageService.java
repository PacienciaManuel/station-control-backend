package com.stationcontrol.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;

	public FileSystemStorageService(@Value("${application.storage.location}") String storageLocation) {
		this.rootLocation = Paths.get(storageLocation).toAbsolutePath();
	}

	@Override
	public void init() {
		try {
			if (!Files.exists(rootLocation)) {				
				Files.createDirectory(rootLocation);
			}
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public String store(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (file.isEmpty()) throw new StorageException("Falha ao armazenar arquivo vazio: " + originalFilename);
		if (originalFilename == null) throw new StorageException("Falha ao armazenar arquivo sem nome: " + originalFilename);
		if (!originalFilename.contains(".")) throw new StorageException("Falha ao armazenar arquivo sem formato/extensão: " + originalFilename);
		try {
			String filename = new StringBuilder(UUID.randomUUID().toString())
			.append("-")
			.append(System.currentTimeMillis())
			.append(originalFilename.substring(originalFilename.lastIndexOf(".")))
			.toString();
			file.transferTo(rootLocation.resolve(filename));
			return filename;
		} catch (IOException e) {
			throw new StorageException("Falha ao armazenar o arquivo: " + originalFilename, e);
		}
	}

	@Override
	public void delete(String ... files) {
		for (String filename : files) {
			try {
				Files.deleteIfExists(rootLocation.resolve(filename));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
