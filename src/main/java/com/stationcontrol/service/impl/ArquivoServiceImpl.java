package com.stationcontrol.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stationcontrol.model.Arquivo;
import com.stationcontrol.model.Ocorrencia;
import com.stationcontrol.repository.ArquivoRepository;
import com.stationcontrol.service.ArquivoService;
import com.stationcontrol.service.OcorrenciaService;
import com.stationcontrol.storage.StorageService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ArquivoServiceImpl extends AbstractServiceImpl<Arquivo, UUID, ArquivoRepository> implements ArquivoService {
	private final StorageService storageService;
	private final OcorrenciaService ocorrenciaService;

	public ArquivoServiceImpl(ArquivoRepository repository, HttpServletRequest request, MessageSource messageSource,
			StorageService storageService, OcorrenciaService ocorrenciaService) {
		super(repository, request, messageSource);
		this.storageService = storageService;
		this.ocorrenciaService = ocorrenciaService;
	}

	@Override
	public List<Arquivo> create(UUID idOcorrencia, MultipartFile[] multipartFiles) {
		Ocorrencia ocorrencia = ocorrenciaService.findById(idOcorrencia);
		return super.save(storageService.store(multipartFiles).stream().map(arquivo -> {
			arquivo.setOcorrencia(ocorrencia);
			return arquivo;
		}).toList());
	}
	
	@Override
	public Arquivo delete(UUID id) {
		Arquivo entity = super.findById(id);
		storageService.delete(entity.getUrl());
		return entity;
	}
}
