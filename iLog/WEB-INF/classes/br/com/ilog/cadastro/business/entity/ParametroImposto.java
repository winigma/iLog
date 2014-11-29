package br.com.ilog.cadastro.business.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * Classe de parametro imposto para o sistema
 * @author Heber Santiago
 *
 */
@Entity
@Table( name = "PARAMETRO_IMPOSTO" )
public class ParametroImposto implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 7209906865159735574L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "parametro_imposto_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@NotNull
	@Column( name = "DESCRICAO", length = 25 )
	private String descricao;
	
	@Column( name = "VALOR" )
	private BigDecimal valor;
	
	@Temporal(TemporalType.DATE)
	@Column( name = "DT_CADASTRO" )
	private Date dtCadastro;
	
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
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the dtCadastro
	 */
	public Date getDtCadastro() {
		return dtCadastro;
	}

	/**
	 * @param dtCadastro the dtCadastro to set
	 */
	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

}
