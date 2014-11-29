package br.com.ilog.importacao.business.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.OrderBy;
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
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.entity.Ocorrencia;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.seguranca.business.entity.Usuario;

@Entity
@Table(name = "CARGA")
public class Carga implements Identificavel<Long> {

	private static final long serialVersionUID = -3199045816940596538L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "carga_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_COLETA")
	@Label("lblDtColeta")
	private Date dtColeta;

	@Column(name = "HAWB", length = 20)
	@Label("lblHawb")
	private String hawb;
	
	@Column( name = "NUM_MASTER", length = 20 ) 
	private String numMaster;

	@JoinColumn(name = "ID_ROTA", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblRota")
	private Rota rota;
	
	
	@Column(name = "NUM_SEQ" )
	private Integer numSequencia;

	@Column(name = "ANO_SEQ" )
	private Integer anoSequencia;
	
	@JoinColumn(name = "ID_FILIAL", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@Label("lblFilial")
	private Filial filial;
	
	@JoinColumn( name = "ID_PROC_BROKER", nullable = true )
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcBroker procBroker;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_PREVISTA_ENTREGA")
	@Label("lblDtPrevista")
	private Date dtPrevista;
	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_ATUALIZACAO")
	private Date lastAtualizacao;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_CHEGADA")
	private Date dtChegada;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_CONSOLIDACAO", nullable = false)
	@Label("lblDtConsolidacao")
	private Date dtConsolidacao;

	@Label("lblObservacao")
	@Column(name = "OBSERVACAO", length = 80)
	private String observacao;

	@Label("lblAps")
	@Column(name = "NUM_APS", length = 6)
	private String numAPS;

	@NotNull(message = "notnull")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_AGENTE_CARGA")
	private PessoaJuridica agenteCarga;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_IMPORTADOR")
	private Usuario importador;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CIDADE_ATUAL")
	private Cidade cidadeAtual;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CANAL")
	private ParametroCanal canal;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	@Label("lblStatus")
	private StatusCarga status;

	@NotNull(message = "notnull")
	@Column(name = "CRITICO")
	private boolean critico;

	@OneToMany(mappedBy = "carga", fetch = FetchType.LAZY)
	@OrderBy(value = "numeroInvoice")
	private List<Invoice> listaDeInvoices;
	
	@OrderBy(value = "id")
	@OneToMany(mappedBy = "carga", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<CustoDI> custosDi;
	
	// para facilitar a consulta no relatorio de quality growth
	@OneToMany(mappedBy = "carga", fetch = FetchType.LAZY)
	private List<Ocorrencia> listaDeOcorrencias;

	@OneToMany(mappedBy = "carga", fetch = FetchType.LAZY)
	private Set<FollowUpImport> listaDeFollowUps = new HashSet<FollowUpImport>();

	// atributo que diz se carga aparece ou nao em painel...
	//@NotNull(message = "notnull")
	@Column(name = "VISIVEL")
	@Label("lblVisivel")
	private Boolean visivel;
	
	// atributo que diz se carga aparece ou nao em painel...
	//@NotNull(message = "notnull")
	@Column(name = "CONFIRMADO")
	@Label("lblConfirmado")
	private Boolean confirmado;
	
	@Column( name = "NUM_DI", length = 20)
	private String numeroDi;
	
	@Column( name = "DT_REGISTRO_DI")
	@Temporal(TemporalType.DATE)
	private Date dtRegistroDi;

	@Column(name = "PESO_HAWB", scale = 5, precision = 11)
	@Label("lblPesoBrutoHawb")
	private BigDecimal pesoBrutoHawb;
	
	@Column(name = "PESO_CUBADO_HAWB", scale = 5, precision = 11)
	@Label("lblPesoCubadoHawb")
	private BigDecimal pesoCubadoHawb;
	
	@Column(name = "VALOR_SEC", scale = 5, precision = 11)
	private BigDecimal valorSEC;
	
	@Column(name = "VALOR_PESO_IMPOSTO", scale = 5, precision = 11)
	private BigDecimal valorPesoImposto;
	
	@Column(name = "VALOR_TX_FRETE", scale = 5, precision = 11)
	private BigDecimal valorTaxaFrete;
	
	@Column(name = "VALOR_FSC", scale = 5, precision = 11)
	private BigDecimal valorFSC;
	
	@Column(name = "VALOR_ISS", scale = 5, precision = 11)
	private BigDecimal valorISS;
	
	@Column(name = "VALOR_AMS", scale = 5, precision = 11)
	private BigDecimal valorAMS;
	
	@Column(name = "VALOR_PICK_UP", scale = 5, precision = 11)
	private BigDecimal valorPickUp;
	
	@Column(name = "VALOR_PSS", scale = 5, precision = 11)
	private BigDecimal valorPSS;
	
	@Column(name = "VALOR_FRETE", scale = 5, precision = 11)
	private BigDecimal valorFrete;
	
	@Column( name = "OTD" )
	private Integer otd;
	
	
	@Transient
	private String processo;
	
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

	public Date getDtColeta() {
		return dtColeta;
	}

	public void setDtColeta(Date dtColeta) {
		this.dtColeta = dtColeta;
	}

	public String getHawb() {
		return hawb;
	}

	public void setHawb(String hawb) {
		this.hawb = hawb;
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public Date getDtPrevista() {
		return dtPrevista;
	}

	public void setDtPrevista(Date dtPrevista) {
		this.dtPrevista = dtPrevista;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Carga))
			return false;
		Carga other = (Carga) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Date getDtConsolidacao() {
		return dtConsolidacao;
	}

	public void setDtConsolidacao(Date dtConsolidacao) {
		this.dtConsolidacao = dtConsolidacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public PessoaJuridica getAgenteCarga() {
		return agenteCarga;
	}

	public void setAgenteCarga(PessoaJuridica agenteCarga) {
		this.agenteCarga = agenteCarga;
	}

	public StatusCarga getStatus() {
		return status;
	}

	public void setStatus(StatusCarga status) {
		this.status = status;
	}

	public String getNumAPS() {
		return numAPS;
	}

	public void setNumAPS(String numAPS) {
		this.numAPS = numAPS;
	}

	public String getSiglaCidadeAtual() {
		if ( cidadeAtual != null )
			return cidadeAtual.getSigla().toUpperCase();
		return "";
	}
	
	public Cidade getCidadeAtual() {
		return cidadeAtual;
	}

	public void setCidadeAtual(Cidade cidadeAtual) {
		this.cidadeAtual = cidadeAtual;
	}

	public List<Invoice> getListaDeInvoices() {
		return listaDeInvoices;
	}

	public void setListaDeInvoices(List<Invoice> listaDeInvoices) {
		this.listaDeInvoices = listaDeInvoices;
	}

	public Date getDtChegada() {
		return dtChegada;
	}

	public void setDtChegada(Date dtChegada) {
		this.dtChegada = dtChegada;
	}

	public ParametroCanal getCanal() {
		return canal;
	}

	public void setCanal(ParametroCanal canal) {
		this.canal = canal;
	}

	public boolean isCritico() {
		return critico;
	}

	public void setCritico(boolean critico) {
		this.critico = critico;
	}

	public Usuario getImportador() {
		return importador;
	}

	public void setImportador(Usuario importador) {
		this.importador = importador;
	}

	public Boolean getVisivel() {
		return visivel;
	}

	public void setVisivel(Boolean visivel) {
		this.visivel = visivel;
	}

	public String getNumeroDi() {
		return numeroDi;
	}

	public void setNumeroDi(String numeroDi) {
		this.numeroDi = numeroDi;
	}

	public Date getDtRegistroDi() {
		return dtRegistroDi;
	}

	public void setDtRegistroDi(Date dtRegistroDi) {
		this.dtRegistroDi = dtRegistroDi;
	}

	public Boolean getConfirmado() {
		return confirmado;
	}

	public void setConfirmado(Boolean confirmado) {
		this.confirmado = confirmado;
	}

	/**
	 * @param listaDeOcorrencias the listaDeOcorrencias to set
	 */
	public void setListaDeOcorrencias(List<Ocorrencia> listaDeOcorrencias) {
		this.listaDeOcorrencias = listaDeOcorrencias;
	}

	/**
	 * @return the listaDeOcorrencias
	 */
	public List<Ocorrencia> getListaDeOcorrencias() {
		return listaDeOcorrencias;
	}

	public BigDecimal getPesoBrutoHawb() {
		return pesoBrutoHawb;
	}

	public void setPesoBrutoHawb(BigDecimal pesoBrutoHawb) {
		this.pesoBrutoHawb = pesoBrutoHawb;
	}

	public BigDecimal getPesoCubadoHawb() {
		return pesoCubadoHawb;
	}

	public void setPesoCubadoHawb(BigDecimal pesoCubadoHawb) {
		this.pesoCubadoHawb = pesoCubadoHawb;
	}

	/**
	 * @return the listaDeFollowUps
	 */
	public Set<FollowUpImport> getListaDeFollowUps() {
		return listaDeFollowUps;
	}

	/**
	 * @param listaDeFollowUps the listaDeFollowUps to set
	 */
	public void setListaDeFollowUps(Set<FollowUpImport> listaDeFollowUps) {
		this.listaDeFollowUps = listaDeFollowUps;
	}

	public Integer getOtd() {
		return otd;
	}

	public void setOtd(Integer otd) {
		this.otd = otd;
	}

	/**
	 * @return the filial
	 */
	public Filial getFilial() {
		return filial;
	}

	/**
	 * @param filial the filial to set
	 */
	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	/**
	 * @return the numSequencia
	 */
	public Integer getNumSequencia() {
		return numSequencia;
	}

	/**
	 * @param numSequencia the numSequencia to set
	 */
	public void setNumSequencia(Integer numSequencia) {
		this.numSequencia = numSequencia;
	}

	/**
	 * @return the anoSequencia
	 */
	public Integer getAnoSequencia() {
		return anoSequencia;
	}

	/**
	 * @param anoSequencia the anoSequencia to set
	 */
	public void setAnoSequencia(Integer anoSequencia) {
		this.anoSequencia = anoSequencia;
	}




	/**
	 * @return
	 */
	public String getProcesso() {
		processo =  "";
		
		if ( filial != null ) {
			processo = filial.getCodigo();
		} else {
			processo = "XX";
		}
		
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




	/**
	 * @return the numMaster
	 */
	public String getNumMaster() {
		return numMaster;
	}




	/**
	 * @param numMaster the numMaster to set
	 */
	public void setNumMaster(String numMaster) {
		this.numMaster = numMaster;
	}




	/**
	 * @return the valorSEC
	 */
	public BigDecimal getValorSEC() {
		return valorSEC;
	}




	/**
	 * @param valorSEC the valorSEC to set
	 */
	public void setValorSEC(BigDecimal valorSEC) {
		this.valorSEC = valorSEC;
	}




	/**
	 * @return the valorPesoImposto
	 */
	public BigDecimal getValorPesoImposto() {
		return valorPesoImposto;
	}




	/**
	 * @param valorPesoImposto the valorPesoImposto to set
	 */
	public void setValorPesoImposto(BigDecimal valorPesoImposto) {
		this.valorPesoImposto = valorPesoImposto;
	}




	/**
	 * @return the valorTaxaFrete
	 */
	public BigDecimal getValorTaxaFrete() {
		return valorTaxaFrete;
	}




	/**
	 * @param valorTaxaFrete the valorTaxaFrete to set
	 */
	public void setValorTaxaFrete(BigDecimal valorTaxaFrete) {
		this.valorTaxaFrete = valorTaxaFrete;
	}




	/**
	 * @return the valorFSC
	 */
	public BigDecimal getValorFSC() {
		return valorFSC;
	}




	/**
	 * @param valorFSC the valorFSC to set
	 */
	public void setValorFSC(BigDecimal valorFSC) {
		this.valorFSC = valorFSC;
	}




	/**
	 * @return the valorISS
	 */
	public BigDecimal getValorISS() {
		return valorISS;
	}




	/**
	 * @param valorISS the valorISS to set
	 */
	public void setValorISS(BigDecimal valorISS) {
		this.valorISS = valorISS;
	}




	/**
	 * @return the valorAMS
	 */
	public BigDecimal getValorAMS() {
		return valorAMS;
	}




	/**
	 * @param valorAMS the valorIMS to set
	 */
	public void setValorAMS(BigDecimal valorAMS) {
		this.valorAMS = valorAMS;
	}




	/**
	 * @return the valorPickUp
	 */
	public BigDecimal getValorPickUp() {
		return valorPickUp;
	}




	/**
	 * @param valorPickUp the valorPickUp to set
	 */
	public void setValorPickUp(BigDecimal valorPickUp) {
		this.valorPickUp = valorPickUp;
	}




	/**
	 * @return the valorPSS
	 */
	public BigDecimal getValorPSS() {
		return valorPSS;
	}




	/**
	 * @param valorPSS the valorPSS to set
	 */
	public void setValorPSS(BigDecimal valorPSS) {
		this.valorPSS = valorPSS;
	}




	/**
	 * @return the valorFrete
	 */
	public BigDecimal getValorFrete() {
		return valorFrete;
	}




	/**
	 * @param valorFrete the valorFrete to set
	 */
	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
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
	 * @return the custosDi
	 */
	public List<CustoDI> getCustosDi() {
		return custosDi;
	}




	/**
	 * @param custosDi the custosDi to set
	 */
	public void setCustosDi(List<CustoDI> custosDi) {
		this.custosDi = custosDi;
	}




	public Date getLastAtualizacao() {
		return lastAtualizacao;
	}




	public void setLastAtualizacao(Date lastAtualizacao) {
		this.lastAtualizacao = lastAtualizacao;
	}

}
