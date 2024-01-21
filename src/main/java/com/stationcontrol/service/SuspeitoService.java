package com.stationcontrol.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.stationcontrol.model.Suspeito;

public interface SuspeitoService extends AbstractService<Suspeito, UUID> {
	public List<Suspeito> create(List<Suspeito> suspeitos);
	public Suspeito updatePhoto(UUID idSuspeito, MultipartFile fotoPerfil);
	public Suspeito update(UUID idSuspeito, UUID idPais, Suspeito suspeito);
	public Suspeito create(UUID idFuncionario, UUID idPais, Suspeito suspeito, Optional<MultipartFile> fotoPerfil);
}
