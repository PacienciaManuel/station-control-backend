package com.stationcontrol.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.stationcontrol.model.Arquivo;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.model.Ocorrencia;
import com.stationcontrol.model.Requerente;
import com.stationcontrol.repository.OcorrenciaRepository;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.service.OcorrenciaService;
import com.stationcontrol.service.RequerenteService;
import com.stationcontrol.storage.StorageService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class OcorrenciaServiceImpl extends AbstractServiceImpl<Ocorrencia, UUID, OcorrenciaRepository> implements OcorrenciaService {
	private final StorageService  storageService;
	private final RequerenteService  requerenteoService;
	private final FuncionarioService funcionarioService;
	
	public OcorrenciaServiceImpl(OcorrenciaRepository repository, HttpServletRequest request,
			MessageSource messageSource, StorageService storageService, RequerenteService requerenteoService,
			FuncionarioService funcionarioService) {
		super(repository, request, messageSource);
		this.storageService = storageService;
		this.requerenteoService = requerenteoService;
		this.funcionarioService = funcionarioService;
	}

	@Override
	public Page<Ocorrencia> paginationByDataCriacaoBetween(int page, int size, Optional<UUID> idFuncionario, LocalDateTime dataInicio,
			LocalDateTime dataFim, String orderBy, Direction direction) {
		return idFuncionario.isEmpty() 
		? super.repository.findByDataCriacaoBetween(dataInicio, dataFim, PageRequest.of(page, size, Sort.by(direction, orderBy)))
		: super.repository.findByFuncionarioAndDataCriacaoBetween(Funcionario.builder().id(idFuncionario.get()).build(), dataInicio, dataFim, PageRequest.of(page, size, Sort.by(direction, orderBy)));
	}

	@Override
	public Page<Ocorrencia> paginationByDataOcorrenciaBetween(int page, int size, Optional<UUID> idFuncionario, LocalDateTime dataInicio,
			LocalDateTime dataFim, String orderBy, Direction direction) {
		return idFuncionario.isEmpty() 
		? super.repository.findByDataOcorrenciaBetween(dataInicio, dataFim, PageRequest.of(page, size, Sort.by(direction, orderBy)))
		: super.repository.findByFuncionarioAndDataOcorrenciaBetween(Funcionario.builder().id(idFuncionario.get()).build(), dataInicio, dataFim, PageRequest.of(page, size, Sort.by(direction, orderBy)));
	}

	@Override
	public Long countByDataCriacaoBetween(Optional<UUID> idFuncionario, LocalDateTime dataInicio, LocalDateTime dataFim) {
		return idFuncionario.isEmpty() 
		? super.repository.countByDataCriacaoBetween(dataInicio, dataFim)
		: super.repository.countByFuncionarioAndDataCriacaoBetween(Funcionario.builder().id(idFuncionario.get()).build(), dataInicio, dataFim);
	}

	@Override
	public Long countByDataOcorrenciaBetween(Optional<UUID> idFuncionario, LocalDateTime dataInicio, LocalDateTime dataFim) {
		return idFuncionario.isEmpty() 
				? super.repository.countByDataOcorrenciaBetween(dataInicio, dataFim)
				: super.repository.countByFuncionarioAndDataOcorrenciaBetween(Funcionario.builder().id(idFuncionario.get()).build(), dataInicio, dataFim);
	}

	@Override
	public Ocorrencia create(UUID idFuncionario, UUID idRequerente, Ocorrencia ocorrencia) {
		ocorrencia.setRequerente(requerenteoService.findById(idRequerente));
		ocorrencia.setFuncionario(funcionarioService.findById(idFuncionario));
		ocorrencia.setTotalCrimes(0L);
		ocorrencia.setTotalObjectos(0L);
		ocorrencia.setTotalSuspeitos(0L);
		return super.save(ocorrencia);
	}
	
	@Override
	public List<Ocorrencia> create(List<Ocorrencia> ocorrencias) {
		SecureRandom random = new SecureRandom();
		List<Requerente> requerentes = requerenteoService.findAll();
		List<Funcionario> funcionarios = funcionarioService.findAll();
		return super.save(ocorrencias.stream().map(ocorrencia -> {
			ocorrencia.setRequerente(requerentes.get(random.nextInt(requerentes.size() - 1)));
			ocorrencia.setFuncionario(funcionarios.get(random.nextInt(funcionarios.size() - 1)));
			return ocorrencia;
		}).toList());
	}

	@Override
	public Ocorrencia update(UUID idOcorrencia, UUID idRequerente, Ocorrencia ocorrencia) {
		Ocorrencia entity = super.findById(idOcorrencia);
		entity.setRequerente(requerenteoService.findById(idRequerente));
		entity.setStatus(ocorrencia.getStatus());
		entity.setDescricao(ocorrencia.getDescricao());
		entity.setDataOcorrencia(ocorrencia.getDataOcorrencia());
		return super.save(entity);
	}
	
	@Override
	public Ocorrencia deleteById(UUID id) {
		Ocorrencia entity = super.findById(id);
		for (Arquivo arquivo : entity.getArquivos()) {
			storageService.delete(arquivo.getUrl());
		}
		entity.getCrimes().clear();
		entity.getObjectos().clear();
		return super.delete(super.save(entity));
	}
}
