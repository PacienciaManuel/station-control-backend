package com.stationcontrol.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stationcontrol.model.Suspeito;

public interface SuspeitoRepository extends JpaRepository<Suspeito, UUID> {
	
}
