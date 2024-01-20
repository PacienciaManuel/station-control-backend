package com.stationcontrol.controller;

import java.util.List;
import java.util.UUID;

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

import com.stationcontrol.model.Crime;
import com.stationcontrol.service.AbstractService;
import com.stationcontrol.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crimes")
public class CrimeController extends BaseController {
	private final AbstractService<Crime, UUID> abstractService;

	@GetMapping
	public ResponseEntity<List<Crime>> findAll(@RequestParam(required = false) String descricao, @RequestParam(defaultValue = "descricao") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(abstractService.findAll(Example.of(Crime.builder().descricao(descricao).build(), ExampleMatcher.matching().withMatcher("descricao", matcher -> matcher.contains().ignoreCase())), orderBy, direction));
	}
	
	@GetMapping("/{idCrime}")
	public ResponseEntity<Crime> findById(@PathVariable UUID idCrime) {
		return super.ok(abstractService.findById(idCrime));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<Crime>> pagination(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) String descricao, @RequestParam(defaultValue = "descricao") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(abstractService.pagination(page, size, Example.of(Crime.builder().descricao(descricao).build(), ExampleMatcher.matching().withMatcher("descricao", matcher -> matcher.contains().ignoreCase())), orderBy, direction));
	}
	
	@PostMapping
	public ResponseEntity<Crime> create(@RequestBody @Valid Crime crime) {
		return super.created(abstractService.save(crime));
	}
	
	@PostMapping("/lista")
	public ResponseEntity<List<Crime>> create(@RequestBody @Valid List<Crime> crimes) {
		return super.ok(abstractService.save(crimes));
	}
	
	@PutMapping("/{idCrime}")
	public ResponseEntity<Crime> update(@PathVariable UUID idCrime, @RequestBody @Valid Crime crime) {
		return super.ok(abstractService.update(idCrime, crime, "id"));
	}

	@Secured("Administrador")
	@DeleteMapping("/{idCrime}")
	public ResponseEntity<Crime> delete(@PathVariable UUID idCrime) {
		return super.ok(abstractService.delete(idCrime));
	}
}
