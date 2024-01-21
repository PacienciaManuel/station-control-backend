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

import com.stationcontrol.dto.RequerenteDTO;
import com.stationcontrol.dto.RequerenteUpdateDTO;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.model.Genero;
import com.stationcontrol.model.Pais;
import com.stationcontrol.model.Requerente;
import com.stationcontrol.model.Telefone;
import com.stationcontrol.service.RequerenteService;
import com.stationcontrol.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requerentes")
public class RequerenteController extends BaseController {
	private final RequerenteService requerenteService;
	
	@GetMapping
	public ResponseEntity<List<Requerente>> findAll(
			@RequestParam(required = false) UUID funcionario,
			@RequestParam(required = false) String nome, @RequestParam(required = false) String bilheteIdentidade, 
			@RequestParam(required = false) Genero genero, @RequestParam(required = false) LocalDate dataNascimento, 
			@RequestParam(required = false) String morada, @RequestParam(required = false) UUID pais, 
			@RequestParam(defaultValue = "nome") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(requerenteService.findAll(
				Example.of(Requerente.builder()
				.nome(nome)
				.genero(genero)
				.morada(morada)
				.bilheteIdentidade(bilheteIdentidade)
				.pais(Pais.builder().id(pais).build())
				.funcionario(Funcionario.builder().id(funcionario).build())
				.build(),
				ExampleMatcher.matching()
				.withMatcher("nome", matcher -> matcher.contains().ignoreCase())
				.withMatcher("morada", matcher -> matcher.contains().ignoreCase())
				.withMatcher("bilheteIdentidade", matcher -> matcher.contains().ignoreCase())
			), 
			orderBy, direction
		));
	}
	
	@GetMapping("/{idRequerente}")
	public ResponseEntity<Requerente> findCountryById(@PathVariable UUID idRequerente) {
		return super.ok(requerenteService.findById(idRequerente));
	}
	
	@GetMapping("/contador")
	public ResponseEntity<Long> count(
			@RequestParam(required = false) Genero genero, 
			@RequestParam(required = false) String morada, 
			@RequestParam(required = false) UUID funcionario,
			@RequestParam(required = false) UUID pais) {
		return super.ok(requerenteService.count(Example.of(Requerente.builder()
			.genero(genero)
			.morada(morada)
			.pais(Pais.builder().id(pais).build())
			.funcionario(Funcionario.builder().id(funcionario).build())
			.build(), 
			ExampleMatcher.matching()
			.withMatcher("morada", matcher -> matcher.contains().ignoreCase())
		)));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<Requerente>> pagination(
			@RequestParam int page, @RequestParam int size, 
			@RequestParam(required = false) UUID funcionario,
			@RequestParam(required = false) String nome, @RequestParam(required = false) String bilheteIdentidade, 
			@RequestParam(required = false) Genero genero, @RequestParam(required = false) LocalDate dataNascimento, 
			@RequestParam(required = false) String morada, @RequestParam(required = false) UUID pais, 
			@RequestParam(defaultValue = "nome") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(requerenteService.pagination(
			page, size, 
			Example.of(Requerente.builder()
			.nome(nome)
			.genero(genero)
			.morada(morada)
			.bilheteIdentidade(bilheteIdentidade)
			.pais(Pais.builder().id(pais).build())
			.funcionario(Funcionario.builder().id(funcionario).build())
			.build(),
			ExampleMatcher.matching()
			.withMatcher("nome", matcher -> matcher.contains().ignoreCase())
			.withMatcher("morada", matcher -> matcher.contains().ignoreCase())
			.withMatcher("bilheteIdentidade", matcher -> matcher.contains().ignoreCase())
			), 
			orderBy, direction
		));
	}
	
	@PostMapping("/{idFuncionario}/{idPais}")
	public ResponseEntity<Requerente> create(@PathVariable UUID idFuncionario, @PathVariable UUID idPais, @Valid RequerenteDTO requerenteDTO, @RequestParam Optional<MultipartFile> fotoPerfil) {
		return super.created(requerenteService.create(
		idFuncionario, idPais,
		Requerente.builder()
		.nome(requerenteDTO.getNome())
		.genero(requerenteDTO.getGenero())
		.morada(requerenteDTO.getMorada())
		.dataNascimento(requerenteDTO.getDataNascimento())
		.bilheteIdentidade(requerenteDTO.getBilheteIdentidade())
		.telefone(Telefone.builder().numero(requerenteDTO.getNumero()).build())
		.build(), fotoPerfil));
	}
	
	@PostMapping("/lista")
	public ResponseEntity<List<Requerente>> create(@RequestBody @Valid List<RequerenteDTO> requerentesDTO) {
		return super.ok(requerenteService.save(requerentesDTO.stream().map(requerenteDTO -> Requerente.builder()
		.nome(requerenteDTO.getNome())
		.genero(requerenteDTO.getGenero())
		.morada(requerenteDTO.getMorada())
		.dataNascimento(requerenteDTO.getDataNascimento())
		.bilheteIdentidade(requerenteDTO.getBilheteIdentidade())
		.telefone(Telefone.builder().numero(requerenteDTO.getNumero()).build())
		.build()
		).toList()));
	}
	
	@PutMapping("/{idRequerente}/{idPais}")
	public ResponseEntity<Requerente> update(@PathVariable UUID idRequerente, @PathVariable UUID idPais, @RequestBody @Valid RequerenteUpdateDTO requerenteUpdateDTO) {
		Requerente requerente = Requerente.builder().build();
		BeanUtils.copyProperties(requerenteUpdateDTO, requerente);
		return super.ok(requerenteService.update(idRequerente, idPais, requerente));
	}
	
	@PatchMapping("/fotoPerfil/{idRequerente}")
	public ResponseEntity<Requerente> updatePhoto(@PathVariable UUID idRequerente, @RequestParam MultipartFile fotoPerfil) {
		return super.ok(requerenteService.updateProfilePhoto(idRequerente, fotoPerfil));
	}
	
	@Secured("Administrador")
	@DeleteMapping("/{idRequerente}")
	public ResponseEntity<Requerente> delete(@PathVariable UUID idRequerente) {
		return super.ok(requerenteService.deleteById(idRequerente));
	}
}
