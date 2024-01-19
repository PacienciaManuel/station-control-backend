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

import com.stationcontrol.dto.FuncionarioDTO;
import com.stationcontrol.dto.FuncionarioUpdateDTO;
import com.stationcontrol.dto.UpdateEmailDTO;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.model.Genero;
import com.stationcontrol.model.Pais;
import com.stationcontrol.model.Papel;
import com.stationcontrol.model.Telefone;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/funcionarios")
public class FuncionarioController extends BaseController {
	private final FuncionarioService funcionarioService;
	
	@GetMapping
	@Secured("Administrador")
	public ResponseEntity<List<Funcionario>> findAll(
			@RequestParam(required = false) UUID pais,
			@RequestParam(required = false) String nome, 
			@RequestParam(required = false) Papel papel, 
			@RequestParam(required = false) String email, 
			@RequestParam(required = false) Genero genero, 
			@RequestParam(required = false) LocalDate dataNascimento, 
			@RequestParam(defaultValue = "nome") String orderBy, 
			@RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(funcionarioService.findAll(
			Example.of(
				Funcionario.builder()
				.nome(nome)
				.email(email)
				.genero(genero)
				.papel(papel)
				.dataNascimento(dataNascimento)
				.pais(Pais.builder().id(pais).build())
				.build(),
				ExampleMatcher.matching()
				.withMatcher("nome", matcher -> matcher.contains().ignoreCase())
				.withMatcher("email", matcher -> matcher.contains().ignoreCase())
			), 
			orderBy, direction
		));
	}

	@GetMapping("/{idFuncionario}")
	public ResponseEntity<Funcionario> findById(@PathVariable UUID idFuncionario) {
		return super.ok(funcionarioService.findById(idFuncionario));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<Funcionario>> pagination(
			@RequestParam int page, @RequestParam int size,
			@RequestParam(required = false) UUID pais,
			@RequestParam(required = false) String nome, 
			@RequestParam(required = false) Papel papel, 
			@RequestParam(required = false) String email, 
			@RequestParam(required = false) Genero genero, 
			@RequestParam(required = false) LocalDate dataNascimento, 
			@RequestParam(defaultValue = "nome") String orderBy, 
			@RequestParam(defaultValue = "ASC") Direction direction) {
		return super.ok(funcionarioService.pagination(
				page, size,
			Example.of(
				Funcionario.builder()
				.nome(nome)
				.email(email)
				.genero(genero)
				.papel(papel)
				.dataNascimento(dataNascimento)
				.pais(Pais.builder().id(pais).build())
				.build(),
				ExampleMatcher.matching()
				.withMatcher("nome", matcher -> matcher.contains().ignoreCase())
				.withMatcher("email", matcher -> matcher.contains().ignoreCase())
			), 
			orderBy, direction
		));
	}
	
	@GetMapping("/count")
	public ResponseEntity<Long> count(@RequestParam Papel papel) {
		return super.ok(funcionarioService.count(Example.of(Funcionario.builder().papel(papel).build())));
	}
	
	@PostMapping("/{idPais}")
	@Secured("Administrador")
	public ResponseEntity<Funcionario> create(@PathVariable UUID idPais, @Valid FuncionarioDTO funcionarioDTO, @RequestParam Optional<MultipartFile> fotoPerfil) {
		var funcionario = Funcionario.builder().build();
		BeanUtils.copyProperties(funcionarioDTO, funcionario);
		return super.created(funcionarioService.create(idPais, funcionario, Telefone.builder().numero(funcionarioDTO.getNumero()).build(), fotoPerfil));
	}
	
	@PostMapping("/lista")
	@Secured("Administrador")
	public ResponseEntity<List<Funcionario>> create(@RequestBody @Valid List<FuncionarioDTO> funcionariosDTO) {
		return super.created(funcionarioService.create(funcionariosDTO.stream().map(funcionarioDTO -> {
			Funcionario funcionario = Funcionario.builder().build();
			BeanUtils.copyProperties(funcionarioDTO, funcionario);
			funcionario.setTelefones(List.of(Telefone.builder().numero(funcionarioDTO.getNumero()).build()));
			return funcionario;
		}).toList()));
	}
	
	@PutMapping("/{idFuncionario}")
	public ResponseEntity<Funcionario>  update(@PathVariable UUID idFuncionario, @RequestBody @Valid FuncionarioUpdateDTO funcionarioUpdateDTO) {
		var funcionario = Funcionario.builder().build();
		BeanUtils.copyProperties(funcionarioUpdateDTO, funcionario);
		return super.ok(funcionarioService.update(idFuncionario, funcionario));
	}
	
	@PatchMapping("/email/{idFuncionario}")
	public ResponseEntity<Funcionario> updateEmail(@PathVariable UUID idFuncionario, @RequestBody @Valid UpdateEmailDTO updateEmailDTO) {
		return super.ok(funcionarioService.updateEmail(idFuncionario, updateEmailDTO));
	}
	
	@PatchMapping("/pais/{idFuncionario}/{idPais}")
	public ResponseEntity<Funcionario> updateCountry(@PathVariable UUID idFuncionario, @PathVariable UUID idPais) {
		return super.ok(funcionarioService.updateCountry(idFuncionario, idPais));
	}
	
	@PatchMapping("/fotoPerfil/{idFuncionario}")
	public ResponseEntity<Funcionario> updateProfilePhoto(@PathVariable UUID idFuncionario, @RequestParam MultipartFile fotoPerfil) {
		return super.ok(funcionarioService.updateProfilePhoto(idFuncionario, fotoPerfil));
	}

	@Secured("Administrador")
	@DeleteMapping("/{idFuncionario}")
	public ResponseEntity<Funcionario>  delete(@PathVariable UUID idFuncionario) {
		return super.ok(funcionarioService.delete(idFuncionario));
	}
}
