package com.stationcontrol.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stationcontrol.model.NotificacaoFuncionario;

public interface NotificacaoFuncionarioRepository extends JpaRepository<NotificacaoFuncionario, UUID> {

}
