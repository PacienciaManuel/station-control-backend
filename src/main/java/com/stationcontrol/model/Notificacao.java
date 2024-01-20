package com.stationcontrol.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.Length;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notificacoes", indexes = @Index(name = "idx_notificacoes_funcionario_id", columnList = "funcionario_id"))
public abstract class Notificacao {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false)
	private String titulo;
	
	@Column(length = Length.LONG32, nullable = false)
	private String descricao;

	@CreationTimestamp(source = SourceType.DB)
	@Column(name = "data_notificacao", nullable = false, updatable = false)
	private LocalDateTime dataNotificacao;
	
	@UpdateTimestamp(source = SourceType.DB)
	@Column(name = "data_atualizacao", insertable = false)
	private LocalDateTime dataAtualizacao;

	@ManyToOne
	@JoinColumn(name = "funcionario_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_notificacoes_funcionarios"))
	private Funcionario funcionario;
}
