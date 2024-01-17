package com.stationcontrol.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.stationcontrol.model.Telefone;
import com.stationcontrol.repository.TelefoneRepository;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.service.TelefoneService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TelefoneServiceImpl extends AbstractServiceImpl<Telefone, UUID, TelefoneRepository> implements TelefoneService {
	private final FuncionarioService funcionarioService;
	
	public TelefoneServiceImpl(TelefoneRepository repository, HttpServletRequest request, MessageSource messageSource,
			FuncionarioService funcionarioService) {
		super(repository, request, messageSource);
		this.funcionarioService = funcionarioService;
	}

	@Override
	public Telefone create(UUID idFuncionario, Telefone telefone) {
		telefone.setFuncionario(this.funcionarioService.findById(idFuncionario));
		return super.save(telefone);
	}
	
	@Override
	public List<Telefone> create(UUID idFuncionario, List<Telefone> telefones) {
		var entity = this.funcionarioService.findById(idFuncionario);
		return super.save(telefones.stream().map(telefone -> {
			telefone.setFuncionario(entity);
			return telefone;
		}).toList());
	}
}
