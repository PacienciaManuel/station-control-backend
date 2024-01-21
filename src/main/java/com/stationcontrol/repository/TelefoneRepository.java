package com.stationcontrol.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stationcontrol.model.Suspeito;
import com.stationcontrol.model.Telefone;

public interface TelefoneRepository extends JpaRepository<Telefone, UUID> {
	
	@Query(value = "select id, numero from telefones t left join suspeitos s on (s.telefone_id=t.id and s=?1 limit 1)", nativeQuery = true)
	Optional<Telefone> findBySuspeito(Suspeito suspeito);
}
