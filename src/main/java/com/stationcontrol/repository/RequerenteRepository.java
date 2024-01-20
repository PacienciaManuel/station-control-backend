package com.stationcontrol.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stationcontrol.model.Requerente;

public interface RequerenteRepository extends JpaRepository<Requerente, UUID> {

}
