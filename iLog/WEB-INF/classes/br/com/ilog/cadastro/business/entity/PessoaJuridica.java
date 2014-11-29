package br.com.ilog.cadastro.business.entity;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.EntidadeRepetida;
import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 *
 */
@Entity
@Table(name="PESSOA_JURIDICA")
public class PessoaJuridica implements Identificavel<Long>  {

	/**  */
	private static final long serialVersionUID = -523781008186026327L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "pessoa_juridica_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@NotEmpty @NotNull(message="notnull") 
	@Label("lblRazaoSocial")
	@Column(name="RAZAO_SOCIAL", length = 100)
	private String razaoSocial;
	
	@NotEmpty @NotNull(message="notnull") 
	@Label("lblNomeFantasia")
	@Column(name="NOME_FANTASIA", length = 100)
	private String nomeFantasia;
	
	@NotNull(message="notnull")
	@Column(name="ATIVO")
	@Label("lblAtivo")
	private boolean ativo;

	@Column( name="VENDOR_SAP", length = 10 )
	@Label("lblNumVendorSap")
	private Long vendorSap;
	
	@Label( "lblCondicaoPagamento" )
	@Column(name="CONDICAO_PAGAMENTO", length = 20, nullable = true)
	@Enumerated( EnumType.STRING )
	private CondicaoPagamento condicaoPagamento;
	
	@Column(name = "QUANTIDADE_DIAS")
	@Label( "lblQuantidadeDias" )
	private Integer quantidadeDias;
	
	@OneToMany( mappedBy = "pessoa", fetch = FetchType.LAZY)
	@Cascade( value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN } )
	@OnDelete( action = OnDeleteAction.CASCADE )
	@OrderBy( value = "id" )
	private List<Endereco> enderecos;
	
	@OneToMany( mappedBy = "pessoa", fetch = FetchType.EAGER)
	@Cascade( value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN } )
	@OnDelete( action = OnDeleteAction.CASCADE )
	@OrderBy( value = "id" )
	private List<Contato> contatos;
	
	@ManyToMany(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.REFRESH)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name="PESSOA_TIPO",
			   joinColumns=@JoinColumn(name="PESSOA_ID"),
			   inverseJoinColumns=@JoinColumn(name="TIPO_ID"))
	@EntidadeRepetida @Label("lblTipoPessoaJuridica")
	private List<TipoPessoa> tiposPessoa;
	
	public PessoaJuridica() {
		super();
	}
	
	public PessoaJuridica(Long id) {
		this.id = id;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PessoaJuridica))
			return false;
		PessoaJuridica other = (PessoaJuridica) obj;
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

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomefantasia) {
		this.nomeFantasia = nomefantasia;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Long getVendorSap() {
		return vendorSap;
	}

	public void setVendorSap(Long vendorSap) {
		this.vendorSap = vendorSap;
	}

	public Integer getQuantidadeDias() {
		return quantidadeDias;
	}

	public void setQuantidadeDias(Integer quantidadeDias) {
		this.quantidadeDias = quantidadeDias;
	}

	public CondicaoPagamento getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}

	public List<TipoPessoa> getTiposPessoa() {
		return tiposPessoa;
	}

	public void setTiposPessoa(List<TipoPessoa> tiposPessoa) {
		this.tiposPessoa = tiposPessoa;
	}

}
