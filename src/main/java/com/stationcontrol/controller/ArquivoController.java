package com.stationcontrol.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Example;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/arquivo")
public class ArquivoController extends BaseController {
	private final ArquivoService arquivoService;
	
	@GetMapping
	public ResponseEntity<List<Arquivo>> findAllByOcorrencia(@PathVariable UUID idOcorrencia, @RequestParam(defaultValue = "nome") String orderBy, @RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(arquivoService.findAll(Example.of(Arquivo.builder()
		.ocorrencia(Ocorrencia.builder().id(idOcorrencia).build())
		.build()),
		orderBy, direction));
	}
	
	@PostMapping("/{idOcorrencia}")
	public ResponseEntity<List<Arquivo>> create(@PathVariable UUID idOcorrencia, @RequestParam @Size(min = 1, max = 25, message = "{upload.size}") @Valid MultipartFile[] arquivos) {
		return super.created(arquivoService.create(idOcorrencia, arquivos));
	}
	
	@DeleteMapping("/{idArquivo}")
	public ResponseEntity<Arquivo> delete(@PathVariable UUID idArquivo) {
		return super.created(arquivoService.delete(idArquivo));
	}
}
