package com.stationcontrol.model;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.Length;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.stationcontrol.model.converter.GeneroConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@JsonClassDescription("suspeito")
@JsonRootName(value = "suspeito", namespace = "suspeitos")
@Table(
	name = "suspeitos",
	indexes = @Index(name = "idx_suspeitos_bilhete_identidade", columnList = "bilhete_identidade"),
	uniqueConstraints = @UniqueConstraint(name = "uk_suspeitos_bilhete_identidade", columnNames = "bilhete_identidade")
)
@JsonPropertyOrder({"id", "nome", "genero", "dataNascimento", "dataCriacao", "dataAtualzacao", "detido", "bilheteIdentidade", "foto", "pais", "biografia"})
public class Suspeito {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	@Convert(converter = GeneroConverter.class)
	private Genero genero;
	
	@Column(nullable = false)
	private LocalDate dataNascimento;
	
	@Column(nullable = false)
	private Boolean detido;

	@Column(name = "bilhete_identidade", nullable = false)
	private String bilheteIdentidade;

	@Column(name = "foto")
	private String foto;

	@CreationTimestamp(source = SourceType.DB)
	@Column(name = "data_criacao", nullable = false, updatable = false)
	private String dataCriacao;

	@UpdateTimestamp(source = SourceType.DB)
	@Column(name = "data_atualzacao", insertable = false)
	private String dataAtualzacao;

	@Column(name = "biografia", length = Length.LONG32, nullable = false)
	private String biografia;
	
	@ManyToOne
	@JoinColumn(name = "pais_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_suspeitos_paises"))
	private Pais pais;
}
