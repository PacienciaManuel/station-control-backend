package com.stationcontrol.dto;

import java.time.LocalDate;

import com.stationcontrol.model.Genero;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FuncionarioUpdateDTO {
	@NotBlank(message = "{FuncionarioDTO.nome.notblank}")
	private String nome;

	@NotNull(message = "{FuncionarioDTO.genero.notnull}")
	private Genero genero;
	
	@NotNull(message = "{FuncionarioDTO.dataNascimento.notnull}")
	private LocalDate dataNascimento;

	@NotBlank(message = "{FuncionarioDTO.morada.notblank}")
	private String morada;

	@NotBlank(message = "{FuncionarioDTO.notaInformativa.notblank}")
	private String notaInformativa;
}
