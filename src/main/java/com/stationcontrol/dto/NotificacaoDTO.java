package com.stationcontrol.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificacaoDTO {
	@NotBlank(message = "{NotificacaoDTO.titulo.notblank}")
	private String titulo;
	
	@NotBlank(message = "{NotificacaoDTO.descricao.notblank}")
	private String descricao;
}
