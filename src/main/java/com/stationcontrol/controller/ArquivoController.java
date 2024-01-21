package com.stationcontrol.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stationcontrol.model.Arquivo;
import com.stationcontrol.model.Ocorrencia;
import com.stationcontrol.service.ArquivoService;
import com.stationcontrol.util.BaseController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/arquivos")
public class ArquivoController extends BaseController {
	private final ArquivoService arquivoService;
	
	@GetMapping
	public ResponseEntity<List<Arquivo>> findAll(
			@RequestParam(required = false) UUID ocorrencia, 
			@RequestParam(defaultValue = "dataArquivo") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(arquivoService.findAll(Example.of(Arquivo.builder()
		.ocorrencia(Ocorrencia.builder().id(ocorrencia).build())
		.build()),
		orderBy, direction));
	}
	
	@GetMapping("/contador")
	public ResponseEntity<Long> count(@RequestParam(required = false) UUID ocorrencia) {
		return super.ok(arquivoService.count(Example.of(Arquivo.builder()
		.ocorrencia(Ocorrencia.builder().id(ocorrencia).build())
		.build())));
	}

	@GetMapping("/paginacao")
	public ResponseEntity<Page<Arquivo>> pagination(
			@RequestParam int page, @RequestParam int size, @PathVariable(required = false) UUID ocorrencia, 
			@RequestParam(defaultValue = "dataArquivo") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(arquivoService.pagination(
			page, size, 
			Example.of(Arquivo.builder()
			.ocorrencia(Ocorrencia.builder().id(ocorrencia).build())
			.build()),
			orderBy, direction
		));
	}

	@PostMapping("/{idOcorrencia}")
	public ResponseEntity<Arquivo> create(@PathVariable UUID idOcorrencia, @RequestParam MultipartFile arquivo) {
		return super.ok(arquivoService.create(idOcorrencia, arquivo));
	}

	@PostMapping("/lista/{idOcorrencia}")
	public ResponseEntity<List<Arquivo>> create(@PathVariable UUID idOcorrencia, @RequestParam MultipartFile[] arquivos) {
		return super.ok(arquivoService.create(idOcorrencia, arquivos));
	}
	
	@DeleteMapping("/{idArquivo}")
	public ResponseEntity<Arquivo> delete(@PathVariable UUID idArquivo) {
		return super.ok(arquivoService.deleteById(idArquivo));
	}
}
