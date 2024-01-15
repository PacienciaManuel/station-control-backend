package com.stationcontrol.dto;

import java.time.LocalDate;

import com.stationcontrol.model.Genero;
import com.stationcontrol.model.Papel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FuncionarioDTO {
	@NotBlank(message = "{FuncionarioDTO.nome.notblank}")
	private String nome;

	@NotNull(message = "{FuncionarioDTO.genero.notnull}")
	private Genero genero;
	
	@NotNull(message = "{FuncionarioDTO.dataNascimento.notnull}")
	private LocalDate dataNascimento;

	@NotBlank(message = "{FuncionarioDTO.email.notblank}")
	private String email;

	@NotBlank(message = "{FuncionarioDTO.morada.notblank}")
	private String morada;

	@NotNull(message = "{FuncionarioDTO.papel.notnull}")
	private Papel papel;
	
	@NotBlank(message = "{FuncionarioDTO.senha.notblank}")
	@Size(min = 8, max = 16, message = "{FuncionarioDTO.senha.size}")
	private String senha;

	@NotBlank(message = "{FuncionarioDTO.notaInformativa.notblank}")
	private String notaInformativa;
}
