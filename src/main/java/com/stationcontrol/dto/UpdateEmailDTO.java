package com.stationcontrol.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateEmailDTO {
	@Email(message = "{FuncionarioDTO.email.email}")
	@NotBlank(message = "{FuncionarioDTO.email.notblank}")
	private String email;

	@NotBlank(message = "{FuncionarioDTO.senha.notblank}")
	@Size(min = 8, max = 16, message = "{FuncionarioDTO.senha.size}")
	private String senha;

}
