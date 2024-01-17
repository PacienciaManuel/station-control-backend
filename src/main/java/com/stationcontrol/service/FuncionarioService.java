package com.stationcontrol.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.stationcontrol.dto.SenhaDTO;
import com.stationcontrol.dto.UpdateEmailDTO;
import com.stationcontrol.exception.DataNotFoundException;
import com.stationcontrol.exception.UnauthorizedException;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.model.Telefone;

public interface FuncionarioService extends AbstractService<Funcionario, UUID> {
	public Funcionario findByEmail(String email) throws DataNotFoundException;
	public Funcionario updateCountry(UUID idUsuario, UUID idPais);
	public List<Funcionario> create(List<Funcionario> funcionarios);
	public Funcionario update(UUID idFuncionario, Funcionario funcionario);
	public Funcionario updateProfilePhoto(UUID idFuncionario, MultipartFile fotoPerfil);
	public Funcionario updatePassword(UUID idFuncionario, SenhaDTO senhaDTO) throws UnauthorizedException;
	public Funcionario updateEmail(UUID idUsuario, UpdateEmailDTO updateEmailDTO) throws UnauthorizedException;
	public Funcionario create(UUID idPais, Funcionario funcionario, Telefone telefone, Optional<MultipartFile> fotoPerfil);
}
