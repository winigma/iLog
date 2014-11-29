package br.com.ilog.exportacao.business.entity;

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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

@Entity
@Table(name = "ITEM_INVOICE_EXPOTACAO")
public class ItemInvoiceExp implements Identificavel<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "gen", sequenceName = "item_inv_expo_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
	private Long id;

	@Column(name = "DESCRICAO")
	@NotNull(message = "notnull")
	@Label("lblDescItem")
	private String descricao;

	@Label("lblVendorPartNum")
	@Column(name = "VENDOR_PART_NUM", length = 20)
	private String vendorPartNum;

	@NotNull(message = "notnull")
	@Label("lblQuantidade")
	@Column(name = "QUANTIDADE")
	private Integer quantidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblCheckList")
	@Fetch(FetchMode.JOIN)
	@Cascade(value = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.SAVE_UPDATE })
	@JoinColumn(name = "ID_PEDIDO", insertable = true, updatable = true)
	private InvoiceExp invoice;

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

	public InvoiceExp getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceExp invoice) {
		this.invoice = invoice;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getVendorPartNum() {
		return vendorPartNum;
	}

	public void setVendorPartNum(String vendorPartNum) {
		this.vendorPartNum = vendorPartNum;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ItemInvoiceExp))
			return false;
		ItemInvoiceExp other = (ItemInvoiceExp) obj;
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
