package com.stationcontrol.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.stationcontrol.exception.BadRequestException;
import com.stationcontrol.model.NotificacaoFuncionario;
import com.stationcontrol.repository.NotificacaoFuncionarioRepository;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.service.NotificacaoFuncionariosService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class NotificacaoFuncionarioServiceImpl extends AbstractServiceImpl<NotificacaoFuncionario, UUID, NotificacaoFuncionarioRepository> implements NotificacaoFuncionariosService {
	private final FuncionarioService funcionarioService;
	
	
	public NotificacaoFuncionarioServiceImpl(NotificacaoFuncionarioRepository repository, HttpServletRequest request,
			MessageSource messageSource, FuncionarioService funcionarioService) {
		super(repository, request, messageSource);
		this.funcionarioService = funcionarioService;
	}

	@Override
	public NotificacaoFuncionario create(UUID idFuncionario, UUID idFuncionarioDestino, NotificacaoFuncionario notificacaoFuncionario) throws BadRequestException{
		notificacaoFuncionario.setFuncionario(funcionarioService.findById(idFuncionario));
		notificacaoFuncionario.setFuncionarioDestino(funcionarioService.findById(idFuncionarioDestino));
		return super.save(notificacaoFuncionario);
	}
	
	@Override
	public NotificacaoFuncionario update(UUID idNotificacao, NotificacaoFuncionario notificacaoFuncionario) {
		var entity = super.findById(idNotificacao);
		entity.setTitulo(notificacaoFuncionario.getTitulo());
		entity.setDescricao(notificacaoFuncionario.getDescricao());
		return super.save(entity);
	}
	
	@Override
	public NotificacaoFuncionario markSeen(UUID idNotificacao) throws BadRequestException {
		NotificacaoFuncionario entity = super.findById(idNotificacao);
		if (Boolean.TRUE.equals(entity.getVisto())) {
			throw new BadRequestException(messageSource.getMessage("notification.seen.already-marked-seen", new String[] {entity.getTitulo()}, request.getLocale()));
		}
		entity.setVisto(Boolean.TRUE);
		entity.setDataVisto(LocalDateTime.now());
		return super.save(entity);
	}

}
