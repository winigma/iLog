package br.com.ilog.importacao.business.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import br.cits.commons.citsbusiness.util.Identificavel;

@Entity
@Table( name = "PROC_ITEM" )
public class ProcItem implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -3172271333986030122L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "proc_item_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@JoinColumn(name = "ID_PROC", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcBroker procBroker;
	
	@Column( name = "NR_ADICAO" )
	private Integer nrAdicao;
	
	@Column( name = "NCM", length = 10 )
	private String ncm;
	
	@Column( name = "NR_LI", length = 10 )
	private String nrLi;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_LI" )
	private Date dtLi;
	
	@Column( name = "APLICACAO" )
	private Integer aplicacao;
	
	@Column( name = "COBERTURA_CAMBIAL" )
	private Integer coberturaCambial;
	
	@Column( name = "FUNDAMENTO_LEGAL" )
	private Integer fundamentoLegal;
	
	@Column( name = "REGIME_TRIBUTARIO" )
	private Integer regimeTributario;
	
	@Column( name = "PERIODICIDADE" )
	private Integer periodicidade;
	
	@OneToMany(mappedBy = "procItem", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<ProcItemPo> itensPo;
	
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
	 * @return the nrAdicao
	 */
	public Integer getNrAdicao() {
		return nrAdicao;
	}

	/**
	 * @param nrAdicao the nrAdicao to set
	 */
	public void setNrAdicao(Integer nrAdicao) {
		this.nrAdicao = nrAdicao;
	}

	/**
	 * @return the nrLi
	 */
	public String getNrLi() {
		return nrLi;
	}

	/**
	 * @param nrLi the nrLi to set
	 */
	public void setNrLi(String nrLi) {
		this.nrLi = nrLi;
	}

	/**
	 * @return the dtLi
	 */
	public Date getDtLi() {
		return dtLi;
	}

	/**
	 * @param dtLi the dtLi to set
	 */
	public void setDtLi(Date dtLi) {
		this.dtLi = dtLi;
	}

	/**
	 * @return the aplicacao
	 */
	public Integer getAplicacao() {
		return aplicacao;
	}

	/**
	 * @param aplicacao the aplicacao to set
	 */
	public void setAplicacao(Integer aplicacao) {
		this.aplicacao = aplicacao;
	}

	/**
	 * @return the coberturaCambial
	 */
	public Integer getCoberturaCambial() {
		return coberturaCambial;
	}

	/**
	 * @param coberturaCambial the coberturaCambial to set
	 */
	public void setCoberturaCambial(Integer coberturaCambial) {
		this.coberturaCambial = coberturaCambial;
	}

	/**
	 * @return the fundamentoLegal
	 */
	public Integer getFundamentoLegal() {
		return fundamentoLegal;
	}

	/**
	 * @param fundamentoLegal the fundamentoLegal to set
	 */
	public void setFundamentoLegal(Integer fundamentoLegal) {
		this.fundamentoLegal = fundamentoLegal;
	}

	/**
	 * @return the regimeTributario
	 */
	public Integer getRegimeTributario() {
		return regimeTributario;
	}

	/**
	 * @param regimeTributario the regimeTributario to set
	 */
	public void setRegimeTributario(Integer regimeTributario) {
		this.regimeTributario = regimeTributario;
	}

	/**
	 * @return the periodicidade
	 */
	public Integer getPeriodicidade() {
		return periodicidade;
	}

	/**
	 * @param periodicidade the periodicidade to set
	 */
	public void setPeriodicidade(Integer periodicidade) {
		this.periodicidade = periodicidade;
	}

	/**
	 * @return the itensPo
	 */
	public List<ProcItemPo> getItensPo() {
		return itensPo;
	}

	/**
	 * @param itensPo the itensPo to set
	 */
	public void setItensPo(List<ProcItemPo> itensPo) {
		this.itensPo = itensPo;
	}

	/**
	 * @return the ncm
	 */
	public String getNcm() {
		return ncm;
	}

	/**
	 * @param ncm the ncm to set
	 */
	public void setNcm(String ncm) {
		this.ncm = ncm;
	}

}
