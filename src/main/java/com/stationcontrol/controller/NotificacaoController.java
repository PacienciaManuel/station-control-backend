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
import com.stationcontrol.model.NotificacaoRequerente;
import com.stationcontrol.model.Requerente;
import com.stationcontrol.service.NotificacaoFuncionariosService;
import com.stationcontrol.service.NotificacaoRequerenteService;
import com.stationcontrol.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notificacoes")
public class NotificacaoController extends BaseController {
	private final NotificacaoRequerenteService notificacaoRequerenteService;
	private final NotificacaoFuncionariosService notificacaoFuncionariosService;
	
	// Notificaçoes Funcionários
	@GetMapping("/funcionarios")
	public ResponseEntity<List<NotificacaoFuncionario>> findAllForEmployee(
			@RequestParam(required = false) UUID funcionarioOrigem, @RequestParam(required = false) UUID funcionarioDestino, 
			@RequestParam(required = false) Boolean visto,
			@RequestParam(defaultValue = "dataNotificacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		NotificacaoFuncionario notificacao = NotificacaoFuncionario.builder().funcionarioDestino(Funcionario.builder().id(funcionarioDestino).build()).build();
		notificacao.setVisto(visto);
		notificacao.setFuncionario(Funcionario.builder().id(funcionarioOrigem).build());
		return super.ok(notificacaoFuncionariosService.findAll(Example.of(notificacao), orderBy, direction));
	}
	
	@GetMapping("/funcionarios/{idNotificacao}")
	public ResponseEntity<NotificacaoFuncionario> findByIdForEmployee(@PathVariable UUID idNotificacao) {
		return super.ok(notificacaoFuncionariosService.delete(idNotificacao));
	}
	
	@GetMapping("/funcionarios/paginacao")
	public ResponseEntity<Page<NotificacaoFuncionario>> paginationForEmployee(
			@RequestParam int page, @RequestParam int size, 
			@RequestParam(required = false) UUID funcionarioOrigem, @RequestParam(required = false) UUID funcionarioDestino, 
			@RequestParam(required = false) Boolean visto,
			@RequestParam(defaultValue = "dataNotificacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		NotificacaoFuncionario notificacao = NotificacaoFuncionario.builder().funcionarioDestino(Funcionario.builder().id(funcionarioDestino).build()).build();
		notificacao.setVisto(visto);
		notificacao.setFuncionario(Funcionario.builder().id(funcionarioOrigem).build());
		return super.ok(notificacaoFuncionariosService.pagination(page, size, Example.of(notificacao), orderBy, direction));
	}
	
	@PostMapping("/funcionarios/{idFuncionario}/{idFuncionarioDestino}")
	public ResponseEntity<NotificacaoFuncionario> createForEmployee(@PathVariable UUID idFuncionario, @PathVariable UUID idFuncionarioDestino, @RequestBody @Valid NotificacaoDTO notificacaoDTO) {
		NotificacaoFuncionario notificacao = NotificacaoFuncionario.builder().build();
		BeanUtils.copyProperties(notificacaoDTO, notificacao);
		return super.ok(notificacaoFuncionariosService.create(idFuncionario, idFuncionarioDestino, notificacao));
	}
	
	@PutMapping("/funcionarios/{idNotificacao}")
	public ResponseEntity<NotificacaoFuncionario> deleteForEmployee(@PathVariable UUID idNotificacao, @RequestBody @Valid NotificacaoDTO notificacaoDTO) {
		NotificacaoFuncionario notificacao = NotificacaoFuncionario.builder().build();
		BeanUtils.copyProperties(notificacaoDTO, notificacao);
		return super.ok(notificacaoFuncionariosService.update(idNotificacao, notificacao));
	}
	
	@DeleteMapping("/funcionarios/{idNotificacao}")
	public ResponseEntity<NotificacaoFuncionario> deleteyForEmployee(@PathVariable UUID idNotificacao) {
		return super.ok(notificacaoFuncionariosService.delete(idNotificacao));
	}
	
	// Notificaçoes Requerentes
	@GetMapping("/requerentes")
	public ResponseEntity<List<NotificacaoRequerente>> findAllForApplicant(
			@RequestParam(required = false) UUID funcionario, @RequestParam(required = false) UUID requerente, 
			@RequestParam(required = false) Boolean recebido,
			@RequestParam(defaultValue = "dataNotificacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		NotificacaoRequerente notificacao = NotificacaoRequerente.builder().requerente(Requerente.builder().id(requerente).build()).build();
		notificacao.setRecebido(recebido);
		notificacao.setFuncionario(Funcionario.builder().id(funcionario).build());
		return super.ok(notificacaoRequerenteService.findAll(Example.of(notificacao), orderBy, direction));
	}
	
	@GetMapping("/requerentes/{idNotificacao}")
	public ResponseEntity<NotificacaoRequerente> findByIdForApplicant(@PathVariable UUID idNotificacao) {
		return super.ok(notificacaoRequerenteService.delete(idNotificacao));
	}
	
	@GetMapping("/requerentes/paginacao")
	public ResponseEntity<Page<NotificacaoRequerente>> paginationForApplicant(
			@RequestParam int page, @RequestParam int size, 
			@RequestParam(required = false) UUID funcionario, @RequestParam(required = false) UUID requerente, 
			@RequestParam(required = false) Boolean recebido,
			@RequestParam(defaultValue = "dataNotificacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		NotificacaoRequerente notificacao = NotificacaoRequerente.builder().requerente(Requerente.builder().id(requerente).build()).build();
		notificacao.setRecebido(recebido);
		notificacao.setFuncionario(Funcionario.builder().id(funcionario).build());
		return super.ok(notificacaoRequerenteService.pagination(page, size, Example.of(notificacao), orderBy, direction));
	}
	
	@PostMapping("/requerentes/{idFuncionario}/{idRequerente}")
	public ResponseEntity<NotificacaoRequerente> createForApplicant(@PathVariable UUID idFuncionario, @PathVariable UUID idRequerente, @RequestBody @Valid NotificacaoDTO notificacaoDTO) {
		NotificacaoRequerente notificacao = NotificacaoRequerente.builder().build();
		BeanUtils.copyProperties(notificacaoDTO, notificacao);
		return super.ok(notificacaoRequerenteService.create(idFuncionario, idRequerente, notificacao));
	}
	
	@PutMapping("/requerentes/{idNotificacao}")
	public ResponseEntity<NotificacaoRequerente> deleteForApplicant(@PathVariable UUID idNotificacao, @RequestBody @Valid NotificacaoDTO notificacaoDTO) {
		NotificacaoRequerente notificacao = NotificacaoRequerente.builder().build();
		BeanUtils.copyProperties(notificacaoDTO, notificacao);
		return super.ok(notificacaoRequerenteService.update(idNotificacao, notificacao));
	}
	
	@DeleteMapping("/requerentes/{idNotificacao}")
	public ResponseEntity<NotificacaoRequerente> deleteForApplicant(@PathVariable UUID idNotificacao) {
		return super.ok(notificacaoRequerenteService.delete(idNotificacao));
	}
}
