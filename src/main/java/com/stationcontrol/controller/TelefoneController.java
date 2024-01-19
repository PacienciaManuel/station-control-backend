package com.stationcontrol.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stationcontrol.dto.TelefoneDTO;
import com.stationcontrol.model.Telefone;
import com.stationcontrol.service.TelefoneService;
import com.stationcontrol.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/telefones")
public class TelefoneController extends BaseController {
	private final TelefoneService telefoneService;
	
	@GetMapping("/{idFuncionaro}")
	public ResponseEntity<List<Telefone>> findAllByFuncionario(@PathVariable UUID idFuncionaro) {
		return this.ok(telefoneService.findAllByFuncionario(idFuncionaro));
	}
	
	@PostMapping("/{idFuncionaro}")
	public ResponseEntity<Telefone> createByFuncionario(@PathVariable UUID idFuncionaro, @RequestBody @Valid TelefoneDTO telefoneDTO) {
		var telefone = Telefone.builder().build();
		BeanUtils.copyProperties(telefoneDTO, telefone);
		return this.ok(telefoneService.createByFuncionario(idFuncionaro, telefone));
	}
	
	@DeleteMapping("/{idFuncionaro}/{idTelefone}")
	public ResponseEntity<Telefone> deleteByFuncionario(@PathVariable UUID idFuncionaro, @PathVariable UUID idTelefone) {
		return this.ok(telefoneService.deleteByFuncionario(idFuncionaro, idTelefone));
	}
}
