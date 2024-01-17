package com.stationcontrol.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stationcontrol.model.Pais;

public interface PaisRepository extends JpaRepository<Pais, UUID> {

}
