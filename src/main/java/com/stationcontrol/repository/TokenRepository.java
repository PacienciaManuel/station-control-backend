package com.stationcontrol.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stationcontrol.model.Funcionario;
import com.stationcontrol.model.Token;


public interface TokenRepository extends JpaRepository<Token, String> {
	boolean existsByAtualizacaoAndValido(String atualizacao, Boolean valido);
	public Optional<Token> findOneByAtualizacaoAndValido(String atualizacao, Boolean valido);
	public List<Token> findAllByFuncionarioAndValido(Funcionario funcionario, Boolean valido);
}
