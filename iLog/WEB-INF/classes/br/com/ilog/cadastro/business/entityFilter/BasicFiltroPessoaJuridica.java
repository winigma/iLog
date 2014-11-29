package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;
import java.util.Date;

import br.com.ilog.cadastro.business.entity.Contato;
import br.com.ilog.cadastro.business.entity.Endereco;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.TipoPessoa;

public class BasicFiltroPessoaJuridica implements Serializable {

	/** */
	private static final long serialVersionUID = 467742023978274267L;
	
	private String cnpj;
	private String razaoSocial;
	private String nomeFantasia;
	private Date dataConstituicao;
	private String status;
	private Endereco endereco;
	private TipoPessoa pessoa;
	private Contato contato;
	private Pais pais;
	private Long vendorSap;
	private Boolean ativo;
	
	public BasicFiltroPessoaJuridica() {
		super();
	}
	
	public BasicFiltroPessoaJuridica( TipoPessoa pessoa, Boolean ativo ) {
		this.pessoa	= pessoa;
		this.ativo = ativo;
	}
	
	public Contato getContato() {
		return contato;
	}
	public void setContato(Contato contato) {
		this.contato = contato;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	public String getNomeFantasia() {
		return nomeFantasia;
	}
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
	public Date getDataConstituicao() {
		return dataConstituicao;
	}
	public void setDataConstituicao(Date data_constituicao) {
		this.dataConstituicao = data_constituicao;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public TipoPessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(TipoPessoa pessoa) {
		this.pessoa = pessoa;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Pais getPais() {
		return pais;
	}
	public void setPais(Pais pais) {
		this.pais = pais;
	}
	public Long getVendorSap() {
		return vendorSap;
	}
	public void setVendorSap(Long vendorSap) {
		this.vendorSap = vendorSap;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	
	
	
}
