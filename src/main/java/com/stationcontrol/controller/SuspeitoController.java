package com.stationcontrol.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stationcontrol.dto.SuspeitoDTO;
import com.stationcontrol.dto.SuspeitoUpdateDTO;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.model.Genero;
import com.stationcontrol.model.Pais;
import com.stationcontrol.model.Suspeito;
import com.stationcontrol.model.Telefone;
import com.stationcontrol.service.SuspeitoService;
import com.stationcontrol.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suspeitos")
public class SuspeitoController extends BaseController {
	private final SuspeitoService suspeitoService;
	
	@GetMapping
	public ResponseEntity<List<Suspeito>> findAll(
			@RequestParam(required = false) UUID pais,
			@RequestParam(required = false) String nome, 
			@RequestParam(required = false) Genero genero, 
			@RequestParam(required = false) Boolean detido, 
			@RequestParam(required = false) UUID funcionario,
			@RequestParam(required = false) LocalDate dataNascimento, 
			@RequestParam(defaultValue = "nome") String orderBy, 
			@RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(suspeitoService.findAll(
			Example.of(
				Suspeito.builder()
				.nome(nome)
				.genero(genero)
				.detido(detido)
				.dataNascimento(dataNascimento)
				.pais(Pais.builder().id(pais).build())
				.funcionario(Funcionario.builder().id(funcionario).build())
				.build(),
				ExampleMatcher.matching()
				.withMatcher("nome", matcher -> matcher.contains().ignoreCase())
			), 
			orderBy, direction
		));
	}

	@GetMapping("/{idSuspeito}")
	public ResponseEntity<Suspeito> findById(@PathVariable UUID idSuspeito) {
		return super.ok(suspeitoService.findById(idSuspeito));
	}
	
	@GetMapping("/contador")
	public ResponseEntity<Long> count(
			@RequestParam(required = false) UUID pais,
			@RequestParam(required = false) Genero genero,
			@RequestParam(required = false) UUID funcionario,
			@RequestParam(required = false) Boolean detido) {
		return super.ok(suspeitoService.count(Example.of(
		Suspeito.builder()
		.detido(detido)
		.genero(genero)
		.pais(Pais.builder().id(pais).build())
		.funcionario(Funcionario.builder().id(funcionario).build())
		.build())));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<Suspeito>> pagination(
			@RequestParam int page, @RequestParam int size,
			@RequestParam(required = false) UUID pais,
			@RequestParam(required = false) String nome, 
			@RequestParam(required = false) Genero genero, 
			@RequestParam(required = false) Boolean detido, 
			@RequestParam(required = false) UUID funcionario,
			@RequestParam(required = false) LocalDate dataNascimento, 
			@RequestParam(defaultValue = "nome") String orderBy, 
			@RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(suspeitoService.pagination(
				page, size,
			Example.of(
				Suspeito.builder()
				.nome(nome)
				.genero(genero)
				.detido(detido)
				.dataNascimento(dataNascimento)
				.pais(Pais.builder().id(pais).build())
				.funcionario(Funcionario.builder().id(funcionario).build())
				.build(),
				ExampleMatcher.matching()
				.withMatcher("nome", matcher -> matcher.contains().ignoreCase())
			), 
			orderBy, direction
		));
	}
	
	@PostMapping("/{idFuncionario}/{idPais}")
	public ResponseEntity<Suspeito> create(@PathVariable UUID idFuncionario, @PathVariable UUID idPais, @Valid SuspeitoDTO suspeitoDTO, @RequestParam Optional<MultipartFile> foto) {
		Suspeito suspeito = Suspeito.builder().build();
		BeanUtils.copyProperties(suspeitoDTO, suspeito);
		if (suspeitoDTO.getNumero() != null) {
			suspeito.setTelefone(Telefone.builder().numero(suspeitoDTO.getNumero()).build());
		}
		return super.created(suspeitoService.create(idFuncionario, idPais, suspeito, foto));
	}
	
	@PostMapping("/lista")
	public ResponseEntity<List<Suspeito>> create(@RequestBody @Valid List<SuspeitoDTO> suspeitosDTO) {
		return super.created(suspeitoService.create(suspeitosDTO.stream().map(suspeitoDTO -> {
			Suspeito suspeito = Suspeito.builder().build();
			BeanUtils.copyProperties(suspeitoDTO, suspeito);
			return suspeito;
		}).toList()));
	}
	
	@PutMapping("/{idSuspeito}/{idPais}")
	public ResponseEntity<Suspeito>  update(@PathVariable UUID idSuspeito, @PathVariable UUID idPais, @RequestBody @Valid SuspeitoUpdateDTO suspeitoUpdateDTO) {
		Suspeito suspeito = Suspeito.builder().build();
		BeanUtils.copyProperties(suspeitoUpdateDTO, suspeito);
		return super.ok(suspeitoService.update(idSuspeito, idPais, suspeito));
	}
	
	@PatchMapping("/foto/{idSuspeito}")
	public ResponseEntity<Suspeito> updatePhoto(@PathVariable UUID idSuspeito, @RequestParam MultipartFile foto) {
		return super.ok(suspeitoService.updatePhoto(idSuspeito, foto));
	}

	@Secured("Administrador")
	@DeleteMapping("/{idSuspeito}")
	public ResponseEntity<Suspeito>  delete(@PathVariable UUID idSuspeito) {
		return super.ok(suspeitoService.deleteById(idSuspeito));
	}
}
