package br.com.ilog.cadastro.business.entity;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 */
@Entity
@Table(name = "Endereco")
public class Endereco implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 7318425641309067409L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "endereco_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@NotNull(message="notnull")
	@NotEmpty
	@Label("lblRua")
	private String rua;
	
	@Label("lblBairro")
	private String bairro;
	
	@NotNull(message="notnull")
	@NotEmpty
	@Label("lblNumero")
	private String numero;
	
	@NotNull(message="notnull")
	@NotEmpty
	@Label("lblCep")
	@Column( name = "ZIP_CODE", length = 11 )
	private String cep;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@NotNull(message="notnull")
	@JoinColumn(name="ID_PAIS")
	@Label("lblPais")
	private Pais pais;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@NotNull(message="notnull")
	@JoinColumn(name="ID_CIDADE")
	@Label("lblCidade")
	private Cidade cidade;

	@ManyToOne( fetch = FetchType.EAGER )
	@Label( "lblPessoa" )
	@JoinColumn( name = "ID_PESSOA_JURIDICA", insertable = true, updatable = true )
	@Fetch( FetchMode.JOIN )
	@Cascade( CascadeType.SAVE_UPDATE )
	@NotNull(message="notnull")
	private PessoaJuridica pessoa;
	
	@NotNull(message="notnull")
	@Label( "lblContinente" )
	@Enumerated( EnumType.STRING )
	private Continente continente;
	
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

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		if ( rua != null ) 
			rua = rua.toUpperCase();
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		if ( numero != null ) 
			numero =numero.toUpperCase();
		this.numero = numero;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		if ( cep != null ) 
			cep = cep.toUpperCase();
		this.cep = cep;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public PessoaJuridica getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaJuridica pessoa) {
		this.pessoa = pessoa;
	}

	public Continente getContinente() {
		return continente;
	}

	public void setContinente(Continente continente) {
		this.continente = continente;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

}
