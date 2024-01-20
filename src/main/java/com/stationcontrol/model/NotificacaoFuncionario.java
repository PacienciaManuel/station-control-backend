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
@JsonClassDescription("notificacaoFuncionario")
@JsonRootName(value = "notificacao", namespace = "notificacoesFuncionarios")
@JsonPropertyOrder({"id","titulo","visto","dataVisto","dataNotificacao","dataAtualizacao","descricao","funcionario","funcionarioDestino"})
@PrimaryKeyJoinColumn(name="id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_notificacoes_funcionarios_notificacoes"))
@Table(name = "notificacoes_funcionarios", indexes = @Index(name = "idx_notificacoes_funcionarios_funcionario_id", columnList = "funcionario_id"))
public class NotificacaoFuncionario extends Notificacao {
	
	@ColumnDefault("false")
	@Column(nullable = false, insertable = false)
	private Boolean visto;

	@Column(name = "data_visto", insertable = false)
	private LocalDateTime dataVisto;
	
	@ManyToOne
	@JoinColumn(name = "funcionario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_notificacoes_funcionarios_funcionarios"))
	private Funcionario funcionarioDestino;
}
