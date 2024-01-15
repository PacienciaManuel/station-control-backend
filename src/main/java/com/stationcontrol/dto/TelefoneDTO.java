package com.stationcontrol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TelefoneDTO {
	@NotBlank(message = "{TelefoneDTO.numero.notblank}")
	@Pattern(message = "{TelefoneDTO.numero.pattern}", regexp = "\\+244 \\d{9}")
	private String numero;
}
