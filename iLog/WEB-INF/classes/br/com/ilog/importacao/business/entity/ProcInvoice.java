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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.cits.commons.citsbusiness.util.Identificavel;

@Entity
@Table( name = "PROC_INVOICE" )
public class ProcInvoice implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 645006822172777579L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "proc_invoice_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@JoinColumn(name = "ID_PROC", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcBroker procBroker;
	
	@JoinColumn(name = "ID_INVOICE" )
	@ManyToOne(fetch = FetchType.LAZY)
	private Invoice invoice;
	
	@Column( name = "NR_INVOICE", length = 30 )
	private String nrInvoice;
	
	@Column( name = "EXPORTADOR", length = 60 )
	private String exportador;
	
	@Temporal( TemporalType.DATE )
	@Column( name = "DT_INVOICE" )
	private Date dtInvoice;
	
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
	 * @return the nrInvoice
	 */
	public String getNrInvoice() {
		return nrInvoice;
	}

	/**
	 * @param nrInvoice the nrInvoice to set
	 */
	public void setNrInvoice(String nrInvoice) {
		this.nrInvoice = nrInvoice;
	}

	/**
	 * @return the exportador
	 */
	public String getExportador() {
		return exportador;
	}

	/**
	 * @param exportador the exportador to set
	 */
	public void setExportador(String exportador) {
		this.exportador = exportador;
	}

	/**
	 * @return the dtInvoice
	 */
	public Date getDtInvoice() {
		return dtInvoice;
	}

	/**
	 * @param dtInvoice the dtInvoice to set
	 */
	public void setDtInvoice(Date dtInvoice) {
		this.dtInvoice = dtInvoice;
	}

	/**
	 * @return the invoice
	 */
	public Invoice getInvoice() {
		return invoice;
	}

	/**
	 * @param invoice the invoice to set
	 */
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

}
