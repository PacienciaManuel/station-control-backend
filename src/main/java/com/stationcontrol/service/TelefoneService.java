package com.stationcontrol.service;

import java.util.List;
import java.util.UUID;

import com.stationcontrol.model.Telefone;

public interface TelefoneService extends AbstractService<Telefone, UUID> {
	public Telefone create(UUID idFuncionario, Telefone telefone);
	public List<Telefone> create(UUID idFuncionario, List<Telefone> telefones);
}
