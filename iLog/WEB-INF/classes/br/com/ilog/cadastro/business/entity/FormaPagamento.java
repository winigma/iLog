package br.com.ilog.cadastro.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.cits.commons.citsbusiness.util.Identificavel;

@Entity
@Table( name = "FORMA_PAGAMENTO" )
public class FormaPagamento implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -632203413207433362L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "forma_pagamento_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@Column(name = "CODE_SAP", length = 10)
	private String codeSAP;

	@Column(name = "DESCRICAO", length = 50)
	private String descricao;
	
	@Column(name = "ATIVO")
	private Boolean ativo;
	
	@Column(name = "CODIGO", length = 10)
	private String codigo;
	
	@Override
	public Long getPK() {
		return id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the codeSAP
	 */
	public String getCodeSAP() {
		return codeSAP;
	}

	/**
	 * @param codeSAP the codeSAP to set
	 */
	public void setCodeSAP(String codeSAP) {
		this.codeSAP = codeSAP;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the ativo
	 */
	public Boolean getAtivo() {
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FormaPagamento))
			return false;
		FormaPagamento other = (FormaPagamento) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
}
