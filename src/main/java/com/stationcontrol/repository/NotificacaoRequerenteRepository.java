package com.stationcontrol.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stationcontrol.model.NotificacaoRequerente;

public interface NotificacaoRequerenteRepository extends JpaRepository<NotificacaoRequerente, UUID> {

}
