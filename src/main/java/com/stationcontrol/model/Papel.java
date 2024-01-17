package com.stationcontrol.model;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public enum Papel implements GrantedAuthority {
	USUARIO("Usuário"), ADMINISTRADOR("Administrador");

	@Getter
	@JsonValue
	private final String descricao;
	
	public static Papel of(String descricao) {
		for (Papel genero : Papel.values()) {
			if (genero.descricao.equalsIgnoreCase(descricao)) {
				return genero;
			}
		}
		throw new IllegalArgumentException("Papel inválido: " + descricao);
	}

	@Override
	public String getAuthority() {
		return descricao;
	}
}
