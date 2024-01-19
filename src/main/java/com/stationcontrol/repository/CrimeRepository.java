package com.stationcontrol.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stationcontrol.model.Crime;

public interface CrimeRepository extends JpaRepository<Crime, UUID> {

}
