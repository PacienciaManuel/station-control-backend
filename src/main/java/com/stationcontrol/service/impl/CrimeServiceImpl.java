package com.stationcontrol.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.stationcontrol.model.Crime;
import com.stationcontrol.model.Ocorrencia;
import com.stationcontrol.repository.CrimeRepository;
import com.stationcontrol.service.CrimeService;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.service.OcorrenciaService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CrimeServiceImpl extends AbstractServiceImpl<Crime, UUID, CrimeRepository> implements CrimeService {
	private final OcorrenciaService ocorrenciaService;
	private final FuncionarioService funcionarioService;
	
	public CrimeServiceImpl(CrimeRepository repository, HttpServletRequest request, MessageSource messageSource,
			OcorrenciaService ocorrenciaService, FuncionarioService funcionarioService) {
		super(repository, request, messageSource);
		this.ocorrenciaService = ocorrenciaService;
		this.funcionarioService = funcionarioService;
	}

	@Override
	public Page<Crime> paginationByOccurrence(int page, int size, String descricao, UUID idOcorrencia, String orderBy, Direction direction) {
		return super.repository.paginationByOccurrence(idOcorrencia, descricao, PageRequest.of(page, size, direction, orderBy));
	}

	@Override
	public List<Crime> findAllByOcorrencia(UUID idOcorrencia) {
		return ocorrenciaService.findById(idOcorrencia).getCrimes();
	}
	
	@Override
	public Crime create(UUID idFuncionario, Crime crime) {
		crime.setFuncionario(funcionarioService.findById(idFuncionario));
		return super.save(crime);
	}
	
	@Override
	public Crime createByOcorrencia(UUID idOcorrencia, UUID idCrime) {
		Crime crime = super.findById(idCrime);
		Ocorrencia ocorrencia = ocorrenciaService.findById(idOcorrencia);
		ocorrencia.getCrimes().add(crime);
		ocorrenciaService.save(ocorrencia);
		return crime;
	}

	@Override
	public List<Crime> createByOcorrencia(UUID idOcorrencia, List<UUID> idsCrimes) {
		List<Crime> crimes = super.findAllById(idsCrimes);
		Ocorrencia ocorrencia = ocorrenciaService.findById(idOcorrencia);
		ocorrencia.getCrimes().addAll(crimes);
		ocorrenciaService.save(ocorrencia);
		return crimes;
	}

	@Override
	public Crime deleteByOcorrencia(UUID idOcorrencia, UUID idCrime) {
		Crime crime = super.findById(idCrime);
		Ocorrencia ocorrencia = ocorrenciaService.findById(idOcorrencia);
		ocorrencia.getCrimes().remove(crime);
		ocorrenciaService.save(ocorrencia);
		return crime;
	}
	
}
