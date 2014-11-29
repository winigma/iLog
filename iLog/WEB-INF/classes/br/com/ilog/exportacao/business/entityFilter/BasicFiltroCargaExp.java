package br.com.ilog.exportacao.business.entityFilter;

import java.io.Serializable;
import java.util.Date;

import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.exportacao.business.entity.InvoiceExp;
import br.com.ilog.exportacao.business.entity.StatusCargaExp;
import br.com.ilog.exportacao.business.entity.StatusInvoice;

public class BasicFiltroCargaExp implements Serializable {

	private static final long serialVersionUID = 1L;

	private PessoaJuridica cliente;
	private Moeda moeda;
	private Modal modal;
	private String numInvoice;
	private StatusInvoice statusInvoice;
	private StatusCargaExp status;
	private Date dataInicio;
	private Date dataFim;
	private InvoiceExp invoiceNumero;
	private String danfe;

	public PessoaJuridica getCliente() {
		return cliente;
	}

	public void setCliente(PessoaJuridica cliente) {
		this.cliente = cliente;
	}

	public Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public Modal getModal() {
		return modal;
	}

	public void setModal(Modal modal) {
		this.modal = modal;
	}

	public String getNumInvoice() {
		return numInvoice;
	}

	public void setNumInvoice(String numInvoice) {
		this.numInvoice = numInvoice;
	}

	public StatusInvoice getStatusInvoice() {
		return statusInvoice;
	}

	public void setStatusInvoice(StatusInvoice statusInvoice) {
		this.statusInvoice = statusInvoice;
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

	public InvoiceExp getInvoiceNumero() {
		return invoiceNumero;
	}

	public void setInvoiceNumero(InvoiceExp invoiceNumero) {
		this.invoiceNumero = invoiceNumero;
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

}
