package com.stationcontrol.controller;

import java.util.List;
import java.util.UUID;

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
	
	@GetMapping("/{idFuncionario}")
	public ResponseEntity<List<Telefone>> findAllByFuncionario(@PathVariable UUID idFuncionario) {
		return this.ok(telefoneService.findAllByFuncionario(idFuncionario));
	}
	
	@GetMapping("/requerente/{idRequerente}")
	public ResponseEntity<Telefone> findByRequerente(@PathVariable UUID idRequerente) {
		return this.ok(telefoneService.findByRequerente(idRequerente));
	}
	
	@GetMapping("/contador")
	public ResponseEntity<Long> count() {
		return this.ok(telefoneService.count());
	}
	
	@PostMapping("/funcionario/{idFuncionario}")
	public ResponseEntity<Telefone> createByFuncionario(@PathVariable UUID idFuncionario, @RequestBody @Valid TelefoneDTO telefoneDTO) {
		return this.ok(telefoneService.createByFuncionario(idFuncionario, Telefone.builder().numero(telefoneDTO.getNumero()).build()));
	}
	
	@DeleteMapping("/funcionario/{idFuncionario}/{idTelefone}")
	public ResponseEntity<Telefone> deleteByFuncionario(@PathVariable UUID idFuncionario, @PathVariable UUID idTelefone) {
		return this.ok(telefoneService.deleteByFuncionario(idFuncionario, idTelefone));
	}
}
