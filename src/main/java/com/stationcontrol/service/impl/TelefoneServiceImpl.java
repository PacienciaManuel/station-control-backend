package com.stationcontrol.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.stationcontrol.model.Requerente;
import com.stationcontrol.model.Telefone;
import com.stationcontrol.repository.TelefoneRepository;
import com.stationcontrol.service.AbstractService;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.service.TelefoneService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TelefoneServiceimpl extends AbstractServiceImpl<Telefone, UUID, TelefoneRepository> implements TelefoneService {
	private final FuncionarioService funcionarioService;
	private final AbstractService<Requerente, UUID> requerenteService;

	
	public TelefoneServiceimpl(TelefoneRepository repository, HttpServletRequest request, MessageSource messageSource,
			FuncionarioService funcionarioService, AbstractService<Requerente, UUID> requerenteService) {
		super(repository, request, messageSource);
		this.funcionarioService = funcionarioService;
		this.requerenteService = requerenteService;
	}

	@Override
	public Telefone findByRequerente(UUID idRequerente) {
		return requerenteService.findById(idRequerente).getTelefone();
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
	public Telefone deleteByFuncionario(UUID idFuncionario, UUID idTelefone) {
		var funcionario = this.funcionarioService.findById(idFuncionario);
		var telefone = super.findById(idTelefone);
		funcionario.getTelefones().remove(telefone);
		funcionarioService.save(funcionario);
		return telefone;
	}
}
