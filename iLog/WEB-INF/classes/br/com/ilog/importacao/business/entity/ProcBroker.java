package br.com.ilog.importacao.business.entity;

import java.math.BigDecimal;
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
import br.com.ilog.cadastro.business.entity.Incoterm;

@Entity
@Table( name = "PROC_BROKER" )
public class ProcBroker implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 5943263701992310799L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "proc_broker_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@Column( name = "DESPACHANTE" )
	private Integer despachante;
	
	@Column( name = "CNPJ_FOXCONN", length = 14 )
	private String cnpjFoxconn;
	
	@Column( name = "NR_DI", length = 12 )
	private String nrDI;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_DI")
	private Date dataDI;
	
	@Column( name = "TIPO_TRANSPORTE", length = 1 )
	private String tipoTransporte;
	
	@JoinColumn(name = "INCOTERMS", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Incoterm incoterm;
	
	@Column( name = "CD_MOEDA_FOB" )
	private Integer cdMoedaFOB;
	
	@Column(name = "PESO_LIQ_TOTAL", scale = 3, precision = 11)
	private BigDecimal pesoLiqTotal;
	
	@Column(name = "PESO_BRUTO_TOTAL", scale = 3, precision = 11)
	private BigDecimal pesoBrutoTotal;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_PLI")
	private Date dataPLI;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_LI")
	private Date dataLI;
	
	@Column( name = "TIPO_DECLARACAO")
	private Integer tipoDeclaracao;
	
	@Column( name = "UFR_ENTRADA")
	private Integer ufrEntrada;
	
	@Column( name = "UFR_DESPACHO")
	private Integer ufrDespacho;
	
	@Column( name = "HAWB", length = 20 )
	private String hawb;
	
	@Column( name = "TIPO_VOLUME")
	private Long tipoVolume;
	
	@Column( name = "QTD_VOLUME", scale = 4, precision = 16)
	private BigDecimal qtdVolume;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_CHEGADA_ETA" )
	private Date dtChegadaETA;
	
	@Column( name = "NUM_MASTER" ) 
	private String numMaster;
	
	@Column( name = "LOCAL_EMBARQUE", length = 20 )
	private String localEmbarque;
	
	@Column( name = "NOME_TRANSPORTADOR", length = 20 )
	private String nomeTransportador;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_EMBARQUE_ETD" )
	private Date dtEmbarqueETD;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_MANTRA" )
	private Date dtMantra;
	
	@Column( name = "CANAL", length = 1 )
	private String canal;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_PARAMETRIZACAO" )
	private Date dtParametrizacao;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_DISTRIBUICAO" )
	private Date dtDistribuicao;
	
	@Column( name = "NOME_FISCAL", length = 20 )
	private String nomeFiscal;
	
	@Column( name = "TRANSP_URBANO", length = 20 )
	private String transpUrbano;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_ENTREGA" )
	private Date dtEntrega;
	
	@Column(name = "VL_FOB_LOC_EMBARQUE", scale = 4, precision = 16)
	private BigDecimal vlFOBLocEmbarque;
	
	@Column(name = "VL_FRETE_PREPAID", scale = 4, precision = 16)
	private BigDecimal vlFretePrepaid;
	
	@Column(name = "VL_FRETE_COLECT", scale = 4, precision = 16)
	private BigDecimal vlFreteColect;
	
	@Column(name = "CD_MOEDA_FRETE")
	private Long cdMoedaFrete;
	
	@Column(name = "VL_TAXA_FRETE", scale = 4, precision = 16)
	private BigDecimal vlTaxaFrete;
	
	@Column(name = "VL_SEGURO_NEGOCIADA", scale = 4, precision = 16)
	private BigDecimal vlSeguroNegociada;
	
	@Column(name = "CD_MOEDA_SEGURO" )
	private Long cdMoedaSeguro;
	
	@Column(name = "VL_TAXA_SEGURO", scale = 4, precision = 16)
	private BigDecimal vlTaxaSeguro;
	
	@Column(name = "VL_II", scale = 4, precision = 16)
	private BigDecimal vlII;
	
	@Column(name = "VL_IPI", scale = 4, precision = 16)
	private BigDecimal vlIPI;
	
	@Column(name = "VL_PIS", scale = 4, precision = 16)
	private BigDecimal vlPIS;
	
	@Column(name = "VL_CONFINS", scale = 4, precision = 16)
	private BigDecimal vlConfins;
	
	@Column(name = "VL_TAXA_SISCOMEX", scale = 4, precision = 16)
	private BigDecimal vlTaxaSiscomex;
	
	@Column(name = "VL_TAXA_SUFRAMA", scale = 4, precision = 16)
	private BigDecimal vlTaxaSuframa;
	
	@Column(name = "VL_MULTA", scale = 4, precision = 16)
	private BigDecimal vlMulta;
	
	@Column(name = "VL_TAXA_FOB", scale = 4, precision = 16)
	private BigDecimal vlTaxaFOB;
	
	@Column( name = "RECINTO", length = 20 )
	private String recinto;
	
	@Column(name = "VL_ARMAZENAGEM", scale = 4, precision = 16)
	private BigDecimal vlArmazenagem;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_ARMAZENAGEM" )
	private Date dtArmazenagem;
	
	@Column(name = "VL_CAPATAZIA", scale = 4, precision = 16)
	private BigDecimal vlCapatazia;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_CAPATAZIA" )
	private Date dtCapatazia;
	
	@Column(name = "VL_DESOVA", scale = 4, precision = 16)
	private BigDecimal vlDesova;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_DESOVA" )
	private Date dtDesova;
	
	@Column(name = "VL_COMISSAO_DESPACHANTE", scale = 4, precision = 16)
	private BigDecimal vlComissaoDespachante;
	
	@Column( name = "CD_PAIS" )
	private Long cdPais;
	
	@Column(name = "VL_TAXA_DI_USD", scale = 4, precision = 16)
	private BigDecimal vlTaxaDiUsd;
	
	@OneToMany(mappedBy = "procBroker", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<ProcItem> itens;
	
	@OneToMany(mappedBy = "procBroker", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<ProcCarga> cargas;
	
	@OneToMany(mappedBy = "procBroker", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<ProcInvoice> invoices;
	
	@OneToMany(mappedBy = "procBroker", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<ProcStatus> status;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "procBroker")
	private List<Carga> cargasImport;
	
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
	 * @return the cnpjFoxconn
	 */
	public String getCnpjFoxconn() {
		return cnpjFoxconn;
	}

	/**
	 * @param cnpjFoxconn the cnpjFoxconn to set
	 */
	public void setCnpjFoxconn(String cnpjFoxconn) {
		this.cnpjFoxconn = cnpjFoxconn;
	}

	/**
	 * @return the nrDI
	 */
	public String getNrDI() {
		return nrDI;
	}

	/**
	 * @param nrDI the nrDI to set
	 */
	public void setNrDI(String nrDI) {
		this.nrDI = nrDI;
	}

	/**
	 * @return the dataDI
	 */
	public Date getDataDI() {
		return dataDI;
	}

	/**
	 * @param dataDI the dataDI to set
	 */
	public void setDataDI(Date dataDI) {
		this.dataDI = dataDI;
	}

	/**
	 * @return the tipoTransporte
	 */
	public String getTipoTransporte() {
		return tipoTransporte;
	}

	/**
	 * @param tipoTransporte the tipoTransporte to set
	 */
	public void setTipoTransporte(String tipoTransporte) {
		this.tipoTransporte = tipoTransporte;
	}

	/**
	 * @return the incoterm
	 */
	public Incoterm getIncoterm() {
		return incoterm;
	}

	/**
	 * @param incoterm the incoterm to set
	 */
	public void setIncoterm(Incoterm incoterm) {
		this.incoterm = incoterm;
	}

	/**
	 * @return the cdMoedaFOB
	 */
	public Integer getCdMoedaFOB() {
		return cdMoedaFOB;
	}

	/**
	 * @param cdMoedaFOB the cdMoedaFOB to set
	 */
	public void setCdMoedaFOB(Integer cdMoedaFOB) {
		this.cdMoedaFOB = cdMoedaFOB;
	}

	/**
	 * @return the pesoLiqTotal
	 */
	public BigDecimal getPesoLiqTotal() {
		return pesoLiqTotal;
	}

	/**
	 * @param pesoLiqTotal the pesoLiqTotal to set
	 */
	public void setPesoLiqTotal(BigDecimal pesoLiqTotal) {
		this.pesoLiqTotal = pesoLiqTotal;
	}

	/**
	 * @return the pesoBrutoTotal
	 */
	public BigDecimal getPesoBrutoTotal() {
		return pesoBrutoTotal;
	}

	/**
	 * @param pesoBrutoTotal the pesoBrutoTotal to set
	 */
	public void setPesoBrutoTotal(BigDecimal pesoBrutoTotal) {
		this.pesoBrutoTotal = pesoBrutoTotal;
	}

	/**
	 * @return the dataPLI
	 */
	public Date getDataPLI() {
		return dataPLI;
	}

	/**
	 * @param dataPLI the dataPLI to set
	 */
	public void setDataPLI(Date dataPLI) {
		this.dataPLI = dataPLI;
	}

	/**
	 * @return the dataLI
	 */
	public Date getDataLI() {
		return dataLI;
	}

	/**
	 * @param dataLI the dataLI to set
	 */
	public void setDataLI(Date dataLI) {
		this.dataLI = dataLI;
	}

	/**
	 * @return the tipoDeclaracao
	 */
	public Integer getTipoDeclaracao() {
		return tipoDeclaracao;
	}

	/**
	 * @param tipoDeclaracao the tipoDeclaracao to set
	 */
	public void setTipoDeclaracao(Integer tipoDeclaracao) {
		this.tipoDeclaracao = tipoDeclaracao;
	}

	/**
	 * @return the ufrEntrada
	 */
	public Integer getUfrEntrada() {
		return ufrEntrada;
	}

	/**
	 * @param ufrEntrada the ufrEntrada to set
	 */
	public void setUfrEntrada(Integer ufrEntrada) {
		this.ufrEntrada = ufrEntrada;
	}

	/**
	 * @return the ufrDespacho
	 */
	public Integer getUfrDespacho() {
		return ufrDespacho;
	}

	/**
	 * @param ufrDespacho the ufrDespacho to set
	 */
	public void setUfrDespacho(Integer ufrDespacho) {
		this.ufrDespacho = ufrDespacho;
	}

	/**
	 * @return the hawb
	 */
	public String getHawb() {
		return hawb;
	}

	/**
	 * @param hawb the hawb to set
	 */
	public void setHawb(String hawb) {
		this.hawb = hawb;
	}

	/**
	 * @return the tipoVolume
	 */
	public Long getTipoVolume() {
		return tipoVolume;
	}

	/**
	 * @param tipoVolume the tipoVolume to set
	 */
	public void setTipoVolume(Long tipoVolume) {
		this.tipoVolume = tipoVolume;
	}

	/**
	 * @return the qtdVolume
	 */
	public BigDecimal getQtdVolume() {
		return qtdVolume;
	}

	/**
	 * @param qtdVolume the qtdVolume to set
	 */
	public void setQtdVolume(BigDecimal qtdVolume) {
		this.qtdVolume = qtdVolume;
	}

	/**
	 * @return the dtChegadaETA
	 */
	public Date getDtChegadaETA() {
		return dtChegadaETA;
	}

	/**
	 * @param dtChegadaETA the dtChegadaETA to set
	 */
	public void setDtChegadaETA(Date dtChegadaETA) {
		this.dtChegadaETA = dtChegadaETA;
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
	 * @return the localEmbarque
	 */
	public String getLocalEmbarque() {
		return localEmbarque;
	}

	/**
	 * @param localEmbarque the localEmbarque to set
	 */
	public void setLocalEmbarque(String localEmbarque) {
		this.localEmbarque = localEmbarque;
	}

	/**
	 * @return the nomeTransportador
	 */
	public String getNomeTransportador() {
		return nomeTransportador;
	}

	/**
	 * @param nomeTransportador the nomeTransportador to set
	 */
	public void setNomeTransportador(String nomeTransportador) {
		this.nomeTransportador = nomeTransportador;
	}

	/**
	 * @return the dtEmbarqueETD
	 */
	public Date getDtEmbarqueETD() {
		return dtEmbarqueETD;
	}

	/**
	 * @param dtEmbarqueETD the dtEmbarqueETD to set
	 */
	public void setDtEmbarqueETD(Date dtEmbarqueETD) {
		this.dtEmbarqueETD = dtEmbarqueETD;
	}

	/**
	 * @return the dtMantra
	 */
	public Date getDtMantra() {
		return dtMantra;
	}

	/**
	 * @param dtMantra the dtMantra to set
	 */
	public void setDtMantra(Date dtMantra) {
		this.dtMantra = dtMantra;
	}

	/**
	 * @return the canal
	 */
	public String getCanal() {
		return canal;
	}

	/**
	 * @param canal the canal to set
	 */
	public void setCanal(String canal) {
		this.canal = canal;
	}

	/**
	 * @return the dtParametrizacao
	 */
	public Date getDtParametrizacao() {
		return dtParametrizacao;
	}

	/**
	 * @param dtParametrizacao the dtParametrizacao to set
	 */
	public void setDtParametrizacao(Date dtParametrizacao) {
		this.dtParametrizacao = dtParametrizacao;
	}

	/**
	 * @return the dtDistribuicao
	 */
	public Date getDtDistribuicao() {
		return dtDistribuicao;
	}

	/**
	 * @param dtDistribuicao the dtDistribuicao to set
	 */
	public void setDtDistribuicao(Date dtDistribuicao) {
		this.dtDistribuicao = dtDistribuicao;
	}

	/**
	 * @return the nomeFiscal
	 */
	public String getNomeFiscal() {
		return nomeFiscal;
	}

	/**
	 * @param nomeFiscal the nomeFiscal to set
	 */
	public void setNomeFiscal(String nomeFiscal) {
		this.nomeFiscal = nomeFiscal;
	}

	/**
	 * @return the transpUrbano
	 */
	public String getTranspUrbano() {
		return transpUrbano;
	}

	/**
	 * @param transpUrbano the transpUrbano to set
	 */
	public void setTranspUrbano(String transpUrbano) {
		this.transpUrbano = transpUrbano;
	}

	/**
	 * @return the dtEntrega
	 */
	public Date getDtEntrega() {
		return dtEntrega;
	}

	/**
	 * @param dtEntrega the dtEntrega to set
	 */
	public void setDtEntrega(Date dtEntrega) {
		this.dtEntrega = dtEntrega;
	}

	/**
	 * @return the vlFOBLocEmbarque
	 */
	public BigDecimal getVlFOBLocEmbarque() {
		return vlFOBLocEmbarque;
	}

	/**
	 * @param vlFOBLocEmbarque the vlFOBLocEmbarque to set
	 */
	public void setVlFOBLocEmbarque(BigDecimal vlFOBLocEmbarque) {
		this.vlFOBLocEmbarque = vlFOBLocEmbarque;
	}

	/**
	 * @return the vlFretePrepaid
	 */
	public BigDecimal getVlFretePrepaid() {
		return vlFretePrepaid;
	}

	/**
	 * @param vlFretePrepaid the vlFretePrepaid to set
	 */
	public void setVlFretePrepaid(BigDecimal vlFretePrepaid) {
		this.vlFretePrepaid = vlFretePrepaid;
	}

	/**
	 * @return the vlFreteColect
	 */
	public BigDecimal getVlFreteColect() {
		return vlFreteColect;
	}

	/**
	 * @param vlFreteColect the vlFreteColect to set
	 */
	public void setVlFreteColect(BigDecimal vlFreteColect) {
		this.vlFreteColect = vlFreteColect;
	}

	/**
	 * @return the cdMoedaFrete
	 */
	public Long getCdMoedaFrete() {
		return cdMoedaFrete;
	}

	/**
	 * @param cdMoedaFrete the cdMoedaFrete to set
	 */
	public void setCdMoedaFrete(Long cdMoedaFrete) {
		this.cdMoedaFrete = cdMoedaFrete;
	}

	/**
	 * @return the vlTaxaFrete
	 */
	public BigDecimal getVlTaxaFrete() {
		return vlTaxaFrete;
	}

	/**
	 * @param vlTaxaFrete the vlTaxaFrete to set
	 */
	public void setVlTaxaFrete(BigDecimal vlTaxaFrete) {
		this.vlTaxaFrete = vlTaxaFrete;
	}

	/**
	 * @return the vlSeguroNegociada
	 */
	public BigDecimal getVlSeguroNegociada() {
		return vlSeguroNegociada;
	}

	/**
	 * @param vlSeguroNegociada the vlSeguroNegociada to set
	 */
	public void setVlSeguroNegociada(BigDecimal vlSeguroNegociada) {
		this.vlSeguroNegociada = vlSeguroNegociada;
	}

	/**
	 * @return the cdMoedaSeguro
	 */
	public Long getCdMoedaSeguro() {
		return cdMoedaSeguro;
	}

	/**
	 * @param cdMoedaSeguro the cdMoedaSeguro to set
	 */
	public void setCdMoedaSeguro(Long cdMoedaSeguro) {
		this.cdMoedaSeguro = cdMoedaSeguro;
	}

	/**
	 * @return the vlTaxaSeguro
	 */
	public BigDecimal getVlTaxaSeguro() {
		return vlTaxaSeguro;
	}

	/**
	 * @param vlTaxaSeguro the vlTaxaSeguro to set
	 */
	public void setVlTaxaSeguro(BigDecimal vlTaxaSeguro) {
		this.vlTaxaSeguro = vlTaxaSeguro;
	}

	/**
	 * @return the vlII
	 */
	public BigDecimal getVlII() {
		return vlII;
	}

	/**
	 * @param vlII the vlII to set
	 */
	public void setVlII(BigDecimal vlII) {
		this.vlII = vlII;
	}

	/**
	 * @return the vlIPI
	 */
	public BigDecimal getVlIPI() {
		return vlIPI;
	}

	/**
	 * @param vlIPI the vlIPI to set
	 */
	public void setVlIPI(BigDecimal vlIPI) {
		this.vlIPI = vlIPI;
	}

	/**
	 * @return the vlPIS
	 */
	public BigDecimal getVlPIS() {
		return vlPIS;
	}

	/**
	 * @param vlPIS the vlPIS to set
	 */
	public void setVlPIS(BigDecimal vlPIS) {
		this.vlPIS = vlPIS;
	}

	/**
	 * @return the vlConfins
	 */
	public BigDecimal getVlConfins() {
		return vlConfins;
	}

	/**
	 * @param vlConfins the vlConfins to set
	 */
	public void setVlConfins(BigDecimal vlConfins) {
		this.vlConfins = vlConfins;
	}

	/**
	 * @return the vlTaxaSiscomex
	 */
	public BigDecimal getVlTaxaSiscomex() {
		return vlTaxaSiscomex;
	}

	/**
	 * @param vlTaxaSiscomex the vlTaxaSiscomex to set
	 */
	public void setVlTaxaSiscomex(BigDecimal vlTaxaSiscomex) {
		this.vlTaxaSiscomex = vlTaxaSiscomex;
	}

	/**
	 * @return the vlTaxaSuframa
	 */
	public BigDecimal getVlTaxaSuframa() {
		return vlTaxaSuframa;
	}

	/**
	 * @param vlTaxaSuframa the vlTaxaSuframa to set
	 */
	public void setVlTaxaSuframa(BigDecimal vlTaxaSuframa) {
		this.vlTaxaSuframa = vlTaxaSuframa;
	}

	/**
	 * @return the vlMulta
	 */
	public BigDecimal getVlMulta() {
		return vlMulta;
	}

	/**
	 * @param vlMulta the vlMulta to set
	 */
	public void setVlMulta(BigDecimal vlMulta) {
		this.vlMulta = vlMulta;
	}

	/**
	 * @return the vlTaxaFOB
	 */
	public BigDecimal getVlTaxaFOB() {
		return vlTaxaFOB;
	}

	/**
	 * @param vlTaxaFOB the vlTaxaFOB to set
	 */
	public void setVlTaxaFOB(BigDecimal vlTaxaFOB) {
		this.vlTaxaFOB = vlTaxaFOB;
	}

	/**
	 * @return the recinto
	 */
	public String getRecinto() {
		return recinto;
	}

	/**
	 * @param recinto the recinto to set
	 */
	public void setRecinto(String recinto) {
		this.recinto = recinto;
	}

	/**
	 * @return the vlArmazenagem
	 */
	public BigDecimal getVlArmazenagem() {
		return vlArmazenagem;
	}

	/**
	 * @param vlArmazenagem the vlArmazenagem to set
	 */
	public void setVlArmazenagem(BigDecimal vlArmazenagem) {
		this.vlArmazenagem = vlArmazenagem;
	}

	/**
	 * @return the dtArmazenagem
	 */
	public Date getDtArmazenagem() {
		return dtArmazenagem;
	}

	/**
	 * @param dtArmazenagem the dtArmazenagem to set
	 */
	public void setDtArmazenagem(Date dtArmazenagem) {
		this.dtArmazenagem = dtArmazenagem;
	}

	/**
	 * @return the vlCapatazia
	 */
	public BigDecimal getVlCapatazia() {
		return vlCapatazia;
	}

	/**
	 * @param vlCapatazia the vlCapatazia to set
	 */
	public void setVlCapatazia(BigDecimal vlCapatazia) {
		this.vlCapatazia = vlCapatazia;
	}

	/**
	 * @return the dtCapatazia
	 */
	public Date getDtCapatazia() {
		return dtCapatazia;
	}

	/**
	 * @param dtCapatazia the dtCapatazia to set
	 */
	public void setDtCapatazia(Date dtCapatazia) {
		this.dtCapatazia = dtCapatazia;
	}

	/**
	 * @return the vlDesova
	 */
	public BigDecimal getVlDesova() {
		return vlDesova;
	}

	/**
	 * @param vlDesova the vlDesova to set
	 */
	public void setVlDesova(BigDecimal vlDesova) {
		this.vlDesova = vlDesova;
	}

	/**
	 * @return the dtDesova
	 */
	public Date getDtDesova() {
		return dtDesova;
	}

	/**
	 * @param dtDesova the dtDesova to set
	 */
	public void setDtDesova(Date dtDesova) {
		this.dtDesova = dtDesova;
	}

	/**
	 * @return the vlComissaoDespachante
	 */
	public BigDecimal getVlComissaoDespachante() {
		return vlComissaoDespachante;
	}

	/**
	 * @param vlComissaoDespachante the vlComissaoDespachante to set
	 */
	public void setVlComissaoDespachante(BigDecimal vlComissaoDespachante) {
		this.vlComissaoDespachante = vlComissaoDespachante;
	}

	/**
	 * @return the cdPais
	 */
	public Long getCdPais() {
		return cdPais;
	}

	/**
	 * @param cdPais the cdPais to set
	 */
	public void setCdPais(Long cdPais) {
		this.cdPais = cdPais;
	}

	/**
	 * @return the vlTaxaDiUsd
	 */
	public BigDecimal getVlTaxaDiUsd() {
		return vlTaxaDiUsd;
	}

	/**
	 * @param vlTaxaDiUsd the vlTaxaDiUsd to set
	 */
	public void setVlTaxaDiUsd(BigDecimal vlTaxaDiUsd) {
		this.vlTaxaDiUsd = vlTaxaDiUsd;
	}

	/**
	 * @return the itens
	 */
	public List<ProcItem> getItens() {
		return itens;
	}

	/**
	 * @param itens the itens to set
	 */
	public void setItens(List<ProcItem> itens) {
		this.itens = itens;
	}

	/**
	 * @return the cargas
	 */
	public List<ProcCarga> getCargas() {
		return cargas;
	}

	/**
	 * @param cargas the cargas to set
	 */
	public void setCargas(List<ProcCarga> cargas) {
		this.cargas = cargas;
	}

	/**
	 * @return the invoices
	 */
	public List<ProcInvoice> getInvoices() {
		return invoices;
	}

	/**
	 * @param invoices the invoices to set
	 */
	public void setInvoices(List<ProcInvoice> invoices) {
		this.invoices = invoices;
	}

	/**
	 * @return the status
	 */
	public List<ProcStatus> getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(List<ProcStatus> status) {
		this.status = status;
	}

	/**
	 * @return the despachante
	 */
	public Integer getDespachante() {
		return despachante;
	}

	/**
	 * @param despachante the despachante to set
	 */
	public void setDespachante(Integer despachante) {
		this.despachante = despachante;
	}

	/**
	 * @return the cargasImport
	 */
	public List<Carga> getCargasImport() {
		return cargasImport;
	}

	/**
	 * @param cargasImport the cargasImport to set
	 */
	public void setCargasImport(List<Carga> cargasImport) {
		this.cargasImport = cargasImport;
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
		if (!(obj instanceof ProcBroker))
			return false;
		ProcBroker other = (ProcBroker) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
	
}

