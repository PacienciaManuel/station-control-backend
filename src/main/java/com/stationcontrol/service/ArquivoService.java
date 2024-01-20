package com.stationcontrol.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.stationcontrol.model.Arquivo;

public interface ArquivoService extends AbstractService<Arquivo, UUID> {
	public List<Arquivo>  create(UUID idOcorrencia, MultipartFile[] multipartFiles);
}
