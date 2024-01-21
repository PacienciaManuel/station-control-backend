package com.stationcontrol.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.stationcontrol.exception.BadRequestException;
import com.stationcontrol.model.Funcionario;
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
		notificacaoFuncionario.setVisto(Boolean.FALSE);
		return super.save(notificacaoFuncionario);
	}
	
	@Override
	public List<NotificacaoFuncionario> create(List<NotificacaoFuncionario> notificacoesFuncionarios) {
		SecureRandom random = new SecureRandom();
		List<Funcionario> funcionarios = funcionarioService.findAll();
		return super.save(notificacoesFuncionarios.stream().map(notificacao -> {
			notificacao.setFuncionario(funcionarios.get(random.nextInt(funcionarios.size() - 1)));
			notificacao.setFuncionarioDestino(funcionarios.get(random.nextInt(funcionarios.size() - 1)));
			notificacao.setVisto(random.nextBoolean());
			notificacao.setDataVisto(Boolean.TRUE.equals(notificacao.getVisto()) ? LocalDateTime.now() : null);
			return notificacao;
		}).toList());
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
