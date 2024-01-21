package com.stationcontrol.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
			@RequestParam(required = false) UUID funcionario, @RequestParam(required = false) UUID requerente,
			@RequestParam(required = false) Status status, @RequestParam(defaultValue = "dataCriacao") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(ocorrenciaService.findAll(
			Example.of(Ocorrencia.builder()
			.status(status)
			.requerente(Requerente.builder().id(requerente).build())
			.funcionario(Funcionario.builder().id(funcionario).build())
			.build()), 
			orderBy, 
			direction
		));
	}
	
	@GetMapping("/{idOcorrencia}")
	public ResponseEntity<Ocorrencia> findById(@PathVariable UUID idOcorrencia) {
		return super.ok(ocorrenciaService.findById(idOcorrencia));
	}
	
	@GetMapping("/contador")
	public ResponseEntity<Long> count(
			@RequestParam(required = false) Status status,
			@RequestParam(required = false) UUID requerente,
			@RequestParam(required = false) UUID funcionario
			) {
		return super.ok(ocorrenciaService.count(Example.of(Ocorrencia.builder()
		.status(status)
		.requerente(Requerente.builder().id(requerente).build())
		.funcionario(Funcionario.builder().id(funcionario).build())
		.status(status).build())));
	}
	
	@GetMapping("/contador/dataCricao")
	public ResponseEntity<Long> countByCreationDateBetween(Optional<UUID> funcionario, @RequestParam LocalDateTime dataInicio, @RequestParam LocalDateTime dataFim) {
		return super.ok(ocorrenciaService.countByDataCriacaoBetween(funcionario, dataInicio, dataFim));
	}
	
	@GetMapping("/contador/dataOcorrencia")
	public ResponseEntity<Long> countByOccurrenceDateBetween(Optional<UUID> funcionario, @RequestParam LocalDateTime dataInicio, @RequestParam LocalDateTime dataFim) {
		return super.ok(ocorrenciaService.countByDataOcorrenciaBetween(funcionario, dataInicio, dataFim));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<Ocorrencia>> pagination(
			@RequestParam int page, @RequestParam int size, 
			@RequestParam(required = false) UUID funcionario, @RequestParam(required = false) UUID requerente,
			@RequestParam(required = false) Status status, 
			@RequestParam(defaultValue = "dataAtualizacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(ocorrenciaService.pagination(
			page, size, 
			Example.of(Ocorrencia.builder()
			.status(status)
			.requerente(Requerente.builder().id(requerente).build())
			.funcionario(Funcionario.builder().id(funcionario).build())
			.build()), 
			orderBy, 
			direction
		));
	}
	
	@GetMapping("/paginacao/dataCriacao")
	public ResponseEntity<Page<Ocorrencia>> paginationByCreateDateBetween(
			@RequestParam int page, @RequestParam int size, Optional<UUID> funcionario, 
			@RequestParam LocalDateTime dataInicio, @RequestParam LocalDateTime dataFim,
			@RequestParam(defaultValue = "dataCriacao") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(ocorrenciaService.paginationByDataCriacaoBetween(page, size, funcionario, dataInicio, dataFim, orderBy, direction));
	}
	
	@GetMapping("/paginacao/dataOcorrencia")
	public ResponseEntity<Page<Ocorrencia>> paginationByOccurrenceBetween(
			@RequestParam int page, @RequestParam int size, Optional<UUID> funcionario, 
			@RequestParam LocalDateTime dataInicio, @RequestParam LocalDateTime dataFim,
			@RequestParam(defaultValue = "dataCriacao") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(ocorrenciaService.paginationByDataOcorrenciaBetween(page, size, funcionario, dataInicio, dataFim, orderBy, direction));
	}
	
	@PostMapping("/{idFuncionario}/{idRequerente}")
	public ResponseEntity<Ocorrencia> create(@PathVariable UUID idFuncionario, @PathVariable UUID idRequerente, @RequestBody @Valid OcorrenciaDTO ocorrenciaDTO) {
		return super.ok(ocorrenciaService.create(
			idFuncionario, 
			idRequerente, 
			Ocorrencia.builder()
			.status(ocorrenciaDTO.getStatus())
			.descricao(ocorrenciaDTO.getDescricao())
			.dataOcorrencia(ocorrenciaDTO.getDataOcorrencia())
			.build()
		));
	}
	
	@PostMapping("/lista")
	public ResponseEntity<List<Ocorrencia>> create(@RequestBody @Valid List<OcorrenciaDTO> ocorrenciasDTO) {
		return super.ok(ocorrenciaService.create(ocorrenciasDTO.stream().map(ocorrenciaDTO -> Ocorrencia.builder()
		.status(ocorrenciaDTO.getStatus())
		.descricao(ocorrenciaDTO.getDescricao())
		.dataOcorrencia(ocorrenciaDTO.getDataOcorrencia())
		.build()).toList()));
	}
	
	@PutMapping("/{idOcorrencia}/{idRequerente}")
	public ResponseEntity<Ocorrencia> update(@PathVariable UUID idOcorrencia, @PathVariable UUID idRequerente, @RequestBody @Valid OcorrenciaDTO ocorrenciaDTO) {
		return super.ok(ocorrenciaService.update(
		idOcorrencia, 
		idRequerente, 
		Ocorrencia.builder()
		.status(ocorrenciaDTO.getStatus())
		.descricao(ocorrenciaDTO.getDescricao())
		.dataOcorrencia(ocorrenciaDTO.getDataOcorrencia())
		.build()));
	}
	
	@Secured("Administrador")
	@DeleteMapping("/{idOcorrencia}")
	public ResponseEntity<Ocorrencia> delete(@PathVariable UUID idOcorrencia) {
		return super.ok(ocorrenciaService.deleteById(idOcorrencia));
	}
}
