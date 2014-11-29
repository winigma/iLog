package br.com.ilog.importacao.business.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.seguranca.business.entity.Usuario;

@Entity
@Table(name = "HISTORICO_STATUS_INVOICE")
public class HistoricoStatusInvoice implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 4629931947539878912L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "hist_status_invoice_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_INVOICE", nullable = false)
	@Label("lblInvoice")
	@NotNull(message="notnull")
	private Invoice invoice;

	@NotNull(message="notnull")
	@Temporal(TemporalType.TIMESTAMP)
	@Label("lblDtModificacao")
	@Column(name = "DT_MODIFICACAO", nullable = false)
	private Date dtModificacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USER", nullable = false)
	@Label("lblUsuario")
	@NotNull(message="notnull")
	private Usuario usuario;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusInvoice status;

	@Column(name = "OBS_JUSTIF", nullable = true, length = 255)
	private String obsJustificativa;
	
	@Column( name = "JUSTIFICATIVA", length = 255 )
	private String justificativa;

	@OneToMany(mappedBy = "historico", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<HistStatusItensInvoice> itens;

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

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Date getDtModificacao() {
		return dtModificacao;
	}

	public void setDtModificacao(Date dtModificacao) {
		this.dtModificacao = dtModificacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public StatusInvoice getStatus() {
		return status;
	}

	public void setStatus(StatusInvoice status) {
		this.status = status;
	}

	public String getObsJustificativa() {
		return obsJustificativa;
	}

	public void setObsJustificativa(String obsJustificativa) {
		this.obsJustificativa = obsJustificativa;
	}

	public List<HistStatusItensInvoice> getItens() {
		return itens;
	}

	public void setItens(List<HistStatusItensInvoice> itens) {
		this.itens = itens;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

}
