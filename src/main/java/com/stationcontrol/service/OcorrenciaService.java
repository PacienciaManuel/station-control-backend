package com.stationcontrol.service;

import java.util.List;
import java.util.UUID;

import com.stationcontrol.model.Ocorrencia;

public interface OcorrenciaService extends AbstractService<Ocorrencia, UUID> {
	public Ocorrencia update(UUID idOcorrencia, UUID idRequerente, Ocorrencia ocorrencia, List<UUID> idsCrimes, List<UUID> idsSuspeitos);
	public Ocorrencia create(UUID idFuncionario, UUID idRequerente, Ocorrencia ocorrencia, List<UUID> idsCrimes, List<UUID> idsSuspeitos);
}
