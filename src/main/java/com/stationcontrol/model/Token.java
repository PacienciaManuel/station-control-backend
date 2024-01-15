package com.stationcontrol.model;

import org.hibernate.Length;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
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
@Table(name = "tokens")
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"acesso", "atualizacao", "tipo", "funcionario"})
public class Token {
	@Column(length = Length.LONG32, nullable = false)
	private String acesso;

	@Column(nullable = false)
	private String tipo;
	
	@Id
	@Column(length = Length.LONG32, nullable = false)
	private String atualizacao;
	
	@JsonIgnore
	@ColumnDefault("true")
	@Column(nullable = false, insertable = false)
	private Boolean valido;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "funcionario_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_tokens_funcionarios"))
	private Funcionario funcionario;
}
