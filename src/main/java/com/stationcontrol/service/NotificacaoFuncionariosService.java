package com.stationcontrol.service;

import java.util.List;
import java.util.UUID;

import com.stationcontrol.exception.BadRequestException;
import com.stationcontrol.model.NotificacaoFuncionario;

public interface NotificacaoFuncionariosService extends AbstractService<NotificacaoFuncionario, UUID> {
	public NotificacaoFuncionario create(UUID idFuncionario, UUID idFuncionarioDestino, NotificacaoFuncionario notificacaoFuncionario);
	public List<NotificacaoFuncionario> create(List<NotificacaoFuncionario> notificacoesFuncionarios);
	public NotificacaoFuncionario update(UUID idNotificacao, NotificacaoFuncionario notificacao);
	public NotificacaoFuncionario markSeen(UUID idNotificacao) throws BadRequestException;
	
}
