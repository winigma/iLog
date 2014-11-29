package br.com.ilog.importacao.business.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.cits.commons.citsbusiness.util.Identificavel;

@Entity
@Table( name = "PROC_STATUS" )
public class ProcStatus implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 8992342063945031169L;
	
	@Id
	@SequenceGenerator( name = "gen", sequenceName = "proc_status_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@JoinColumn(name = "ID_PROC", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcBroker procBroker;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_ABERTURA" )
	private Date dtAbertura;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_FECHAMENTO" )
	private Date dtFechamento;
	
	@Column( name = "DESCRICAO", length = 120 )
	private String descricao;
	
	@Column( name = "STATUS", length = 1 )
	private String status;
	

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
	 * @return the procBroker
	 */
	public ProcBroker getProcBroker() {
		return procBroker;
	}


	/**
	 * @param procBroker the procBroker to set
	 */
	public void setProcBroker(ProcBroker procBroker) {
		this.procBroker = procBroker;
	}


	/**
	 * @return the dtAbertura
	 */
	public Date getDtAbertura() {
		return dtAbertura;
	}


	/**
	 * @param dtAbertura the dtAbertura to set
	 */
	public void setDtAbertura(Date dtAbertura) {
		this.dtAbertura = dtAbertura;
	}


	/**
	 * @return the dtFechamento
	 */
	public Date getDtFechamento() {
		return dtFechamento;
	}


	/**
	 * @param dtFechamento the dtFechamento to set
	 */
	public void setDtFechamento(Date dtFechamento) {
		this.dtFechamento = dtFechamento;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
