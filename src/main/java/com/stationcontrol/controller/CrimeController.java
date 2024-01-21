package com.stationcontrol.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stationcontrol.dto.CrimeDTO;
import com.stationcontrol.model.Crime;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.service.CrimeService;
import com.stationcontrol.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crimes")
public class CrimeController extends BaseController {
	private final CrimeService crimeService;

	@GetMapping
	public ResponseEntity<List<Crime>> findAll(@RequestParam(required = false) String descricao, @RequestParam(required = false) UUID funcionario, @RequestParam(defaultValue = "descricao") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(crimeService.findAll(Example.of(Crime.builder().descricao(descricao).funcionario(Funcionario.builder().id(funcionario).build()).build(), ExampleMatcher.matching().withMatcher("descricao", matcher -> matcher.contains().ignoreCase())), orderBy, direction));
	}
	
	@GetMapping("/{idCrime}")
	public ResponseEntity<Crime> findById(@PathVariable UUID idCrime) {
		return super.ok(crimeService.findById(idCrime));
	}
	
	@GetMapping("/ocorrencia/{idOcorrencia}")
	public ResponseEntity<List<Crime>> findAllByOcorrencia(@PathVariable UUID idOcorrencia) {
		return super.ok(crimeService.findAllByOcorrencia(idOcorrencia));
	}
	
	@GetMapping("/contador")
	public ResponseEntity<Long> count(@RequestParam(required = false) UUID funcionario, @RequestParam(required = false) Boolean vigor) {
		return super.ok(crimeService.count(Example.of(Crime.builder().vigor(vigor).funcionario(Funcionario.builder().id(funcionario).build()).build())));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<Crime>> pagination(
			@RequestParam int page, @RequestParam int size, 
			@RequestParam Optional<String> nome, @RequestParam Optional<UUID> ocorrencia, 
			@RequestParam(required = false) UUID funcionario, @RequestParam(required = false) Boolean vigor, 
			@RequestParam(defaultValue = "descricao") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(ocorrencia.isEmpty()
			? crimeService.pagination(page, size, Example.of(Crime.builder().nome(nome.orElse(null)).vigor(vigor).funcionario(Funcionario.builder().id(funcionario).build()).build(), ExampleMatcher.matching().withMatcher("nome", matcher -> matcher.contains().ignoreCase())), orderBy, direction)
			: crimeService.paginationByOccurrence(page, size, nome.orElse(""), ocorrencia.get(), orderBy, direction)
		);
	}
	
	@Secured("Administrador")
	@PostMapping("/{idFuncionario}")
	public ResponseEntity<Crime> create(@PathVariable UUID idFuncionario, @RequestBody @Valid CrimeDTO crimeDTO) {
		Crime crime = Crime.builder().build();
		BeanUtils.copyProperties(crimeDTO, crime);
		return super.created(crimeService.create(idFuncionario, crime));
	}
	
	@Secured("Administrador")
	@PostMapping("/{idFuncionario}/lista")
	public ResponseEntity<List<Crime>> create(@RequestBody @Valid List<CrimeDTO> crimesDTO) {
		return super.created(crimeService.save(crimesDTO.stream().map(crimeDTO -> {
			Crime crime = Crime.builder().build();
			BeanUtils.copyProperties(crimeDTO, crime);
			return crime;
		}).toList()));
	}
	
	@PostMapping("/ocorrencia/{idOcorrencia}/{idCrime}")
	public ResponseEntity<Crime> createByOcorrencia(@PathVariable UUID idOcorrencia, @PathVariable UUID idCrime) {
		return super.created(crimeService.createByOcorrencia(idOcorrencia, idCrime));
	}
	
	@PostMapping("/ocorrencia/lista/{idOcorrencia}")
	public ResponseEntity<List<Crime>> createByOcorrencia(@PathVariable UUID idOcorrencia, @RequestBody List<UUID> crimes) {
		return super.created(crimeService.createByOcorrencia(idOcorrencia, crimes));
	}
	
	@PutMapping("/{idCrime}")
	@Secured("Administrador")
	public ResponseEntity<Crime> update(@PathVariable UUID idCrime, @RequestBody @Valid Crime crime) {
		return super.ok(crimeService.update(idCrime, crime, "id", "funcionario"));
	}

	@Secured("Administrador")
	@DeleteMapping("/{idCrime}")
	public ResponseEntity<Crime> delete(@PathVariable UUID idCrime) {
		return super.ok(crimeService.deleteById(idCrime));
	}
	
	@DeleteMapping("/ocorrencia/{idOcorrencia}/{idCrime}")
	public ResponseEntity<Crime> deleteByOcorrencia(@PathVariable UUID idOcorrencia, @PathVariable UUID idCrime) {
		return super.ok(crimeService.deleteByOcorrencia(idOcorrencia, idCrime));
	}
}
