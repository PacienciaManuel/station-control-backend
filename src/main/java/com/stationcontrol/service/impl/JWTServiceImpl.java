package com.stationcontrol.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.stationcontrol.exception.JWTServiceException;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.service.JWTService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JWTServiceImpl implements JWTService {
	private final String type;
	private final String secretAccessToken;
	private final String secretRefreshToken;
	private final Long expirationAccessToken;
	private final Long expirationRefreshToken;
	private final HttpServletRequest request;
	private final MessageSource messageSource;
	
	public JWTServiceImpl(@Value("${application.jwt.type}") String type, @Value("${application.jwt.access-token.secret}") String secretAccessToken, 
			@Value("${application.jwt.refresh-token.secret}") String secretRefreshToken, @Value("${application.jwt.access-token.expiration}") Long expirationAccessToken,
			@Value("${application.jwt.refresh-token.expiration}") Long expirationRefreshToken, HttpServletRequest request, MessageSource messageSource) {
		super();
		this.type = type;
		this.secretAccessToken = secretAccessToken;
		this.secretRefreshToken = secretRefreshToken;
		this.expirationAccessToken = expirationAccessToken;
		this.expirationRefreshToken = expirationRefreshToken;
		this.request = request;
		this.messageSource = messageSource;
	}

	@Override
	public String getType() {
		return this.type;
	}

	private String generateToken(Funcionario funcionario, String secret, Date expiration) {
		return JWT.create()
		.withSubject(funcionario.getId().toString())
		.withExpiresAt(expiration)
		.withClaim("papel", funcionario.getPapel().getAuthority())
		.sign(Algorithm.HMAC512(secret));
	}

	@Override
	public String generateAccessToken(Funcionario funcionario) {
		return generateToken(funcionario, secretAccessToken, new Date(System.currentTimeMillis() + expirationAccessToken));
	}

	@Override
	public String generateRefreshToken(Funcionario funcionario) throws JWTServiceException {
		return generateToken(funcionario, secretRefreshToken, new Date(System.currentTimeMillis() + expirationRefreshToken));
	}

	@Override
	public UUID extractSubjectAccessToken(String authorization) {
		try {
			return UUID.fromString(JWT.require(Algorithm.HMAC512(secretAccessToken)).build().verify(this.extractToken(authorization)).getSubject());
		} catch (JWTCreationException | JWTServiceException | JWTVerificationException | IllegalArgumentException e) {
			throw new JWTServiceException(e);
		}
	}

	@Override
	public UUID extractSubjectRefreshToken(String authorization) {
		try {
			return UUID.fromString(JWT.require(Algorithm.HMAC512(secretRefreshToken)).build().verify(this.extractToken(authorization)).getSubject());
		} catch (JWTCreationException | JWTServiceException | JWTVerificationException | IllegalArgumentException e) {
			throw new JWTServiceException(e);
		}
	}
	
	public <T> T extractClaim(String authorization, String claimName, Class<T> targetClass, String secret) throws JWTServiceException {
		try {
			return JWT.require(Algorithm.HMAC512(secret)).build().verify(this.extractToken(authorization)).getClaim(claimName).as(targetClass);
		} catch (JWTCreationException | JWTServiceException | JWTVerificationException | IllegalArgumentException e) {
			throw new JWTServiceException(e);
		}
	}
	
	@Override
	public <T> T extractClaimAccessToken(String authorization, String claimName, Class<T> targetClass) throws JWTServiceException {
		return this.extractClaim(authorization, claimName, targetClass, secretAccessToken);
	}
	
	@Override
	public <T> T extractClaimRefreshToken(String authorization, String claimName, Class<T> targetClass) throws JWTServiceException {
		return this.extractClaim(authorization, claimName, targetClass, secretRefreshToken);
	}

	@Override
	public String extractToken(String authorization) throws JWTServiceException {
		if (!authorization.startsWith(type)) throw new JWTServiceException(messageSource.getMessage("jwt.invalid.authorization", null, request.getLocale()));
		return authorization.substring(type.length());
	}
}
