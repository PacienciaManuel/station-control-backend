package com.stationcontrol.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.stationcontrol.model.Ocorrencia;

public interface OcorrenciaService extends AbstractService<Ocorrencia, UUID> {
	public Long countByDataCriacaoBetween(Optional<UUID> idFuncionario, LocalDateTime dataInicio, LocalDateTime dataFim);
	public Long countByDataOcorrenciaBetween(Optional<UUID> idFuncionario, LocalDateTime dataInicio, LocalDateTime dataFim);
	public Page<Ocorrencia> paginationByDataCriacaoBetween(int page, int size, Optional<UUID> idFuncionario, LocalDateTime dataInicio, LocalDateTime dataFim, String orderBy, Direction direction);
	public Page<Ocorrencia> paginationByDataOcorrenciaBetween(int page, int size, Optional<UUID> idFuncionario, LocalDateTime dataInicio, LocalDateTime dataFim, String orderBy, Direction direction);
	public Ocorrencia create(UUID idFuncionario, UUID idRequerente, Ocorrencia ocorrencia);
	public List<Ocorrencia> create(List<Ocorrencia> ocorrencias);
	public Ocorrencia update(UUID idOcorrencia, UUID idRequerente, Ocorrencia ocorrencia);	
}
