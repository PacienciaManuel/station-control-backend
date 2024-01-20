package com.stationcontrol.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stationcontrol.model.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, UUID> {

}
