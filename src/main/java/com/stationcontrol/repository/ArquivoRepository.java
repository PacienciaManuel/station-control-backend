package com.stationcontrol.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.stationcontrol.model.Arquivo;
import com.stationcontrol.model.Ocorrencia;


public interface ArquivoRepository extends JpaRepository<Arquivo, UUID> {
	public List<Arquivo> findByOcorrencia(Ocorrencia ocorrencia);
	public Page<Arquivo> findByOcorrencia(Ocorrencia ocorrencia, Pageable pageable);

}
