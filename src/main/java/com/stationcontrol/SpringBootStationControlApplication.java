package com.stationcontrol;

import java.io.File;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stationcontrol.model.Crime;
import com.stationcontrol.model.Funcionario;
import com.stationcontrol.model.Genero;
import com.stationcontrol.model.Ocorrencia;
import com.stationcontrol.model.Papel;
import com.stationcontrol.model.Suspeito;
import com.stationcontrol.model.SuspeitoOcorrencia;
import com.stationcontrol.service.AbstractService;
import com.stationcontrol.service.FuncionarioService;
import com.stationcontrol.service.OcorrenciaService;
import com.stationcontrol.service.SuspeitoOcorrenciaService;
import com.stationcontrol.service.SuspeitoService;
import com.stationcontrol.storage.StorageService;

@SpringBootApplication
public class SpringBootStationControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStationControlApplication.class, args);
	}
	
	@Bean
	CommandLineRunner initStorage(StorageService storageService) {
		return args -> storageService.init();
	}

//	@Bean
	CommandLineRunner changePhoto(FuncionarioService funcionarioService, SuspeitoService suspeitoService) {
		return args -> {
			int i = 0;
			int j = 0;
			
			SecureRandom random = new SecureRandom();
			ObjectMapper objectMapper = new ObjectMapper();
			List<String> maleProfilePhoto = objectMapper.readValue(new File(this.getClass().getResource("/male_profile_photos.json").toURI()), new TypeReference<List<String>>(){});
			List<String> femaleProfilePhoto = objectMapper.readValue(new File(this.getClass().getResource("/female_profile_photos.json").toURI()), new TypeReference<List<String>>(){});
			var funcionarios = funcionarioService.findAll();
			var suspeitos = suspeitoService.findAll();
			for (var funcionario : funcionarios) {
				if (funcionario.getFotoPerfil() != null || random.nextBoolean()) continue;
				if (funcionario.getGenero().equals(Genero.MASCULINO)) {
					funcionario.setFotoPerfil(maleProfilePhoto.get(i++));
					i = i < maleProfilePhoto.size() ? i : 0;
				} else {
					funcionario.setFotoPerfil(femaleProfilePhoto.get(j++));
					j = j < femaleProfilePhoto.size() ? j : 0;
				}
			}
			for (var suspeito : suspeitos) {
				if (suspeito.getFoto() != null || random.nextBoolean()) continue;
				if (suspeito.getGenero().equals(Genero.MASCULINO)) {
					suspeito.setFoto(maleProfilePhoto.get(i++));
					i = i < maleProfilePhoto.size() ? i : 0;
				} else {
					suspeito.setFoto(femaleProfilePhoto.get(j++));
					j = j < femaleProfilePhoto.size() ? j : 0;
				}
			}
			funcionarioService.save(funcionarios);
			suspeitoService.save(suspeitos);
			System.out.println("==================== TERMINADO ====================");
		};
	}
	
//	@Bean
	CommandLineRunner initObjects(OcorrenciaService ocorrenciaService) {
		return args -> {
			SecureRandom random = new SecureRandom();
			ObjectMapper objectMapper = new ObjectMapper();
			List<Ocorrencia> ocorrencias = ocorrenciaService.pagination(0, 1000, Example.of(Ocorrencia.builder().build()), "dataAtualizacao", Direction.DESC).getContent();
			List<String> objectos = objectMapper.readValue(new File(this.getClass().getResource("/objectos.json").toURI()), new TypeReference<List<String>>(){});
			for (Ocorrencia ocorrencia : ocorrencias) {
				Set<String> datas = new HashSet<>();
				for (int i = random.nextInt(1, objectos.size()); i > 0; i--) {
					datas.add(objectos.get(random.nextInt(objectos.size() - 1)));
				}
				ocorrencia.getObjectos().addAll(datas);
			}
			ocorrenciaService.save(ocorrencias);
			System.out.println("==================== TERMINADO ====================");
		};
	}
	
