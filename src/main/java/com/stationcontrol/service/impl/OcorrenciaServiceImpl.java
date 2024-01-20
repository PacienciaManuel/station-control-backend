package com.stationcontrol.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.stationcontrol.model.Crime;
import com.stationcontrol.model.Ocorrencia;
import com.stationcontrol.model.Requerente;
import com.stationcontrol.repository.OcorrenciaRepository;
import com.stationcontrol.service.AbstractService;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.service.OcorrenciaService;
import com.stationcontrol.service.SuspeitoService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class OcorrenciaServiceImpl extends AbstractServiceImpl<Ocorrencia, UUID, OcorrenciaRepository> implements OcorrenciaService {
	private final SuspeitoService suspeitoService;
	private final FuncionarioService funcionarioService;
	private final AbstractService<Crime, UUID>  crimeService;
	private final AbstractService<Requerente, UUID>  requerenteoService;

	public OcorrenciaServiceImpl(OcorrenciaRepository repository, HttpServletRequest request,
			MessageSource messageSource, SuspeitoService suspeitoService, FuncionarioService funcionarioService,
			AbstractService<Crime, UUID> crimeService, AbstractService<Requerente, UUID> requerenteoService) {
		super(repository, request, messageSource);
		this.suspeitoService = suspeitoService;
		this.funcionarioService = funcionarioService;
		this.crimeService = crimeService;
		this.requerenteoService = requerenteoService;
	}

	@Override
	public Ocorrencia create(UUID idFuncionario, UUID idRequerente, Ocorrencia ocorrencia, List<UUID> idsCrimes,
			List<UUID> idsSuspeitos) {
		ocorrencia.setRequerente(requerenteoService.findById(idRequerente));
		ocorrencia.setFuncionario(funcionarioService.findById(idFuncionario));
		ocorrencia.setCrimes(crimeService.findAllById(idsCrimes));
		ocorrencia.setSuspeitos(suspeitoService.findAllById(idsSuspeitos));
		return super.save(ocorrencia);
	}

	@Override
	public Ocorrencia update(UUID idOcorrencia, UUID idRequerente, Ocorrencia ocorrencia, List<UUID> idsCrimes,
			List<UUID> idsSuspeitos) {
		Ocorrencia entity = super.findById(idRequerente);
		
		entity.setCrimes(crimeService.findAllById(idsCrimes));
		entity.setSuspeitos(suspeitoService.findAllById(idsSuspeitos));
		entity.setRequerente(requerenteoService.findById(idRequerente));
		
		entity.setStatus(ocorrencia.getStatus());
		entity.setDescricao(ocorrencia.getDescricao());
		entity.setDataOcorrencia(ocorrencia.getDataOcorrencia());
		
		entity.getObjectos().clear();
		entity.getObjectos().addAll(ocorrencia.getObjectos());
		return super.save(entity);
	}
	
}
