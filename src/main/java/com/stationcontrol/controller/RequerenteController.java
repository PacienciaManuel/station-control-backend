package com.stationcontrol.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stationcontrol.dto.RequerenteDTO;
import com.stationcontrol.dto.RequerenteUpdateDTO;
import com.stationcontrol.model.Requerente;
import com.stationcontrol.model.Telefone;
import com.stationcontrol.service.AbstractService;
import com.stationcontrol.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requerentes")
public class RequerenteController extends BaseController {
	private final AbstractService<Requerente, UUID> abstractService;
	
	@GetMapping
	public ResponseEntity<List<Requerente>> findAll(
			@RequestParam(required = false) String nome, @RequestParam(required = false) String bilheteIdentidade, 
			@RequestParam(defaultValue = "nome") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(abstractService.findAll(
				Example.of(Requerente.builder()
					.nome(nome)
					.bilheteIdentidade(bilheteIdentidade)
					.build(), 
				ExampleMatcher.matching()
				.withMatcher("nome", matcher -> matcher.contains().ignoreCase())
				.withMatcher("bilheteIdentidade", matcher -> matcher.contains().ignoreCase())
			), 
			orderBy, direction
		));
	}
	
	@GetMapping("/{idRequerente}")
	public ResponseEntity<Requerente> findCountryById(@PathVariable UUID idRequerente) {
		return super.ok(abstractService.findById(idRequerente));
	}
	
	@GetMapping("/contador")
	public ResponseEntity<Long> count(@RequestParam(required = false) String nome, @RequestParam(required = false) String bilheteIdentidade) {
		return super.ok(abstractService.count(Example.of(Requerente.builder()
		.nome(nome)
		.bilheteIdentidade(bilheteIdentidade)
		.build())));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<Requerente>> pagination(
			@RequestParam int page, @RequestParam int size, 
			@RequestParam(required = false) String nome, @RequestParam(required = false) String bilheteIdentidade, 
			@RequestParam(defaultValue = "nome") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(abstractService.pagination(
			page, size, 
			Example.of(
				Requerente.builder()
				.nome(nome)
				.bilheteIdentidade(bilheteIdentidade)
				.build(), 
				ExampleMatcher.matching()
				.withMatcher("nome", matcher -> matcher.contains().ignoreCase())
				.withMatcher("bilheteIdentidade", matcher -> matcher.contains().ignoreCase())
			), 
			orderBy, direction
		));
	}
	
	@PostMapping
	public ResponseEntity<Requerente> create(@RequestBody @Valid RequerenteDTO requerenteDTO) {
		return super.created(abstractService.save(Requerente.builder()
		.nome(requerenteDTO.getNome())
		.genero(requerenteDTO.getGenero())
		.morada(requerenteDTO.getMorada())
		.dataNascimento(requerenteDTO.getDataNascimento())
		.bilheteIdentidade(requerenteDTO.getBilheteIdentidade())
		.telefone(Telefone.builder().numero(requerenteDTO.getNumero()).build())
		.build()));
	}
	
	@PostMapping("/lista")
	public ResponseEntity<List<Requerente>> create(@RequestBody @Valid List<RequerenteDTO> requerentesDTO) {
		return super.ok(abstractService.save(requerentesDTO.stream().map(requerenteDTO -> Requerente.builder()
		.nome(requerenteDTO.getNome())
		.genero(requerenteDTO.getGenero())
		.morada(requerenteDTO.getMorada())
		.dataNascimento(requerenteDTO.getDataNascimento())
		.bilheteIdentidade(requerenteDTO.getBilheteIdentidade())
		.telefone(Telefone.builder().numero(requerenteDTO.getNumero()).build())
		.build()
		).toList()));
	}
	
	@PutMapping("/{idRequerente}")
	public ResponseEntity<Requerente> update(@PathVariable UUID idRequerente, @RequestBody @Valid RequerenteUpdateDTO requerenteUpdateDTO) {
		Requerente requerente = Requerente.builder().build();
		BeanUtils.copyProperties(requerenteUpdateDTO, requerente);
		return super.ok(abstractService.update(idRequerente, requerente, "id", "dataCriacao", "dataAtualzacao", "telefone", "ocorrencias"));
	}
	
	@Secured("Administrador")
	@DeleteMapping("/{idRequerente}")
	public ResponseEntity<Requerente> delete(@PathVariable UUID idRequerente) {
		return super.ok(abstractService.delete(idRequerente));
	}
}
