package com.stationcontrol.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.stationcontrol.model.Telefone;

public interface TelefoneService extends AbstractService<Telefone, UUID> {
	public Telefone findByRequerente(UUID idRequerente);
	public Optional<Telefone> findBySuspeito(UUID idSuspeito);
	public List<Telefone> findAllByFuncionario(UUID idFuncionario);
	public Telefone createByFuncionario(UUID idFuncionario, Telefone telefone);
	public Telefone changeBySuspeito(UUID idSuspeito, Telefone telefone);
	public Telefone deleteByFuncionario(UUID idFuncionario, UUID idTelefone);
}
