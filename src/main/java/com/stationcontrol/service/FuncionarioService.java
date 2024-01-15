package com.stationcontrol.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.stationcontrol.dto.SenhaDTO;
import com.stationcontrol.exception.DataNotFoundException;
import com.stationcontrol.exception.UnauthorizedException;
import com.stationcontrol.model.Funcionario;

public interface FuncionarioService extends AbstractService<Funcionario, UUID> {
	Funcionario findByEmail(String email) throws DataNotFoundException;
	Funcionario create(Funcionario funcionario);
	List<Funcionario> create(List<Funcionario> funcionarios);
	public Funcionario update(UUID id, Funcionario funcionario);
	Funcionario updateProfilePhoto(UUID idFuncionario, MultipartFile fotoPerfil);
	Funcionario updatePassword(UUID idFuncionario, SenhaDTO senhaDTO) throws UnauthorizedException;
}
