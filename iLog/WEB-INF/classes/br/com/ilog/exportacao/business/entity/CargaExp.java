package br.com.ilog.exportacao.business.entity;

import java.math.BigDecimal;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.CondicaoPagamento;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Projeto;



@Entity
@Table(name = "CARGA_EXPOTACAO")
public class CargaExp implements Identificavel<Long>{

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator( name = "gen", sequenceName = "carga_expo_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@Column(name = "NUM_SEQ" )
	private Integer numSequencia;
	
	@Enumerated(EnumType.STRING)
	@Label("lblStatus")
	@NotNull(message = "notnull")
	private StatusCargaExp  status;
	
	@Column(name = "ANO_SEQ" )
	private Integer anoSequencia;
	
	@Column(name = "DANFE", length = 25)
	@Label("lblDanfe")
	private String danfe;
	
	@JoinColumn(name = "CLIENTE_ID", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblExportador")
	private PessoaJuridica cliente;

	@Column(name = "HAWB", length = 20)
	@Label("lblHawb")
	private String hawb;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_HAWB")
	@Label("lblDtColeta")
	private Date dataHawb;
	
	
	@Column(name = "MAWB", length = 20)
	@Label("lblMawb")
	private String mawb;
	
	@JoinColumn(name = "MODAL_ID", nullable = true)
	@OneToOne(fetch = FetchType.EAGER)
	@Label("lblModal")
	private Modal modal;
	
	@JoinColumn(name = "MOEDA_ID", nullable = true)
	@OneToOne(fetch = FetchType.EAGER)
	@Label("lblMoeda")
	private Moeda moeda;
	
	@Column(name = "RE", length = 20)
	@Label("lblRE")
	private String re;
	
	@Column(name = "DDE", length = 20)
	@Label("lblDDE")
	private String dde;
	
	
	@Column(name = "DSE", length = 20)
	@Label("lblDSE")
	private String dse;
	
	@Column(name = "VALOR_FOBL", scale = 4, precision = 12)
	@Label("lblValorFob")
	private BigDecimal valorFob;
	
	@Column(name = "PESO_BRUTO", scale = 5, precision = 11)
	@Label("lblPesoBruto")
	private BigDecimal pesoBruto;
	
	@Column(name = "PESO_CUBADO", scale = 5, precision = 11)
	@Label("lblPesoCubado")
	private BigDecimal pesoCubado;
	
	@Column(name = "QNT_VOLUME")
	@Label("lblQntVolume")
	private Integer qntVolume;
	
	
	@Column(name = "VALOR_FRETE", scale = 2, precision = 12)
	@Label("lblValorFrete")
	private BigDecimal valorFrete;
	
	@Column(name = "FORMA_PAGAMENTO")
	@Enumerated(EnumType.STRING)
	@Label("lblFormaPagamento")
	private CondicaoPagamento condicaoPagamento;
	
	@JoinColumn(name = "PROJETO_ID", nullable = true)
	@OneToOne(fetch = FetchType.EAGER)
	@Label("lblProjeto")
	private Projeto projeto;
	
	@OneToMany(mappedBy = "carga", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<InvoiceExp> invoices;
	
	
	@OneToMany(mappedBy = "carga", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Followup> followups;
	
	@Transient
	private String processo;
	
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




	public Integer getNumSequencia() {
		return numSequencia;
	}




	public void setNumSequencia(Integer numSequencia) {
		this.numSequencia = numSequencia;
	}




	public Integer getAnoSequencia() {
		return anoSequencia;
	}




	public void setAnoSequencia(Integer anoSequencia) {
		this.anoSequencia = anoSequencia;
	}




	public PessoaJuridica getCliente() {
		return cliente;
	}




	public void setCliente(PessoaJuridica cliente) {
		this.cliente = cliente;
	}




	public String getHawb() {
		return hawb;
	}




	public void setHawb(String hawb) {
		this.hawb = hawb;
	}




	public Date getDataHawb() {
		return dataHawb;
	}




	public void setDataHawb(Date dataHawb) {
		this.dataHawb = dataHawb;
	}




	public String getMawb() {
		return mawb;
	}




	public void setMawb(String mawb) {
		this.mawb = mawb;
	}




	public Modal getModal() {
		return modal;
	}




	public void setModal(Modal modal) {
		this.modal = modal;
	}




	public Moeda getMoeda() {
		return moeda;
	}




	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}




	public String getRe() {
		return re;
	}




	public void setRe(String re) {
		this.re = re;
	}




	public String getDde() {
		return dde;
	}




	public void setDde(String dde) {
		this.dde = dde;
	}




	public String getDse() {
		return dse;
	}




	public void setDse(String dse) {
		this.dse = dse;
	}




	public BigDecimal getValorFob() {
		return valorFob;
	}




	public void setValorFob(BigDecimal valorFob) {
		this.valorFob = valorFob;
	}




	public BigDecimal getPesoBruto() {
		return pesoBruto;
	}




	public void setPesoBruto(BigDecimal pesoBruto) {
		this.pesoBruto = pesoBruto;
	}




	public BigDecimal getPesoCubado() {
		return pesoCubado;
	}




	public void setPesoCubado(BigDecimal pesoCubado) {
		this.pesoCubado = pesoCubado;
	}




	public Integer getQntVolume() {
		return qntVolume;
	}




	public void setQntVolume(Integer qntVolume) {
		this.qntVolume = qntVolume;
	}




	public BigDecimal getValorFrete() {
		return valorFrete;
	}




	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}




	public CondicaoPagamento getCondicaoPagamento() {
		return condicaoPagamento;
	}




	public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}




	public Projeto getProjeto() {
		return projeto;
	}




	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}




	public List<InvoiceExp> getInvoices() {
		return invoices;
	}




	public void setInvoices(List<InvoiceExp> invoices) {
		this.invoices = invoices;
	}




	public List<Followup> getFollowups() {
		return followups;
	}




	public void setFollowups(List<Followup> followups) {
		this.followups = followups;
	}




	public String getDanfe() {
		return danfe;
	}




	public void setDanfe(String danfe) {
		this.danfe = danfe;
	}




	public StatusCargaExp getStatus() {
		return status;
	}




	public void setStatus(StatusCargaExp status) {
		this.status = status;
	}




	public String getProcesso() {
		if(this.numSequencia != null &&  anoSequencia != null ){
			String seq = "0000" + numSequencia;
			seq = seq.substring(seq.length() - 4, seq.length());
			
			String ano = anoSequencia.toString();
			processo += "." + seq + "/" + ano.substring(ano.length() - 2, ano.length());
		}else{
			processo +=  ".0000/00";
		}
		
		return processo;
	}




	public void setProcesso(String processo) {
		this.processo = processo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CargaExp))
			return false;
		CargaExp other = (CargaExp) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
