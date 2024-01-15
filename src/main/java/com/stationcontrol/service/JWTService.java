package com.stationcontrol.service;

import com.stationcontrol.exception.JWTServiceException;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.service.impl.JWTServiceImpl.ClaimName;

public interface JWTService {
	public String getType();
	public String extractSubjectAccessToken(String authorization);
	public String extractSubjectRefreshToken(String authorization);
	public String generateAccessToken(Funcionario funcionario);
	public String generateRefreshToken(Funcionario funcionario) throws JWTServiceException;
	public String extractToken(String authorization) throws JWTServiceException;
	public <T> T extractClaim(String authorization, ClaimName claimName, Class<T> targetClass) throws JWTServiceException;
}
