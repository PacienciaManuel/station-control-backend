package com.stationcontrol.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.stationcontrol.exception.BadRequestException;
import com.stationcontrol.model.NotificacaoRequerente;
import com.stationcontrol.model.Requerente;
import com.stationcontrol.repository.NotificacaoRequerenteRepository;
import com.stationcontrol.service.AbstractService;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.service.NotificacaoRequerenteService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class NotificacaoRequerenteServiceImpl extends AbstractServiceImpl<NotificacaoRequerente, UUID, NotificacaoRequerenteRepository> implements NotificacaoRequerenteService {
	private final FuncionarioService funcionarioService;
	private final AbstractService<Requerente, UUID> requerenteService;
	
	public NotificacaoRequerenteServiceImpl(NotificacaoRequerenteRepository repository, HttpServletRequest request,
			MessageSource messageSource, FuncionarioService funcionarioService,
			AbstractService<Requerente, UUID> requerenteService) {
		super(repository, request, messageSource);
		this.funcionarioService = funcionarioService;
		this.requerenteService = requerenteService;
	}

	@Override
	public NotificacaoRequerente create(UUID idFuncionario, UUID idRequerenteDestino, NotificacaoRequerente notificacaoRequerente) throws BadRequestException{
		notificacaoRequerente.setFuncionario(funcionarioService.findById(idFuncionario));
		notificacaoRequerente.setRequerente(requerenteService.findById(idRequerenteDestino));
		return super.save(notificacaoRequerente);
	}
	
	@Override
	public NotificacaoRequerente update(UUID idNotificacao, NotificacaoRequerente notificacaoRequerente) {
		var entity = super.findById(idNotificacao);
		entity.setTitulo(notificacaoRequerente.getTitulo());
		entity.setRecebido(notificacaoRequerente.getRecebido());
		entity.setDescricao(notificacaoRequerente.getDescricao());
		return super.save(entity);
	}

	@Override
	public NotificacaoRequerente markReceived(UUID idNotificacao) throws BadRequestException {
		NotificacaoRequerente entity = super.findById(idNotificacao);
		if (Boolean.TRUE.equals(entity.getRecebido())) {
			throw new BadRequestException(messageSource.getMessage("notification.received.already-marked-received", new String[] {entity.getTitulo()}, request.getLocale()));
		}
		entity.setRecebido(Boolean.TRUE);
		entity.setDataRecebido(LocalDateTime.now());
		return super.save(entity);
	}
}
