package com.stationcontrol.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonClassDescription("requerente")
@JsonRootName(value = "requerente", namespace = "requerentes")
@Table(
	name = "requerentes",
	indexes = @Index(name = "idx_requerentes_bilhete_identidade", columnList = "bilhete_identidade"),
	uniqueConstraints = @UniqueConstraint(name = "uk_requerentes_bilhete_identidade", columnNames = "bilhete_identidade")
)
@JsonPropertyOrder({"id", "nome", "bilheteIdentidade", "telefone"})
public class Requerente {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false)
	private String nome;

	@Column(name = "bilhete_identidade", nullable = false)
	private String bilheteIdentidade;
	
	@ManyToOne
	@JoinColumn(name = "telefone_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_requerentes_telefones"))
	private Telefone telefone;
	
}
