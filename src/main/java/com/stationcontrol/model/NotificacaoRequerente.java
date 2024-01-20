package com.stationcontrol.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@JsonClassDescription("notificacaoRequerente")
@JsonRootName(value = "notificacao", namespace = "notificacoesFuncionarios")
@Table(name = "notificacoes_requerentes", indexes = @Index(name = "idx_notificacoes_requerente_id", columnList = "requerente_id"))
@JsonPropertyOrder({"id","titulo","recebido","dataRecebido","dataNotificacao","dataAtualizacao","descricao","funcionario","requerente"})
@PrimaryKeyJoinColumn(name="id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_notificacoes_requerentes_notificacoes"))
public class NotificacaoRequerente extends Notificacao {
	@ColumnDefault("false")
	@Column(nullable = false, insertable = false)
	private Boolean recebido;

	@Column(name = "data_recebido", insertable = false)
	private LocalDateTime dataRecebido;
	
	@ManyToOne
	@JoinColumn(name = "requerente_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_notificacoes_requerentes_requerentes"))
	private Requerente requerente;
}
