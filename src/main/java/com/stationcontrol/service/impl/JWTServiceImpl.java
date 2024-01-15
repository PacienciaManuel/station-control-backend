package com.stationcontrol.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.stationcontrol.exception.JWTServiceException;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.service.JWTService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
public class JWTServiceImpl implements JWTService {

	@Value("${application.jwt.type}")
	private String type;

	@Value("${application.jwt.access-token.secret}")
	private String secretAccessToken;
	
	@Value("${application.jwt.refresh-token.secret}")
	private String secretRefreshToken;
	
	@Value("${application.jwt.access-token.expiration}")
	private Long expirationAccessToken;
	
	@Value("${application.jwt.refresh-token.expiration}")
	private Long expirationRefreshToken;
	
	@Override
	public String getType() {
		return this.type;
	}

	private String generateToken(Funcionario funcionario, String secret, Date expiration) {
		return JWT.create()
		.withSubject(funcionario.getEmail())
		.withExpiresAt(expiration)
		.withClaim(ClaimName.ID.descricao, funcionario.getId().toString())
		.withClaim(ClaimName.PAPEL.descricao, funcionario.getPapel().toString())
		.sign(Algorithm.HMAC512(secret));
	}
	
	
	@Override
	public String generateAccessToken(Funcionario funcionario) {
		return generateToken(funcionario, secretAccessToken, new Date(System.currentTimeMillis() + expirationAccessToken));
	}
	
	@Override
	public String generateRefreshToken(Funcionario funcionario) {
		return generateToken(funcionario, secretRefreshToken, new Date(System.currentTimeMillis() + expirationRefreshToken));
	}

	@Override
	public String extractSubjectAccessToken(String authorization) throws JWTServiceException {
		try {
			return JWT.require(Algorithm.HMAC512(secretAccessToken)).build().verify(this.extractToken(authorization)).getSubject();
		} catch (JWTCreationException | JWTServiceException | JWTVerificationException | IllegalArgumentException e) {
			throw new JWTServiceException(e);
		}
	}

	@Override
	public String extractSubjectRefreshToken(String authorization) throws JWTServiceException {
		try {
			return JWT.require(Algorithm.HMAC512(secretRefreshToken)).build().verify(this.extractToken(authorization)).getSubject();
		} catch (JWTCreationException | JWTServiceException | JWTVerificationException | IllegalArgumentException e) {
			throw new JWTServiceException(e);
		}
	}
	
	@Override
	public <T> T extractClaim(String authorization, ClaimName claimName, Class<T> targetClass) throws JWTServiceException {
		try {
			return JWT.require(Algorithm.HMAC512(secretAccessToken)).build().verify(this.extractToken(authorization)).getClaim(claimName.descricao).as(targetClass);
		} catch (JWTCreationException | JWTServiceException | JWTVerificationException | IllegalArgumentException e) {
			throw new JWTServiceException(e);
		}
	}
	
	@Override
	public String extractToken(String authorization) throws JWTServiceException {
		if (!authorization.startsWith(type)) throw new JWTServiceException("Autorização inválido: " + authorization);
		return authorization.substring(type.length());
	}
	
	@RequiredArgsConstructor
	public enum ClaimName {
		ID("id"), PAPEL("papel");
		
		@Getter
		private final String descricao;
	}

}
