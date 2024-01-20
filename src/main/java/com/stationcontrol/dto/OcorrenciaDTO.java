package com.stationcontrol.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

	@NotNull(message = "{OcorrenciaDTO.objectos.notnull}")
	private List<String> objectos;

	@NotNull(message = "{OcorrenciaDTO.crimes.notnull}")
	private List<UUID> crimes;

	@NotNull(message = "{OcorrenciaDTO.suspeitos.notnull}")
	private List<UUID> suspeitos;
}
