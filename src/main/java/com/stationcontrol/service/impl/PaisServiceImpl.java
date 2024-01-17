package com.stationcontrol.service.impl;

import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.stationcontrol.model.Pais;
import com.stationcontrol.repository.PaisRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PaisServiceImpl extends AbstractServiceImpl<Pais, UUID, PaisRepository> {

	protected PaisServiceImpl(PaisRepository repository, HttpServletRequest request, MessageSource messageSource) {
		super(repository, request, messageSource);
	}
	
}
