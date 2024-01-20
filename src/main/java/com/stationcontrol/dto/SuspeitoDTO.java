package com.stationcontrol.dto;

import java.time.LocalDate;

import com.stationcontrol.model.Genero;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SuspeitoDTO {
	@NotBlank(message = "{SuspeitoDTO.nome.notblank}")
	private String nome;

	@NotNull(message = "{SuspeitoDTO.genero.notnull}")
	private Genero genero;
	
	@NotNull(message = "{SuspeitoDTO.dataNascimento.notnull}")
	private LocalDate dataNascimento;
	
	@NotNull(message = "{SuspeitoDTO.detido.notnull}")
	private Boolean detido;

	@NotBlank(message = "{SuspeitoDTO.bilheteIdentidade.notblank}")
	private String bilheteIdentidade;

	@NotBlank(message = "{SuspeitoDTO.biografia.notblank}")
	private String biografia;
}
