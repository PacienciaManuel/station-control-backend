package com.stationcontrol.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonClassDescription("paise")
@JsonPropertyOrder({"id","nome"})
@JsonRootName(value = "pais", namespace = "paises")
@Table(
	name = "paises",
	uniqueConstraints = @UniqueConstraint(name = "uk_paises_nome", columnNames = "nome")
)
public class Pais {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false)
	@NotBlank(message = "{Pais.nome.notblank}")
	private String nome;
}
