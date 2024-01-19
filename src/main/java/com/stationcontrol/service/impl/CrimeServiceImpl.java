package com.stationcontrol.service.impl;

import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.stationcontrol.model.Crime;
import com.stationcontrol.repository.CrimeRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CrimeServiceImpl extends AbstractServiceImpl<Crime, UUID, CrimeRepository> {

	protected CrimeServiceImpl(CrimeRepository repository, HttpServletRequest request, MessageSource messageSource) {
		super(repository, request, messageSource);
	}
	
}
