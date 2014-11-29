package br.com.ilog.importacao.business.entity;

import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.cadastro.business.entity.Trecho;

@Entity
@Table( name = "FOLLOW_UP_IMPORT_TRECHO" )
public class FollowUpImportTrecho implements Identificavel<Long> {
	
	/** */
	private static final long serialVersionUID = 1470741965131500466L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "follow_up_imp_trecho_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@NotNull(message="notnull")
	@Label( "lblDtPlanejado" )
	@Column( name = "DT_PLANEJADO", nullable = false )
	private Date dtPlanejado;
	
	@Temporal(TemporalType.DATE)
	@Label( "lblDtRealizado" )
	@Column( name = "DT_REALIZADO", nullable = true )
	private Date dtRealizado;
	
	@Column( name = "OTD" )
	private Integer otd;
	
	@JoinColumn( name = "ID_TRECHO" )
	@ManyToOne( fetch = FetchType.LAZY )
	@Label( "lblTrecho" )
	private Trecho trecho;
	
//	@JoinColumn( name = "ID_FOLLOW_UP", nullable = false )
	@JoinColumn( name = "ID_FOLLOW_UP" ,insertable=true, updatable=true)
	@Cascade( value = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE, CascadeType.DELETE } )
	@ManyToOne( fetch = FetchType.LAZY )
	@Label( "lblFollowUp" )
	private FollowUpImport followUp;
	
	@JoinColumn( name = "ID_CIDADE" )
	@ManyToOne( fetch = FetchType.LAZY )
	@Label( "lblFollowUp" )
	private Cidade cidade;
	
	@Column( name = "TX_LOCAL" )
	private String txLocal;
	
	@Column( name = "CANAL")
	private boolean canal;
	
	@JoinColumn( name = "ID_CANAL_ESTIMADO" )
	@ManyToOne( fetch = FetchType.LAZY )
	private ParametroCanal parametroCanal;
	
	@JoinColumn( name = "ID_CANAL_ATUAL" )
	@ManyToOne( fetch = FetchType.LAZY )
	private ParametroCanal parametroCanalAtual;
	
	@Column( name = "DIAS_PREVISTOS" )
	private int diasPrevistos;
	
	@OrderBy( value = "dtAlteracao desc" )
	@OneToMany(mappedBy = "followUpTrecho", fetch = FetchType.EAGER)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<FollowUpEstimado> estimados;
	
	@Override
	public Long getPK() {
		return id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDtPlanejado() {
		return dtPlanejado;
	}

	public void setDtPlanejado(Date dtPlanejado) {
		this.dtPlanejado = dtPlanejado;
	}

	public Date getDtRealizado() {
		return dtRealizado;
	}

	public void setDtRealizado(Date dtRealizado) {
		this.dtRealizado = dtRealizado;
	}

	public Trecho getTrecho() {
		return trecho;
	}

	public void setTrecho(Trecho trecho) {
		this.trecho = trecho;
	}

	public FollowUpImport getFollowUp() {
		return followUp;
	}

	public void setFollowUp(FollowUpImport followUp) {
		this.followUp = followUp;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getTxLocal() {
		return txLocal;
	}

	public void setTxLocal(String txLocal) {
		this.txLocal = txLocal;
	}

	public boolean isCanal() {
		return canal;
	}

	public void setCanal(boolean canal) {
		this.canal = canal;
	}

	public int getDiasPrevistos() {
		return diasPrevistos;
	}

	public void setDiasPrevistos(int diasPrevistos) {
		this.diasPrevistos = diasPrevistos;
	}

	public ParametroCanal getParametroCanal() {
		return parametroCanal;
	}

	public void setParametroCanal(ParametroCanal parametroCanal) {
		this.parametroCanal = parametroCanal;
	}

	public ParametroCanal getParametroCanalAtual() {
		return parametroCanalAtual;
	}

	public void setParametroCanalAtual(ParametroCanal parametroCanalAtual) {
		this.parametroCanalAtual = parametroCanalAtual;
	}

	/**
	 * Metodo transiente apenas para recuperar o style do texto para a entidade.
	 * @return
	 */
	public String getStyle() {
		if ( dtRealizado != null && dtRealizado.after( dtPlanejado ) ) {
			return "color: RED;";
		} else if ( this.isCanal() ) {
			if ( getOtd() != null && getOtd() > 0 ) {
				return "color: RED;";
			}
		}
		return "";
	}

	public List<FollowUpEstimado> getEstimados() {
		Collections.reverse(estimados);
		return estimados;
	}

	public void setEstimados(List<FollowUpEstimado> estimados) {
		this.estimados = estimados;
	}

	public Integer getOtd() {
		return otd;
	}

	public void setOtd(Integer otd) {
		this.otd = otd;
	}
	
}
