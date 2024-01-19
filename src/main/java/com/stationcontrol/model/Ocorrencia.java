package com.stationcontrol.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.Length;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.stationcontrol.model.converter.PapelConverter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
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
@Table(name = "ocorrencias")
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"id", "status", "dataOcorrencia", "dataCriacao", "descricao", "objectos", "requerente", "funcionario", "crimes"})
public class Ocorrencia {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	@Convert(converter = PapelConverter.class)
	private Status status;
	
	@Column(name = "descricao", length = Length.LONG32, nullable = false)
	private String descricao;

	@Column(name = "data_ocorrencia", nullable = false)
	private LocalDateTime dataOcorrencia;

	@CreationTimestamp(source = SourceType.DB)
	@Column(name = "data_criacao", nullable = false, updatable = false)
	private String dataCriacao;
	
	@CollectionTable(
		name = "objectos", 
		indexes = @Index(name = "idx_objectos_ocorrencia_id", columnList = "ocorrencia_id"), 
		uniqueConstraints = @UniqueConstraint(name = "uk_objectos_ocorrencia_id", columnNames = {"objecto", "ocorrencia_id"}),
		joinColumns = @JoinColumn(name = "ocorrencia_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_objectos_ocorrencias"))
	)
	@JsonIgnore
	@OrderBy("objecto asc")
	@Column(name = "objecto", nullable = false)
	@ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
	private List<String> objectos;

	@ManyToOne
	@JoinColumn(name = "requerente_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_ocorrencias_requerentes"))
	private Requerente requerente;

	@ManyToOne
	@JoinColumn(name = "funcionario_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_ocorrencias_funcionarios"))
	private Funcionario funcionario;

	@JoinTable(
		name="crimes_ocorrencias",
		indexes = @Index(name = "idx_crimes_ocorrencias_funcionario_id", columnList = "ocorrencia_id"),
		uniqueConstraints = @UniqueConstraint(name = "uk_crimes_ocorrencias_ocorrencia_id_crime_id", columnNames = {"ocorrencia_id", "crime_id"}),
        joinColumns=@JoinColumn(name="ocorrencia_id", referencedColumnName="id", nullable = false, foreignKey = @ForeignKey(name = "fk_crimes_ocorrencias_ocorrencia")),
        inverseJoinColumns=@JoinColumn(name="crime_id", referencedColumnName="id", nullable = false, foreignKey = @ForeignKey(name = "fk_crimes_ocorrencias_crime"))
	)
	@OneToMany(orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Crime> crimes;
}
