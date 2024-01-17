package com.stationcontrol.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stationcontrol.model.Pais;
import com.stationcontrol.service.AbstractService;
import com.stationcontrol.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/paises")
public class PaisController extends BaseController<Pais> {
	private final AbstractService<Pais, UUID> abstractService;

	@GetMapping
	public ResponseEntity<List<Pais>> findAll(@RequestParam(required = false) String nome, @RequestParam(defaultValue = "nome") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(abstractService.findAll(Example.of(Pais.builder().nome(nome).build(), ExampleMatcher.matching().withMatcher("nome", matcher -> matcher.contains().ignoreCase())), orderBy, direction));
	}
	
	@GetMapping("/{idPais}")
	public ResponseEntity<Pais> findCountryById(@PathVariable UUID idPais) {
		return super.ok(abstractService.findById(idPais));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<Pais>> pagination(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) String nome, @RequestParam(defaultValue = "nome") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(abstractService.pagination(page, size, Example.of(Pais.builder().nome(nome).build(), ExampleMatcher.matching().withMatcher("nome", matcher -> matcher.contains().ignoreCase())), orderBy, direction));
	}
	
	@PostMapping
	public ResponseEntity<Pais> create(@RequestBody @Valid Pais pais) {
		return super.created(abstractService.save(pais));
	}
	
	@PostMapping("/lista")
	public ResponseEntity<List<Pais>> create(@RequestBody @Valid List<Pais> paises) {
		return super.ok(abstractService.save(paises));
	}
	
	@PutMapping("/{idPais}")
	public ResponseEntity<Pais> update(@PathVariable UUID idPais, @RequestBody @Valid Pais pai) {
		return super.ok(abstractService.update(idPais, pai, "id"));
	}
	
	@DeleteMapping("/{idPais}")
	public ResponseEntity<Pais> delete(@PathVariable UUID idPais) {
		return super.ok(abstractService.delete(idPais));
	}
}
