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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.seguranca.business.entity.Usuario;

/**
 * @author Heber Santiago
 * 
 *         Classe armazena valores de historico da mudanca de FollowUp.
 * 
 */
@Entity
@Table(name = "FOLLOW_UP_ESTIMADO")
public class FollowUpEstimado implements Identificavel<Long> {


	private static final long serialVersionUID = 4645253652237719111L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "follow_up_estimado_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	@Column(name = "ID")
	private Long id;


	
	
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "ID_FOLLOWUP_TRECHO", insertable = true, updatable = true)
	@Cascade(value = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.SAVE_UPDATE })
	@ManyToOne(fetch = FetchType.LAZY)
	private FollowUpImportTrecho followUpTrecho;

	@Column(name = "DT_ALTERACAO", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@OrderBy("desc")
	private Date dtAlteracao;

	@Column(name = "DT_ESTIMADA", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dtEstimada;

	@JoinColumn(name = "ID_CANAL_ESTIMADO")
	@ManyToOne(fetch = FetchType.LAZY)
	private ParametroCanal canalEstimado;

	@JoinColumn(name = "ID_AUTOR")
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario autor;

	@Column(name = "SISTEMA")
	private Boolean sitema;

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

	public FollowUpImportTrecho getFollowUpTrecho() {
		return followUpTrecho;
	}

	public void setFollowUpTrecho(FollowUpImportTrecho followUpTrecho) {
		this.followUpTrecho = followUpTrecho;
	}

	public Date getDtEstimada() {
		return dtEstimada;
	}

	public void setDtEstimada(Date dtEstimada) {
		this.dtEstimada = dtEstimada;
	}

	public ParametroCanal getCanalEstimado() {
		return canalEstimado;
	}

	public void setCanalEstimado(ParametroCanal canalEstimado) {
		this.canalEstimado = canalEstimado;
	}

	public Usuario getAutor() {
		return autor;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	public Boolean getSitema() {
		return sitema;
	}

	public void setSitema(Boolean sitema) {
		this.sitema = sitema;
	}

	public Date getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(Date dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}

}
