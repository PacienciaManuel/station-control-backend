package com.stationcontrol.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stationcontrol.dto.SenhaDTO;
import com.stationcontrol.exception.DataNotFoundException;
import com.stationcontrol.exception.UnauthorizedException;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.repository.FuncionarioRepository;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.storage.StorageService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class FuncionarioServiceImpl extends AbstractServiceImpl<Funcionario, UUID, FuncionarioRepository> implements FuncionarioService {
	
	private final PasswordEncoder encoder;

	private final StorageService storageService;
	

	public FuncionarioServiceImpl(FuncionarioRepository repository, HttpServletRequest request,
			MessageSource messageSource, PasswordEncoder encoder, StorageService storageService) {
		super(repository, request, messageSource);
		this.encoder = encoder;
		this.storageService = storageService;
	}

	@Override
	public Funcionario findByEmail(String email) throws DataNotFoundException {
		return this.repository.findByEmail(email)
		.orElseThrow(() -> new DataNotFoundException(messageSource.getMessage("data.notfound", null, request.getLocale())));
	}
	
	@Override
	public Funcionario create(Funcionario funcionario) {
		funcionario.setSenha(encoder.encode(funcionario.getSenha()));
		return super.save(funcionario);
	}
	
	@Override
	public List<Funcionario> create(List<Funcionario> funcionarios) {
		return super.save(funcionarios.stream().map(funcionario -> {
			funcionario.setSenha(encoder.encode(funcionario.getSenha()));
			return funcionario;
		}).toList());
	}

	@Override
	public Funcionario update(UUID id, Funcionario funcionario) {
		Funcionario entity = this.findById(id);
		entity.setNome(funcionario.getNome());
		entity.setGenero(funcionario.getGenero());
		entity.setDataNascimento(funcionario.getDataNascimento());
		entity.setMorada(funcionario.getMorada());
		entity.setNotaInformativa(funcionario.getNotaInformativa());
		return super.save(entity);
	}
	
	@Override
	public Funcionario updatePassword(UUID idFuncionario, SenhaDTO senhaDTO) throws UnauthorizedException {
		var entity = this.findById(idFuncionario);
		if (!encoder.matches(senhaDTO.getAntiga(), entity.getSenha())) {
			throw new UnauthorizedException(messageSource.getMessage("old.invalid.password", null, request.getLocale()));
		}
		entity.setSenha(encoder.encode(senhaDTO.getNova()));
		return super.save(entity);
	}
	
	@Override
	public Funcionario updateProfilePhoto(UUID idFuncionario, MultipartFile fotoPerfil) {
		var entity = this.findById(idFuncionario);
		String filename = storageService.store(fotoPerfil);
		storageService.delete(entity.getFotoPerfil());
		entity.setFotoPerfil(filename);
		return super.save(entity);
	}
	
	@Override
	public Funcionario delete(UUID id) {
		Funcionario funcionario = super.delete(id);
		storageService.delete(funcionario.getFotoPerfil());
		return funcionario;	
	}
}
