package br.com.ilog.exportacao.business.entity;

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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.Cidade;

@Entity
@Table(name = "ITEM_FOLLOWUP_EXPOTACAO")
public class ItemFollowup implements Identificavel<Long> {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator( name = "gen", sequenceName = "item_followup_expo_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;

	
	@Temporal(TemporalType.DATE)
	@Label( "lblDtRealizado" )
	@Column( name = "DT_REALIZADO", nullable = true )
	private Date dtRealizado;
	
	@Temporal(TemporalType.DATE)
	@NotNull(message="notnull")
	@Label( "lblDtPlanejado" )
	@Column( name = "DT_PLANEJADO", nullable = false )
	private Date dtPlanejado;
	
	@Column( name = "CANAL")
	private boolean canal;
	
	@JoinColumn( name = "ID_CIDADE" )
	@ManyToOne( fetch = FetchType.EAGER )
	@Fetch(FetchMode.JOIN)
	@Label("lblCidade")
	private Cidade cidade;
	
	
	@JoinColumn( name = "ID_FOLLOW_UP" ,insertable=true, updatable=true)
	@ManyToOne( fetch = FetchType.LAZY )
	@Label( "lblFollowUp" )
	@Fetch(FetchMode.JOIN)
	@Cascade( value = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE } )	
	@NotNull( message = "notnull" )
	private Followup followup;
	
	@Override
	public Long getPK() {
		return this.id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDtRealizado() {
		return dtRealizado;
	}

	public void setDtRealizado(Date dtRealizado) {
		this.dtRealizado = dtRealizado;
	}

	public Date getDtPlanejado() {
		return dtPlanejado;
	}

	public void setDtPlanejado(Date dtPlanejado) {
		this.dtPlanejado = dtPlanejado;
	}

	public boolean isCanal() {
		return canal;
	}

	public void setCanal(boolean canal) {
		this.canal = canal;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Followup getFollowup() {
		return followup;
	}

	public void setFollowup(Followup followup) {
		this.followup = followup;
	}

}
