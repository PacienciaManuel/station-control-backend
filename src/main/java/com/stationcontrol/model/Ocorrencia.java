package com.stationcontrol.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.Length;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.stationcontrol.model.converter.StatusConverter;

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
@JsonClassDescription("ocorrencias")
@JsonRootName(value = "ocorrencia", namespace = "ocorrencias")
@Table(
	name = "ocorrencias",
	indexes = @Index(name = "idx_ocorrencias_funcionario_id", columnList = "funcionario_id")
)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"id", "status", "local", "totalArquivos", "totalObjectos", "totalCrimes", "totalSuspeitos", "dataOcorrencia", "dataCriacao", "dataAtualizacao", "descricao", "funcionario", "requerente"})
public class Ocorrencia {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	@Convert(converter = StatusConverter.class)
	private Status status;
	
	@Column(name = "descricao", length = Length.LONG32, nullable = false)
	private String descricao;
	
	private String local;

	@Column(name = "data_ocorrencia", nullable = false)
	private LocalDateTime dataOcorrencia;

	@CreationTimestamp(source = SourceType.DB)
	@Column(name = "data_criacao", nullable = false, updatable = false)
	private LocalDateTime dataCriacao;

	@UpdateTimestamp(source = SourceType.DB)
	@Column(name = "data_atualizacao", insertable = false)
	private LocalDateTime dataAtualizacao;

	@Formula("(SELECT COUNT(a.ocorrencia_id) FROM ocorrencias o LEFT JOIN arquivos a ON (a.ocorrencia_id=o.id) WHERE o.id=id GROUP BY o.id)")
	private Long totalArquivos;
	
	@Formula("(SELECT COUNT(obj.ocorrencia_id) FROM ocorrencias o LEFT JOIN objectos obj ON (obj.ocorrencia_id=o.id) WHERE o.id=id GROUP BY o.id)")
	private Long totalObjectos;
	
	@Formula("(SELECT COUNT(c.ocorrencia_id) FROM ocorrencias o LEFT JOIN crimes_ocorrencias c ON (c.ocorrencia_id=o.id) WHERE o.id=id GROUP BY o.id)")
	private Long totalCrimes;

	@Formula("(SELECT COUNT(s.ocorrencia_id) FROM ocorrencias o LEFT JOIN suspeitos_ocorrencias s ON (s.ocorrencia_id=o.id) WHERE o.id=id GROUP BY o.id)")
	private Long totalSuspeitos;

	@ManyToOne
	@JoinColumn(name = "requerente_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_ocorrencias_requerentes"))
	private Requerente requerente;

	@ManyToOne
	@JoinColumn(name = "funcionario_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_ocorrencias_funcionarios"))
	private Funcionario funcionario;
	
	@JsonIgnore
	@OneToMany(mappedBy = "ocorrencia", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Arquivo> arquivos;
	
	@CollectionTable(
		name = "objectos", 
		indexes = @Index(name = "idx_objectos_ocorrencia_id", columnList = "ocorrencia_id"), 
		uniqueConstraints = @UniqueConstraint(name = "uk_objectos_ocorrencia_id", columnNames = {"objecto", "ocorrencia_id"}),
		joinColumns = @JoinColumn(name = "ocorrencia_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_objectos_ocorrencias"))
	)
	@JsonIgnore
	@OrderBy("objecto asc")
	@Column(name = "objecto", nullable = false)
	@ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
	private List<String> objectos;

	@JoinTable(
		name="crimes_ocorrencias",
		indexes = @Index(name = "idx_crimes_ocorrencias_ocorrencia_id", columnList = "ocorrencia_id"),
		uniqueConstraints = @UniqueConstraint(name = "uk_crimes_ocorrencias_ocorrencia_id_crime_id", columnNames = {"ocorrencia_id", "crime_id"}),
        joinColumns=@JoinColumn(name="ocorrencia_id", referencedColumnName="id", nullable = false, foreignKey = @ForeignKey(name = "fk_crimes_ocorrencias_ocorrencia")),
        inverseJoinColumns=@JoinColumn(name="crime_id", referencedColumnName="id", nullable = false, foreignKey = @ForeignKey(name = "fk_crimes_ocorrencias_crime"))
	)
	@JsonIgnore
	@OneToMany(orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Crime> crimes;

	@JsonIgnore
	@OneToMany(mappedBy = "ocorrencia", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<SuspeitoOcorrencia> suspeitosOcorrencia;
}
