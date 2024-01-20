package com.stationcontrol.dto;

import java.time.LocalDate;

import com.stationcontrol.model.Genero;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequerenteUpdateDTO {
	@NotBlank(message = "{RequerenteDTO.nome.notblank}")
	private String nome;

	@NotNull(message = "{RequerenteDTO.genero.notnull}")
	private Genero genero;
	
	@NotNull(message = "{RequerenteDTO.dataNascimento.notnull}")
	private LocalDate dataNascimento;

	@NotBlank(message = "{RequerenteDTO.bilheteIdentidade.notblank}")
	private String morada;
	
	@NotBlank(message = "{RequerenteDTO.bilheteIdentidade.notblank}")
	private String bilheteIdentidade;
}
