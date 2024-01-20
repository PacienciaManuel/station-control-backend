package com.stationcontrol.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stationcontrol.exception.IntegrityException;
import com.stationcontrol.model.Pais;
import com.stationcontrol.model.Suspeito;
import com.stationcontrol.repository.SuspeitoRepository;
import com.stationcontrol.service.AbstractService;
import com.stationcontrol.service.SuspeitoService;
import com.stationcontrol.storage.StorageService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class SuspeitoServiceImpl extends AbstractServiceImpl<Suspeito, UUID, SuspeitoRepository> implements SuspeitoService {
	private final StorageService storageService;
	private final AbstractService<Pais, UUID> paisService;


	public SuspeitoServiceImpl(SuspeitoRepository repository, HttpServletRequest request, MessageSource messageSource,
			StorageService storageService, AbstractService<Pais, UUID> paisService) {
		super(repository, request, messageSource);
		this.storageService = storageService;
		this.paisService = paisService;
	}
	
	@Override
	public Suspeito create(UUID idPais, Suspeito suspeito, Optional<MultipartFile> foto) {
		suspeito.setPais(paisService.findById(idPais));
		if (foto.isPresent()) {
			suspeito.setFoto(storageService.store(foto.get()).getUrl());
		}
		try {			
			return super.save(suspeito);
		} catch (DataIntegrityViolationException e) {
			storageService.delete(suspeito.getFoto());
			throw new IntegrityException(messageSource.getMessage("suspect.identity-card.already-exists", new String[]{suspeito.getBilheteIdentidade()}, request.getLocale()));
		}
	}
	
	@Override
	public List<Suspeito> create(List<Suspeito> suspeitos) {
		return super.save(suspeitos);
	}

	@Override
	public Suspeito update(UUID idSuspeito, UUID idPais, Suspeito suspeito) {
		Suspeito entity = super.findById(idSuspeito);
		entity.setNome(suspeito.getNome());
		entity.setDetido(suspeito.getDetido());
		entity.setGenero(suspeito.getGenero());
		entity.setPais(paisService.findById(idPais));
		entity.setBiografia(suspeito.getBiografia());
		entity.setDataNascimento(suspeito.getDataNascimento());
		entity.setBilheteIdentidade(suspeito.getBilheteIdentidade());
		try {			
			return super.save(entity);
		} catch (DataIntegrityViolationException e) {
			throw new IntegrityException(messageSource.getMessage("suspect.identity-card.already-exists", new String[]{suspeito.getBilheteIdentidade()}, request.getLocale()));
		}
	}

	@Override
	public Suspeito updatePhoto(UUID idSuspeito, MultipartFile foto) {
		Suspeito entity = super.findById(idSuspeito);
		var arquivo = storageService.store(foto);
		storageService.delete(entity.getFoto());
		entity.setFoto(arquivo.getUrl());
		return super.save(entity);
	}
	
	@Override
	public Suspeito delete(UUID id) {
		Suspeito entity = super.delete(id);
		storageService.delete(entity.getFoto());
		return entity;	
	}

}
