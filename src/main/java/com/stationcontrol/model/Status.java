package com.stationcontrol.model;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public enum Status {
	ANALISE("Análise"), PROGRESSO("Progresso"), CONCLUIDO("Concluido");

	@Getter
	@JsonValue
	private final String descricao;
	
	public static Status of(String descricao) {
		for (Status status : Status.values()) {
			if (status.descricao.equalsIgnoreCase(descricao)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Status inválido: " + descricao);
	}
}
