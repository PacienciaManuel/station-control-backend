package com.stationcontrol.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stationcontrol.model.Requerente;
import com.stationcontrol.model.Suspeito;
import com.stationcontrol.model.Telefone;
import com.stationcontrol.repository.TelefoneRepository;
import com.stationcontrol.service.AbstractService;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.service.SuspeitoService;
import com.stationcontrol.service.TelefoneService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TelefoneServiceimpl extends AbstractServiceImpl<Telefone, UUID, TelefoneRepository> implements TelefoneService {
	private final SuspeitoService suspeitoService;
	private final FuncionarioService funcionarioService;
	private final AbstractService<Requerente, UUID> requerenteService;

	public TelefoneServiceimpl(TelefoneRepository repository, HttpServletRequest request, MessageSource messageSource,
			SuspeitoService suspeitoService, FuncionarioService funcionarioService,
			AbstractService<Requerente, UUID> requerenteService) {
		super(repository, request, messageSource);
		this.suspeitoService = suspeitoService;
		this.funcionarioService = funcionarioService;
		this.requerenteService = requerenteService;
	}

	@Override
	public Telefone findByRequerente(UUID idRequerente) {
		return requerenteService.findById(idRequerente).getTelefone();
	}
	
	@Override
	public Optional<Telefone> findBySuspeito(UUID idSuspeito) {
		return super.repository.findBySuspeito(Suspeito.builder().id(idSuspeito).build());
	}
	
	@Override
	public List<Telefone> findAllByFuncionario(UUID idFuncionario) {
		return this.funcionarioService.findById(idFuncionario).getTelefones();
	}

	@Override
	public Telefone createByFuncionario(UUID idFuncionario, Telefone telefone) {
		var funcionario = this.funcionarioService.findById(idFuncionario);
		funcionario.getTelefones().add(telefone);
		funcionarioService.save(funcionario);
		return telefone;
	}
	
	@Override
	@Transactional
	public Telefone changeBySuspeito(UUID idSuspeito, Telefone telefone) {
		Suspeito suspeito = this.suspeitoService.findById(idSuspeito);
		if (suspeito.getTelefone() == null) {
			suspeito.setTelefone(super.save(telefone));
		} else {
			suspeito.getTelefone().setNumero(telefone.getNumero());
			return super.save(suspeito.getTelefone());
		}
		return suspeitoService.save(suspeito).getTelefone();
	}

	@Override
	public Telefone deleteByFuncionario(UUID idFuncionario, UUID idTelefone) {
		var funcionario = this.funcionarioService.findById(idFuncionario);
		var telefone = super.findById(idTelefone);
		funcionario.getTelefones().remove(telefone);
		funcionarioService.save(funcionario);
		return telefone;
	}
}
