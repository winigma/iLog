package br.com.ilog.importacao.business.entity;

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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.CheckList;

/**
 * @author Heber Santiago
 */
@Entity
@Table( name = "INVOICE_CHECKLIST" )
public class InvoiceChecklist implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -3962279355608807859L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "invoice_checklist_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;

	@NotNull(message="notnull")
	@JoinColumn( name = "ID_INVOICE" )
	@ManyToOne( fetch = FetchType.LAZY )
	private Invoice invoice;
	
	@NotNull(message="notnull")
	@JoinColumn( name = "ID_CHECKLIST" )
	@ManyToOne( fetch = FetchType.LAZY )
	private CheckList checkList;
	
	@Column( name = "JUSTIFICATIVA", length = 255 )
	private String justificativa;
	
	@OneToMany(mappedBy = "invoiceChecklist", fetch = FetchType.LAZY)
	@Cascade(value={CascadeType.ALL,CascadeType.DELETE_ORPHAN})
	@OnDelete(action=OnDeleteAction.CASCADE)
	private List<InvoiceItemChecklist> respostasChecklist;
	
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

	public CheckList getCheckList() {
		return checkList;
	}

	public void setCheckList(CheckList checkList) {
		this.checkList = checkList;
	}

	public List<InvoiceItemChecklist> getRespostasChecklist() {
		return respostasChecklist;
	}

	public void setRespostasChecklist(List<InvoiceItemChecklist> respostasChecklist) {
		this.respostasChecklist = respostasChecklist;
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
		if (!(obj instanceof InvoiceChecklist))
			return false;
		InvoiceChecklist other = (InvoiceChecklist) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	
}
