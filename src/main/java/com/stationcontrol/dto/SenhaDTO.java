package com.stationcontrol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SenhaDTO {
	@NotBlank(message = "{FuncionarioDTO.senha.notblank}")
	@Size(min = 8, max = 16, message = "{FuncionarioDTO.senha.size}")
	private String nova;
	
	@NotBlank(message = "{FuncionarioDTO.senha.notblank}")
	@Size(min = 8, max = 16, message = "{FuncionarioDTO.senha.size}")
	private String antiga;
}
