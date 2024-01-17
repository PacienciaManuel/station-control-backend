package com.stationcontrol.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@JsonClassDescription("telefone")
@JsonPropertyOrder({"id","numero"})
@JsonRootName(value = "telefone", namespace = "telefones")
@Table(
		name = "telefones", 
		indexes = @Index(name = "idx_telefones_funcionario_id", columnList = "funcionario_id"),
		uniqueConstraints = @UniqueConstraint(name = "uk_telefones_numero_funcionario_id", columnNames = {"numero", "funcionario_id"})
		)
public class Telefone {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false, length = 15)
	private String numero;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "funcionario_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_telefones_funcionarios"))
	private Funcionario funcionario;
}
