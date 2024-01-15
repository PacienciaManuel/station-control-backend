package com.stationcontrol.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stationcontrol.model.Telefone;

public interface TelefoneRepository extends JpaRepository<Telefone, UUID> {

}
