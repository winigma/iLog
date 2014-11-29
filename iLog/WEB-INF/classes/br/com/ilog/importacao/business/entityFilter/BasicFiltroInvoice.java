package br.com.ilog.importacao.business.entityFilter;

import java.io.Serializable;
import java.util.Date;

import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.entity.Incoterm;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Projeto;
import br.com.ilog.cadastro.business.entity.Terminal;
import br.com.ilog.importacao.business.entity.StatusInvoice;
import br.com.ilog.seguranca.business.entity.Usuario;

/**
 * 
 * @author Wisley souza
 * @coment Classe auxiliar de filtragem de invoice
 * 
 */
public class BasicFiltroInvoice implements Serializable {

	private static final long serialVersionUID = -4248934232657757865L;

	/**
	 * numero da invoice
	 */
	private String numeroInvoice;
	
	private String numeroSeq;
	
	/**
	 * data de emissao da invoice
	 */
	private Date dtEmissao;
	/**
	 * fornecedor da invoice
	 */
	private PessoaJuridica fornecedor;
	
	/**
	 * filtro para comprador.
	 */
	private Usuario comprador;
	
	/**
	 * Origem.
	 */
	private Pais origem;
	
	/**
	 * Projeto.
	 */
	private Projeto projeto;
	
	private Filial filial;
	
	/**
	 * origem da invoice
	 */
	private Pais pais;
	/**
	 * status da da invoice
	 */
	private Boolean status;
	
	/**
	 * comprador, responsável pela compra da invoice
	 */
	private Usuario user;
	
	private Moeda moeda;
	
	private Modal modal;
	
	private Cidade cidadeAtual;
	
	private StatusInvoice statusInvoice;
	
	/**
	 * Terminal
	 */
	private Terminal terminal;
	
	private Date dataInicio;
	private Date dataFim;
	
	private Incoterm incoterm; 
	
	public String getNumeroInvoice() {
		return numeroInvoice;
	}
	public void setNumeroInvoice(String numeroInvoice) {
		this.numeroInvoice = numeroInvoice;
	}
	public Date getDtEmissao() {
		return dtEmissao;
	}
	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
	}
	public PessoaJuridica getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(PessoaJuridica fornecedor) {
		this.fornecedor = fornecedor;
	}
	public Pais getPais() {
		return pais;
	}
	public void setPais(Pais pais) {
		this.pais = pais;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Usuario getUser() {
		return user;
	}
	public void setUser(Usuario user) {
		this.user = user;
	}
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Date getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	public Usuario getComprador() {
		return comprador;
	}
	public void setComprador(Usuario comprador) {
		this.comprador = comprador;
	}
	public Pais getOrigem() {
		return origem;
	}
	public void setOrigem(Pais origem) {
		this.origem = origem;
	}
	public Projeto getProjeto() {
		return projeto;
	}
	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}
	public StatusInvoice getStatusInvoice() {
		return statusInvoice;
	}
	public void setStatusInvoice(StatusInvoice statusInvoice) {
		this.statusInvoice = statusInvoice;
	}
	public Cidade getCidadeAtual() {
		return cidadeAtual;
	}
	public void setCidadeAtual(Cidade cidadeAtual) {
		this.cidadeAtual = cidadeAtual;
	}
	public String getNumeroSeq() {
		return numeroSeq;
	}
	public void setNumeroSeq(String numeroSeq) {
		this.numeroSeq = numeroSeq;
	}
	public Terminal getTerminal() {
		return terminal;
	}
	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
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
	 * @return the moeda
	 */
	public Moeda getMoeda() {
		return moeda;
	}
	/**
	 * @param moeda the moeda to set
	 */
	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
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
	 * @return the modal
	 */
	public Modal getModal() {
		return modal;
	}
	/**
	 * @param modal the modal to set
	 */
	public void setModal(Modal modal) {
		this.modal = modal;
	}

}
