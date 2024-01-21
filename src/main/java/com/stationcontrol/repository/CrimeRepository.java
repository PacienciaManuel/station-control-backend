package com.stationcontrol.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stationcontrol.model.Crime;

public interface CrimeRepository extends JpaRepository<Crime, UUID> {
	
	@Query(
		nativeQuery = true,
		value = "select * from crimes as c left join crimes_ocorrencias as co on (co.crime_id=c.id and co.ocorrencia_id=?1) where co.ocorrencia_id=?1 and lower(descricao) like lower(concat('%', concat(?2, '%')))",
		countQuery = "select count(co.ocorrencia_id) from crimes as c left join crimes_ocorrencias as co on (co.crime_id=c.id and co.ocorrencia_id=?1) where co.ocorrencia_id=?1 and lower(descricao) like lower(concat('%', concat(?2, '%')))"
	)
	public Page<Crime> paginationByOccurrence(UUID idOcorrencia, String descricao, Pageable pageable);
}
