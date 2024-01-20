package com.stationcontrol.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stationcontrol.model.Arquivo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;
	
	protected final HttpServletRequest request;

	protected final MessageSource messageSource;
	

	public FileSystemStorageService(@Value("${application.storage.location}") Path storageLocation, 
			HttpServletRequest request, MessageSource messageSource) {
		super();
		this.rootLocation = storageLocation;
		this.request = request;
		this.messageSource = messageSource;
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
	public Arquivo store(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (file.isEmpty()) throw new StorageException(messageSource.getMessage("storage.failed.empty-file", new String[] {originalFilename}, request.getLocale()));
		if (originalFilename == null) throw new StorageException(messageSource.getMessage("storage.failed.unnamed-file", new String[] {originalFilename}, request.getLocale()));
		if (!originalFilename.contains(".")) throw new StorageException(messageSource.getMessage("storage.failed.without-extension", new String[] {originalFilename}, request.getLocale()));
		try {
			String url = new StringBuilder(UUID.randomUUID().toString())
			.append("-")
			.append(System.currentTimeMillis())
			.append(originalFilename.substring(originalFilename.lastIndexOf(".")))
			.toString();
			file.transferTo(rootLocation.resolve(url));
			return Arquivo.builder()
			.url(url)
			.nome(originalFilename)
			.tipo(file.getContentType())
			.build();
		} catch (IOException e) {
			throw new StorageException(messageSource.getMessage("storage.failed", new String[] {originalFilename}, request.getLocale()), e);
		}
	}
	
	@Override
	public List<Arquivo> store(MultipartFile[] files) {
		List<Arquivo> arquivos = new ArrayList<>(files.length);
		for (MultipartFile multipartFile : files) {
			arquivos.add(store(multipartFile));
		}
		return arquivos;
	}

	@Override
	public void delete(String ... files) {
		for (String filename : files) {
			try {
				if (Objects.nonNull(filename)) {
					Files.deleteIfExists(rootLocation.resolve(filename));
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
