package br.com.ilog.importacao.business.entity;

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

import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.ItemChecklist;

/**
 * 
 * @author Heber Santiago
 *
 */
@Entity
@Table( name = "INVOICE_ITEM_CHECK" )
public class InvoiceItemChecklist implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -1000037253372189210L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "invoice_item_check_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@Column( name = "RESPOSTA", length = 1 )
	private Boolean resposta;

	@Column( name = "REPROVADO", length = 1 )
	private Boolean reprovado;
	
	@NotNull(message="notnull")
	@JoinColumn( name = "ID_INVOICE_CHECKLIST" )
	@ManyToOne( fetch = FetchType.LAZY )
	private InvoiceChecklist invoiceChecklist;
	
	@NotNull(message="notnull")
	@JoinColumn( name = "ID_ITEM_CHECKLIST" )
	@ManyToOne( fetch = FetchType.LAZY )
	private ItemChecklist itemCheckList;
	
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

	public Boolean getResposta() {
		return resposta;
	}

	public void setResposta(Boolean resposta) {
		this.resposta = resposta;
	}

	public InvoiceChecklist getInvoiceChecklist() {
		return invoiceChecklist;
	}

	public void setInvoiceChecklist(InvoiceChecklist invoiceChecklist) {
		this.invoiceChecklist = invoiceChecklist;
	}

	public ItemChecklist getItemCheckList() {
		return itemCheckList;
	}

	public void setItemCheckList(ItemChecklist itemCheckList) {
		this.itemCheckList = itemCheckList;
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
		if (!(obj instanceof InvoiceItemChecklist))
			return false;
		InvoiceItemChecklist other = (InvoiceItemChecklist) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean getReprovado() {
		return reprovado;
	}

	public void setReprovado(Boolean aprovado) {
		this.reprovado = aprovado;
	}
}
