package com.stationcontrol.dto;

import java.time.LocalDateTime;

import com.stationcontrol.model.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OcorrenciaDTO {
	@NotNull(message = "{OcorrenciaDTO.status.notnull}")
	private Status status;
	
	@NotBlank(message = "{OcorrenciaDTO.descricao.notblank}")
	private String descricao;
	
	@NotNull(message = "{OcorrenciaDTO.dataOcorrencia.notnull}")
	private LocalDateTime dataOcorrencia;
}
