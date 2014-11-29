package br.com.ilog.importacao.business.entity;

import javax.persistence.Basic;
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

import org.hibernate.annotations.Type;

import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.geral.presentation.util.File;

@Entity
@Table( name = "ANEXO_INVOICE" )
public class AnexoInvoice implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 5297193704274341209L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "anexo_invoice_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	//@Lob()
	@Type( type = "org.hibernate.type.BinaryType" )
	@Basic( fetch = FetchType.LAZY )
	@Column( name = "ANEXO" )
	private byte[] anexo;
	
	@Column(name="NOME_ARQUIVO", length=255, nullable=false)
	private String nomeArquivo;
	
	@Column(name="MIME_TYPE", length=255, nullable=false)
	private String mimeType;
	
	@JoinColumn( name = "ID_INVOICE", nullable = false )
	@ManyToOne( fetch = FetchType.LAZY )
	private Invoice invoice;
	
	public AnexoInvoice() {
		super();
	}
	
	public AnexoInvoice(File file, Invoice invoice) {
		this.anexo = file.getData();
		this.nomeArquivo = file.getName();
		this.mimeType = file.getMime();
		
		this.invoice = invoice;
	}

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

	public byte[] getAnexo() {
		return anexo;
	}

	public void setAnexo(byte[] anexo) {
		this.anexo = anexo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

}
