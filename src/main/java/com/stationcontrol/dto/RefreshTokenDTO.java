package com.stationcontrol.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefreshTokenDTO {
	@NotBlank(message = "{RefreshTokenDTO.authorization.notblank}")
	private String authorization;
}
