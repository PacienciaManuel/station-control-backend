package com.stationcontrol.model;

import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonClassDescription("arquivo")
@JsonRootName(value = "arquivo", namespace = "arquivos")
@JsonPropertyOrder({"id","nome","url","tipo","dataArquivo"})
@Table(name = "arquivos", indexes = @Index(name = "idx_arquivos_ocorrencia_id", columnList = "ocorrencia_id"))
public class Arquivo {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private String url;
	
	@Column(nullable = false)
	private String tipo;

	@CreationTimestamp(source = SourceType.DB)
	@Column(name = "data_arquivo", nullable = false, updatable = false)
	private String dataArquivo;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ocorrencia_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_arquivos_ocorrencias"))
	private Ocorrencia ocorrencia;
}
