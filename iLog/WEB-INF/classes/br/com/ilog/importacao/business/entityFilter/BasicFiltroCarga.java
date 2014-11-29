package br.com.ilog.importacao.business.entityFilter;

import java.io.Serializable;
import java.util.Date;

import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entity.Invoice;
import br.com.ilog.importacao.business.entity.ItemInvoice;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.seguranca.business.entity.Usuario;

/**
 * 
 * @author Heber Santiago
 *
 */
public class BasicFiltroCarga implements Serializable {

	private static final long serialVersionUID = 1L;

	private String numMaster;
	
	private String numberPart;
	
	private String processo;
	
	private String hawb;
	
	private Rota rota;
	
	private String numeroDI;
	
	/**
	 * data prevista da coleta
	 */
	private Date dtPrevista;
	
	/**
	 * data de criacao do planejamento
	 */
	private Date dtConsolidacao;
	private PessoaJuridica agenteCarga;
	private StatusCarga status;
	private StatusInvoice statusInvoice;
	private Carga carga;
	
	private Boolean critico;
	
	private Usuario responsavel;
	//
	private PessoaJuridica exportador;
	private Usuario comprador;
	private Usuario importador;
	private Invoice invoice;
	private String numInvoice;
	private String numPO;
	private PessoaJuridica agente;
    private String aps;
    
    private Date dtInicioColeta;
    private Date dtFimColeta;
    
    private Date dtInicioConsolidacao;
    private Date dtFimConsolidacao;
    
    private Date dtInicioPrevista;
    private Date dtFimPrevista;
    
    private Date dtInicioChegada;
    private Date dtFimChegada;
    
    private ItemInvoice itemInvoice;
    private Cidade cidadeAtual;
    
    private FollowUpImportTrecho followUpImportTrecho;
	
    private ParametroCanal canal;
    
    //filto para visivel em carga
    private Boolean visivel;
    
    private Boolean ativo;
    
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

	public Date getDtConsolidacao() {
		return dtConsolidacao;
	}

	public void setDtConsolidacao(Date dtConsolidacao) {
		this.dtConsolidacao = dtConsolidacao;
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

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public PessoaJuridica getAgente() {
		return agente;
	}

	public void setAgente(PessoaJuridica agente) {
		this.agente = agente;
	}

	public String getAps() {
		return aps;
	}

	public void setAps(String aps) {
		this.aps = aps;
	}

	public Date getDtInicioColeta() {
		return dtInicioColeta;
	}

	public void setDtInicioColeta(Date dtInicioColeta) {
		this.dtInicioColeta = dtInicioColeta;
	}

	public Date getDtFimColeta() {
		return dtFimColeta;
	}

	public void setDtFimColeta(Date dtFimColeta) {
		this.dtFimColeta = dtFimColeta;
	}

	public Date getDtInicioConsolidacao() {
		return dtInicioConsolidacao;
	}

	public void setDtInicioConsolidacao(Date dtInicioConsolidacao) {
		this.dtInicioConsolidacao = dtInicioConsolidacao;
	}

	public Date getDtFimConsolidacao() {
		return dtFimConsolidacao;
	}

	public void setDtFimConsolidacao(Date dtFimConsolidacao) {
		this.dtFimConsolidacao = dtFimConsolidacao;
	}

	public ItemInvoice getItemInvoice() {
		return itemInvoice;
	}

	public void setItemInvoice(ItemInvoice itemInvoice) {
		this.itemInvoice = itemInvoice;
	}

	public String getNumInvoice() {
		return numInvoice;
	}

	public void setNumInvoice(String numInvoice) {
		this.numInvoice = numInvoice;
	}

	public String getNumPO() {
		return numPO;
	}

	public void setNumPO(String numPO) {
		this.numPO = numPO;
	}

	public Cidade getCidadeAtual() {
		return cidadeAtual;
	}

	public void setCidadeAtual(Cidade cidadeAtual) {
		this.cidadeAtual = cidadeAtual;
	}

	public Date getDtInicioPrevista() {
		return dtInicioPrevista;
	}

	public void setDtInicioPrevista(Date dtInicioPrevista) {
		this.dtInicioPrevista = dtInicioPrevista;
	}

	public Date getDtFimPrevista() {
		return dtFimPrevista;
	}

	public void setDtFimPrevista(Date dtFimPrevista) {
		this.dtFimPrevista = dtFimPrevista;
	}

	public ParametroCanal getCanal() {
		return canal;
	}

	public void setCanal(ParametroCanal canal) {
		this.canal = canal;
	}

	public FollowUpImportTrecho getFollowUpImportTrecho() {
		return followUpImportTrecho;
	}

	public void setFollowUpImportTrecho(FollowUpImportTrecho followUpImportTrecho) {
		this.followUpImportTrecho = followUpImportTrecho;
	}

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	
	public PessoaJuridica getExportador() {
		return exportador;
	}

	public void setExportador(PessoaJuridica exportador) {
		this.exportador = exportador;
	}

	public Usuario getComprador() {
		return comprador;
	}

	public void setComprador(Usuario comprador) {
		this.comprador = comprador;
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

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the ativo
	 */
	public Boolean getAtivo() {
		return ativo;
	}

	public Boolean getCritico() {
		return critico;
	}

	public void setCritico(Boolean critico) {
		this.critico = critico;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public Date getDtInicioChegada() {
		return dtInicioChegada;
	}

	public void setDtInicioChegada(Date dtInicioChegada) {
		this.dtInicioChegada = dtInicioChegada;
	}

	public Date getDtFimChegada() {
		return dtFimChegada;
	}

	public void setDtFimChegada(Date dtFimChegada) {
		this.dtFimChegada = dtFimChegada;
	}

	public StatusInvoice getStatusInvoice() {
		return statusInvoice;
	}

	public void setStatusInvoice(StatusInvoice statusInvoice) {
		this.statusInvoice = statusInvoice;
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
	 * @return the processo
	 */
	public String getProcesso() {
		return processo;
	}

	/**
	 * @param processo the processo to set
	 */
	public void setProcesso(String processo) {
		this.processo = processo;
	}

	/**
	 * @return the numeroDI
	 */
	public String getNumeroDI() {
		return numeroDI;
	}

	/**
	 * @param numeroDI the numeroDI to set
	 */
	public void setNumeroDI(String numeroDI) {
		this.numeroDI = numeroDI;
	}

	public String getNumberPart() {
		return numberPart;
	}

	public void setNumberPart(String numberPart) {
		this.numberPart = numberPart;
	}

	
}
