package com.stationcontrol.service.impl;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stationcontrol.exception.IntegrityException;
import com.stationcontrol.model.Arquivo;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.model.Pais;
import com.stationcontrol.model.Requerente;
import com.stationcontrol.repository.RequerenteRepository;
import com.stationcontrol.service.AbstractService;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.service.RequerenteService;
import com.stationcontrol.storage.StorageService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class RequerenteServiceImpl extends AbstractServiceImpl<Requerente, UUID, RequerenteRepository> implements RequerenteService {
	private final StorageService storageService;
	private final FuncionarioService funcionarioService;
	private final AbstractService<Pais, UUID> abstractService;

	public RequerenteServiceImpl(RequerenteRepository repository, HttpServletRequest request,
			MessageSource messageSource, StorageService storageService, FuncionarioService funcionarioService,
			AbstractService<Pais, UUID> abstractService) {
		super(repository, request, messageSource);
		this.storageService = storageService;
		this.funcionarioService = funcionarioService;
		this.abstractService = abstractService;
	}

	@Override
	public Requerente create(UUID idFuncionario, UUID idPais, Requerente requerente, Optional<MultipartFile> fotoPerfil) {
		requerente.setPais(abstractService.findById(idPais));
		requerente.setFuncionario(funcionarioService.findById(idFuncionario));
		if (fotoPerfil.isPresent()) {
			requerente.setFotoPerfil(storageService.store(fotoPerfil.get()).getUrl());
		}
		try {
			return super.save(requerente);
		} catch (DataIntegrityViolationException e) {
			throw new IntegrityException(messageSource.getMessage("applicant.identity-card.already-exists", new String[]{requerente.getBilheteIdentidade()}, request.getLocale()));
		}
	}
	
	@Override
	public List<Requerente> save(List<Requerente> datas) {
		SecureRandom random = new SecureRandom();
		List<Pais> paises = abstractService.findAll();
		List<Funcionario> funcionarios = funcionarioService.findAll();
		return super.save(datas.stream().map(requerente -> {
			requerente.setPais(paises.get(random.nextInt(paises.size() - 1)));
			requerente.setFuncionario(funcionarios.get(random.nextInt(funcionarios.size() - 1)));
			return requerente;
		}).toList());
	}
	
	@Override
	public Requerente update(UUID idRequerente, UUID idPais, Requerente requerente) {
		Requerente entity = super.findById(idRequerente);
		entity.setNome(requerente.getNome());
		entity.setGenero(requerente.getGenero());
		entity.setMorada(requerente.getMorada());
		entity.setPais(abstractService.findById(idPais));
		entity.setDataNascimento(requerente.getDataNascimento());
		entity.setBilheteIdentidade(requerente.getBilheteIdentidade());
		try {
			return super.save(entity);
		} catch (DataIntegrityViolationException e) {
			throw new IntegrityException(messageSource.getMessage("applicant.identity-card.already-exists", new String[]{requerente.getBilheteIdentidade()}, request.getLocale()));
		}
	}
	
	@Override
	public Requerente updateProfilePhoto(UUID idRequerente, MultipartFile fotoPerfil) {
		Requerente entity = super.findById(idRequerente);
		Arquivo arquivo = storageService.store(fotoPerfil);
		storageService.delete(entity.getFotoPerfil());
		entity.setFotoPerfil(arquivo.getUrl());
		return super.save(entity);
	}
	
	@Override
	public Requerente deleteById(UUID id) {
		Requerente entity = super.deleteById(id);
		storageService.delete(entity.getFotoPerfil());		
		return entity;
	}
}
