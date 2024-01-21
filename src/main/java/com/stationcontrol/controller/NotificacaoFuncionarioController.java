package com.stationcontrol.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stationcontrol.dto.NotificacaoDTO;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.model.NotificacaoFuncionario;
import com.stationcontrol.service.NotificacaoFuncionariosService;
import com.stationcontrol.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notificacoes/funcionarios")
public class NotificacaoFuncionarioController extends BaseController {
	private final NotificacaoFuncionariosService notificacaoFuncionariosService;
	
	@GetMapping
	public ResponseEntity<List<NotificacaoFuncionario>> findAll(
			@RequestParam(required = false) UUID funcionarioOrigem, @RequestParam(required = false) UUID funcionarioDestino, 
			@RequestParam(required = false) Boolean visto,
			@RequestParam(defaultValue = "dataNotificacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		NotificacaoFuncionario notificacao = NotificacaoFuncionario.builder().funcionarioDestino(Funcionario.builder().id(funcionarioDestino).build()).build();
		notificacao.setVisto(visto);
		notificacao.setFuncionario(Funcionario.builder().id(funcionarioOrigem).build());
		return super.ok(notificacaoFuncionariosService.findAll(Example.of(notificacao), orderBy, direction));
	}
	
	@GetMapping("/{idNotificacao}")
	public ResponseEntity<NotificacaoFuncionario> findById(@PathVariable UUID idNotificacao) {
		return super.ok(notificacaoFuncionariosService.deleteById(idNotificacao));
	}
	
	@GetMapping("/contador")
	public ResponseEntity<Long> count(@RequestParam(required = false) UUID funcionarioOrigem, @RequestParam(required = false) UUID funcionarioDestino, @RequestParam(required = false) Boolean visto) {
		NotificacaoFuncionario notificacao = NotificacaoFuncionario.builder().funcionarioDestino(Funcionario.builder().id(funcionarioDestino).build()).build();
		notificacao.setVisto(visto);
		notificacao.setFuncionario(Funcionario.builder().id(funcionarioOrigem).build());
		return super.ok(notificacaoFuncionariosService.count(Example.of(notificacao)));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<NotificacaoFuncionario>> pagination(
			@RequestParam int page, @RequestParam int size, 
			@RequestParam(required = false) UUID funcionarioOrigem, @RequestParam(required = false) UUID funcionarioDestino, 
			@RequestParam(required = false) Boolean visto,
			@RequestParam(defaultValue = "dataNotificacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		NotificacaoFuncionario notificacao = NotificacaoFuncionario.builder().funcionarioDestino(Funcionario.builder().id(funcionarioDestino).build()).build();
		notificacao.setVisto(visto);
		notificacao.setFuncionario(Funcionario.builder().id(funcionarioOrigem).build());
		return super.ok(notificacaoFuncionariosService.pagination(page, size, Example.of(notificacao), orderBy, direction));
	}
	
	@PostMapping("/{idFuncionario}/{idFuncionarioDestino}")
	public ResponseEntity<NotificacaoFuncionario> create(@PathVariable UUID idFuncionario, @PathVariable UUID idFuncionarioDestino, @RequestBody @Valid NotificacaoDTO notificacaoDTO) {
		NotificacaoFuncionario notificacao = NotificacaoFuncionario.builder().build();
		BeanUtils.copyProperties(notificacaoDTO, notificacao);
		return super.ok(notificacaoFuncionariosService.create(idFuncionario, idFuncionarioDestino, notificacao));
	}
	
	@PostMapping("/lista")
	public ResponseEntity<List<NotificacaoFuncionario>> create(@RequestBody @Valid List<NotificacaoDTO> notificacoesDTO) {
		return super.ok(notificacaoFuncionariosService.create(notificacoesDTO.stream().map(notificacaoDTO -> {
			NotificacaoFuncionario notificacao = NotificacaoFuncionario.builder().build();
			BeanUtils.copyProperties(notificacaoDTO, notificacao);
			return notificacao;
		}).toList()));
	}
	
	@PutMapping("/{idNotificacao}")
	public ResponseEntity<NotificacaoFuncionario> update(@PathVariable UUID idNotificacao, @RequestBody @Valid NotificacaoDTO notificacaoDTO) {
		NotificacaoFuncionario notificacao = NotificacaoFuncionario.builder().build();
		BeanUtils.copyProperties(notificacaoDTO, notificacao);
		return super.ok(notificacaoFuncionariosService.update(idNotificacao, notificacao));
	}
	
	@DeleteMapping("/{idNotificacao}")
	public ResponseEntity<NotificacaoFuncionario> delete(@PathVariable UUID idNotificacao) {
		return super.ok(notificacaoFuncionariosService.deleteById(idNotificacao));
	}
}
