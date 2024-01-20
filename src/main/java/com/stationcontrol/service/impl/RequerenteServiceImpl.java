package com.stationcontrol.service.impl;

import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.stationcontrol.exception.IntegrityException;
import com.stationcontrol.model.Requerente;
import com.stationcontrol.repository.RequerenteRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class RequerenteServiceImpl extends AbstractServiceImpl<Requerente, UUID, RequerenteRepository> {

	public RequerenteServiceImpl(RequerenteRepository repository, HttpServletRequest request,
			MessageSource messageSource) {
		super(repository, request, messageSource);
	}
	
	@Override
	public Requerente save(Requerente data) {
		try {			
			return super.save(data);
		} catch (DataIntegrityViolationException e) {
			throw new IntegrityException(messageSource.getMessage("applicant.identity-card.already-exists", new String[]{data.getBilheteIdentidade()}, request.getLocale()));
		}
	}
	
}
