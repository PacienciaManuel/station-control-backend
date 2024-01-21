package com.stationcontrol.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stationcontrol.model.Funcionario;
import com.stationcontrol.model.Ocorrencia;
import com.stationcontrol.model.Suspeito;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, UUID> {
	public Long countByDataCriacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
	public Long countByDataOcorrenciaBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
	public Long countByFuncionarioAndDataCriacaoBetween(Funcionario funcionario, LocalDateTime dataInicio, LocalDateTime dataFim);
	public Long countByFuncionarioAndDataOcorrenciaBetween(Funcionario funcionario, LocalDateTime dataInicio, LocalDateTime dataFim);

	@Query(value = "select count(so.ocorrencia_id) from suspeitos_ocorrencias so where so.suspeito_id=?1", nativeQuery = true)
	public Long countBySuspeito(UUID idSuspeito);
	
	@Query(value = "select objecto from objectos", nativeQuery = true)
	public Page<String> paginationObjects(Pageable pageable);

	@Query(value = "select * from ocorrencias o left join suspeitos_ocorrencias so on (so.ocorrencia_id=o.id and so.suspeito_id=?1)", nativeQuery = true)
	public Page<Ocorrencia> paginationBySuspeito(UUID idSuspeito, Pageable pageable);
	
	public Page<Ocorrencia> findByDataCriacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);
	public Page<Ocorrencia> findByDataOcorrenciaBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);
	
	public Page<Ocorrencia> findByFuncionarioAndDataCriacaoBetween(Funcionario funcionario, LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);
	public Page<Ocorrencia> findByFuncionarioAndDataOcorrenciaBetween(Funcionario funcionario, LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);
	
	@Query(value = "select objecto from objectos where ocorrencia_id=?1", nativeQuery = true)
	public Page<String> paginationObjects(UUID idOcorrencia, Pageable pageable);
	
	public Page<Suspeito> findById(UUID idOcorrencia, Pageable pageable);
}
