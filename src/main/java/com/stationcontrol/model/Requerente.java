package com.stationcontrol.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SourceType;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.stationcontrol.model.converter.GeneroConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@JsonPropertyOrder({"id", "nome", "genero", "dataNascimento", "morada", "bilheteIdentidade", "dataCriacao", "totalOcorrencias", "fotoPerfil", "pais", "telefone"})
public class Requerente {
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
	private String morada;

	@Column(name = "bilhete_identidade", nullable = false)
	private String bilheteIdentidade;

	@CreationTimestamp(source = SourceType.DB)
	@Column(name = "data_criacao", nullable = false, updatable = false)
	private LocalDateTime dataCriacao;
	
	@Formula("(SELECT COUNT(o.requerente_id) FROM requerentes r LEFT JOIN ocorrencias o ON (o.requerente_id=r.id) WHERE r.id=id GROUP BY r.id)")
	private Long totalOcorrencias;

	@Column(name = "foto_perfil")
	private String fotoPerfil;
	
	@OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "telefone_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_requerentes_telefones"))
	private Telefone telefone;
	
	@ManyToOne
	@JoinColumn(name = "pais_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_requerentes_paises"))
	private Pais pais;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "funcionario_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_requerentes_funcionarios"))
	private Funcionario funcionario;
	
	@JsonIgnore
	@OneToMany(mappedBy = "requerente", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Ocorrencia> ocorrencias;
}
