package com.stationcontrol.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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
	public List<Arquivo> findByOcorrencia(Ocorrencia ocorrencia) {
		return super.repository.findByOcorrencia(Ocorrencia.builder().id(null).build());
	}

	@Override
	public Page<Arquivo> findByOcorrencia(int page, int size, Ocorrencia ocorrencia, String orderBy,
			Direction direction) {
		return super.repository.findByOcorrencia(Ocorrencia.builder().id(null).build(), PageRequest.of(page, size, direction, orderBy));
	}

	@Override
	public Arquivo create(UUID idOcorrencia, MultipartFile file) {
		Ocorrencia ocorrencia = ocorrenciaService.findById(idOcorrencia);
		Arquivo arquivo = storageService.store(file);
		arquivo.setOcorrencia(ocorrencia);
		return super.save(arquivo);
	}

	@Override
	public List<Arquivo> create(UUID idOcorrencia, MultipartFile[] arquivos) {
		Ocorrencia ocorrencia = ocorrenciaService.findById(idOcorrencia);
		return super.save(storageService.store(arquivos).stream().map(arquivo -> {
			arquivo.setOcorrencia(ocorrencia);
			return arquivo;
		}).toList());
	}

	@Override
	public Arquivo deleteById(UUID idArquivo) {
		Arquivo arquivo = super.deleteById(idArquivo);
		storageService.delete(arquivo.getUrl());
		return arquivo;
	}
	
}
