package com.stationcontrol.model.converter;

import com.stationcontrol.model.Status;

import jakarta.persistence.AttributeConverter;

public class StatusConverter implements AttributeConverter<Status, String> {

	@Override
	public String convertToDatabaseColumn(Status attribute) {
		return attribute.getDescricao();
	}

	@Override
	public Status convertToEntityAttribute(String dbData) {
		return Status.of(dbData);
	}

}
