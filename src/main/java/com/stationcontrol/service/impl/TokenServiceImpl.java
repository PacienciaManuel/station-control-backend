package com.stationcontrol.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.stationcontrol.exception.JWTAuthenticationException;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.model.Token;
import com.stationcontrol.repository.TokenRepository;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.service.JWTService;
import com.stationcontrol.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenServiceImpl extends AbstractServiceImpl<Token, String, TokenRepository> implements TokenService {
	
	private final JWTService jwtService;
	
	private final FuncionarioService funcionarioService;


	public TokenServiceImpl(TokenRepository repository, HttpServletRequest request, MessageSource messageSource,
			JWTService jwtService, FuncionarioService funcionarioService) {
		super(repository, request, messageSource);
		this.jwtService = jwtService;
		this.funcionarioService = funcionarioService;
	}

	@Override
	public Token create(Funcionario funcionario) {
		this.invalidateAll(funcionario);
		return super.save(Token.builder()
		.funcionario(funcionario)
		.tipo(jwtService.getType())
		.acesso(jwtService.generateAccessToken(funcionario))
		.atualizacao(jwtService.generateRefreshToken(funcionario))
		.build());
	}
	
	@Override
	public Token refresh(String authorization) {
		if (!this.repository.existsByAtualizacaoAndValido(jwtService.extractToken(authorization), Boolean.TRUE)) {
			throw new JWTAuthenticationException("Autorizaçao invalida.");
		}
		return this.create(funcionarioService.findByEmail(jwtService.extractSubjectRefreshToken(authorization)));
	}
	
	@Override
	public Token update(String id, Token data) {
		Token entity = this.findById(id);
		entity.setValido(data.getValido());
		return super.save(entity);
	}
	
	@Override
	public void invalidateAll(Funcionario funcionario) {
		super.save(this.repository.findAllByFuncionarioAndValido(funcionario, Boolean.TRUE).stream().map(token -> {
			token.setValido(Boolean.FALSE);
			return token;
		}).toList());
	}
}