//	@Bean
	CommandLineRunner initCrimes(OcorrenciaService ocorrenciaService, AbstractService<Crime, UUID> abstractService) {
		return args -> {
			SecureRandom random = new SecureRandom();
			List<Crime> crimes = abstractService.findAll();
			List<Ocorrencia> ocorrencias = ocorrenciaService.pagination(0, 100, Example.of(Ocorrencia.builder().build()), "dataAtualizacao", Direction.DESC).getContent();
			for (Ocorrencia ocorrencia : ocorrencias) {
				Set<Crime> datas = new HashSet<>();
				for (int i = random.nextInt(1, crimes.size()); i > 0; i--) {
					datas.add(crimes.get(random.nextInt(crimes.size() - 1)));
				}
				ocorrencia.getCrimes().addAll(datas);
			}
			ocorrenciaService.save(ocorrencias);
			System.out.println("==================== TERMINADO ====================");
		};
	}
	
//	@Bean
	CommandLineRunner initSuspeitos(
			FuncionarioService funcionarioService, OcorrenciaService ocorrenciaService, 
			SuspeitoService suspeitoService, SuspeitoOcorrenciaService suspeitoOcorrenciaService) {
		return args -> {
			SecureRandom random = new SecureRandom();
			List<Suspeito> suspeitos = suspeitoService.findAll();
			List<Ocorrencia> ocorrencias = ocorrenciaService.findAll();
			List<Funcionario> funcionarios = funcionarioService.findAll();
			List<SuspeitoOcorrencia> suspeitosOcorrencias = new ArrayList<>(suspeitos.size() + ocorrencias.size());
			for (Ocorrencia ocorrencia : ocorrencias) {
				Set<SuspeitoOcorrencia> datas = new HashSet<>();
				for (int i = random.nextInt(500); i > 0; i--) {
					datas.add(SuspeitoOcorrencia.builder()
					.ocorrencia(ocorrencia)
					.suspeito(suspeitos.get(random.nextInt(suspeitos.size() - 1)))
					.funcionario(funcionarios.get(random.nextInt(funcionarios.size() - 1)))
					.build());
				}
				suspeitosOcorrencias.addAll(datas);
			}
			suspeitoOcorrenciaService.save(suspeitosOcorrencias);
			System.out.println("==================== TERMINADO ====================");
		};
	}
	
//	@Bean
	CommandLineRunner change(OcorrenciaService ocorrenciaService, FuncionarioService funcionarioService) {
		return args -> {
			SecureRandom random = new SecureRandom();
			Funcionario funcionario = funcionarioService.findById(UUID.fromString("ba8e9685-987b-4449-ab5d-3050139480ed"));
			List<Ocorrencia> ocorrencias = ocorrenciaService.pagination(0, 200, Example.of(Ocorrencia.builder().build()), "dataAtualizacao", Direction.DESC).getContent();
			for (Ocorrencia ocorrencia : ocorrencias) {
				if (random.nextBoolean()) {					
					ocorrencia.setFuncionario(funcionario);
				}
			}
			ocorrenciaService.save(ocorrencias);
			System.out.println("==================== TERMINADO ====================");
		};
	}
	
//	@Bean
	CommandLineRunner changeCrimes(FuncionarioService funcionarioService, AbstractService<Crime, UUID> abstractService) {
		return args -> {
			SecureRandom random = new SecureRandom();
			ObjectMapper objectMapper = new ObjectMapper();
			List<Crime> crimes = abstractService.findAll();
			List<Funcionario> funcionarios = funcionarioService.findAll(Example.of(Funcionario.builder().papel(Papel.ADMINISTRADOR).build()), "nome", Direction.ASC);
			List<String> textos = objectMapper.readValue(new File(this.getClass().getResource("/textos.json").toURI()), new TypeReference<List<String>>(){});
			for (Crime crime : crimes) {
				crime.setVigor(random.nextBoolean());
				crime.setDataCriacao(LocalDateTime.of(LocalDate.of(random.nextInt(1970, 2020), random.nextInt(1, 12), random.nextInt(1, 28)), LocalTime.now()));
				crime.setDataAtualizacao(LocalDateTime.now());
				crime.setDescricao(textos.get(random.nextInt(textos.size() - 1)));
				crime.setFuncionario(funcionarios.get(random.nextInt(funcionarios.size() - 1)));
			}
			abstractService.save(crimes);
			System.out.println("==================== TERMINADO ====================");
		};
	}
}
