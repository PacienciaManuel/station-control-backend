package com.stationcontrol.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stationcontrol.dto.OcorrenciaDTO;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.model.Ocorrencia;
import com.stationcontrol.model.Requerente;
import com.stationcontrol.model.Status;
import com.stationcontrol.service.OcorrenciaService;
import com.stationcontrol.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ocorrencias")
public class OcorrenciaController extends BaseController {
	private final OcorrenciaService ocorrenciaService;
	
	@GetMapping
	public ResponseEntity<List<Ocorrencia>> findAll(
			@RequestParam(required = false) UUID idFuncionario, @RequestParam(required = false) UUID idRequerente,
			@RequestParam(required = false) Status status, @RequestParam(required = false) LocalDateTime dataOcorrencia,  
			@RequestParam(defaultValue = "nome") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(ocorrenciaService.findAll(
			Example.of(Ocorrencia.builder()
			.status(status)
			.dataOcorrencia(dataOcorrencia)
			.requerente(Requerente.builder().id(idRequerente).build())
			.funcionario(Funcionario.builder().id(idFuncionario).build())
			.build()), 
			orderBy, 
			direction
		));
	}
	
	@GetMapping("/{idOcorrencia}")
	public ResponseEntity<Ocorrencia> findCountryById(@PathVariable UUID idOcorrencia) {
		return super.ok(ocorrenciaService.findById(idOcorrencia));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<Ocorrencia>> paginations(
			@RequestParam int page, @RequestParam int size, 
			@RequestParam(required = false) UUID idFuncionario, @RequestParam(required = false) UUID idRequerente,
			@RequestParam(required = false) Status status, @RequestParam(required = false) LocalDateTime dataOcorrencia,  
			@RequestParam(defaultValue = "nome") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(ocorrenciaService.pagination(
			page, size, 
			Example.of(Ocorrencia.builder()
			.status(status)
			.dataOcorrencia(dataOcorrencia)
			.requerente(Requerente.builder().id(idRequerente).build())
			.funcionario(Funcionario.builder().id(idFuncionario).build())
			.build()), 
			orderBy, 
			direction
		));
	}
	
	@PostMapping("/{idFuncionario}/{idRequerente}")
	public ResponseEntity<Ocorrencia> create(
			@RequestParam(required = false) UUID idFuncionario, @RequestParam(required = false) UUID idRequerente,
			@RequestBody @Valid OcorrenciaDTO ocorrenciaDTO) {
		return super.ok(ocorrenciaService.create(
		idFuncionario, 
		idRequerente, 
		Ocorrencia.builder()
		.status(ocorrenciaDTO.getStatus())
		.descricao(ocorrenciaDTO.getDescricao())
		.dataOcorrencia(ocorrenciaDTO.getDataOcorrencia())
		.build(), 
		ocorrenciaDTO.getCrimes(), 
		ocorrenciaDTO.getSuspeitos()));
	}
	
	@PutMapping("/{idOcorrencia}/{idRequerente}")
	public ResponseEntity<Ocorrencia> update(
			@RequestParam(required = false) UUID idOcorrencia, @RequestParam(required = false) UUID idRequerente,
			@RequestBody @Valid OcorrenciaDTO ocorrenciaDTO) {
		return super.ok(ocorrenciaService.update(
		idOcorrencia, 
		idRequerente, 
		Ocorrencia.builder()
		.status(ocorrenciaDTO.getStatus())
		.descricao(ocorrenciaDTO.getDescricao())
		.dataOcorrencia(ocorrenciaDTO.getDataOcorrencia())
		.build(), 
		ocorrenciaDTO.getCrimes(), 
		ocorrenciaDTO.getSuspeitos()));
	}
	
	@Secured("Administrador")
	@DeleteMapping("/{idOcorrencia}")
	public ResponseEntity<Ocorrencia> delete(@PathVariable UUID idOcorrencia) {
		return super.ok(ocorrenciaService.delete(idOcorrencia));
	}
}
