package br.com.ilog.importacao.business.entity;

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
import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.entity.FormaPagamento;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;

@Entity
@Table(name = "PO")
public class PO implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -5885905669019094475L;
	
	@Id
	@SequenceGenerator( name = "gen", sequenceName = "po_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@JoinColumn(name = "ID_FILIAL", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Filial filial;
	
	@JoinColumn(name = "ID_FORNECEDOR", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private PessoaJuridica fornecedor;
	
	@JoinColumn(name = "ID_FORMA_PAGAMENTO", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private FormaPagamento formaPagamento;
	
	@Column(name = "NUMERO_PO" , length = 10)
	private String numeroPO;
	

	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_PO")
	private Date dataPO;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_ENTREGA")
	private Date dataEntrega;
	
	@OneToMany(mappedBy = "po", fetch = FetchType.EAGER)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<ItemPO> itens;
	
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
	 * @return the fornecedor
	 */
	public PessoaJuridica getFornecedor() {
		return fornecedor;
	}

	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(PessoaJuridica fornecedor) {
		this.fornecedor = fornecedor;
	}

	/**
	 * @return the numeroPO
	 */
	public String getNumeroPO() {
		return numeroPO;
	}

	/**
	 * @param numeroPO the numeroPO to set
	 */
	public void setNumeroPO(String numeroPO) {
		this.numeroPO = numeroPO;
	}

	/**
	 * @return the itens
	 */
	public List<ItemPO> getItens() {
		return itens;
	}

	/**
	 * @param itens the itens to set
	 */
	public void setItens(List<ItemPO> itens) {
		this.itens = itens;
	}

	/**
	 * @return the formaPagamento
	 */
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	/**
	 * @param formaPagamento the formaPagamento to set
	 */
	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PO))
			return false;
		PO other = (PO) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
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

	
	/**
	 * @return the dataPO
	 */
	public Date getDataPO() {
		return dataPO;
	}

	/**
	 * @param dataPO the dataPO to set
	 */
	public void setDataPO(Date dataPO) {
		this.dataPO = dataPO;
	}

	/**
	 * @return the dataEntrega
	 */
	public Date getDataEntrega() {
		return dataEntrega;
	}

	/**
	 * @param dataEntrega the dataEntrega to set
	 */
	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}
}
