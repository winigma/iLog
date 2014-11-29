package br.com.ilog.exportacao.business.entity;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

@Entity
@Table(name = "INVOICE_EXPOTACAO")
public class InvoiceExp implements Identificavel<Long> {

	private static final long serialVersionUID = 6831776837821535705L;

	@Id
	@SequenceGenerator(name = "gen", sequenceName = "invoice_expo_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
	private Long id;

	@Label("lblNumero")
	@Column(name = "NUM_INVOICE", length = 20)
	@NotNull(message = "notnull")
	private String numeroInvoice;

	@Enumerated(EnumType.STRING)
	@Label("lblStatus")
	@NotNull(message = "notnull")
	private StatusInvoice status;

	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblCarga")
	@Fetch(FetchMode.JOIN)
	@Cascade(value = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.SAVE_UPDATE })
	@JoinColumn(name = "ID_CARGA", insertable = true, updatable = true)
	private CargaExp carga;


	@OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<ItemInvoiceExp> itens;

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

	public String getNumeroInvoice() {
		return numeroInvoice;
	}

	public void setNumeroInvoice(String numeroInvoice) {
		this.numeroInvoice = numeroInvoice;
	}

	public StatusInvoice getStatus() {
		return status;
	}

	public void setStatus(StatusInvoice status) {
		this.status = status;
	}

	public CargaExp getCarga() {
		return carga;
	}

	public void setCarga(CargaExp carga) {
		this.carga = carga;
	}

	public List<ItemInvoiceExp> getItens() {
		return itens;
	}

	public void setItens(List<ItemInvoiceExp> itens) {
		this.itens = itens;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InvoiceExp))
			return false;
		InvoiceExp other = (InvoiceExp) obj;
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
