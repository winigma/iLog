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

import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 */
@Entity
@Table(name="CONTATO")
public class Contato implements Identificavel<Long>{

	
	/**  */
	private static final long serialVersionUID = 1133683599414828683L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "contato_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;	
	
	@Label( "lblResponsavel" )
	@Column( name = "CONTATO", length = 80 )
	@NotNull(message="notnull")
	private String nomeResponsavel;
	
	@Label("lblEmail")
	@Column(name="EMAIL", length = 150 )
	private String email;
	
	@Label("lblTelefone")
	@Column(name="TELEFONE", length = 16 )
	private String telefone;
	
	@Label("lblTelefoneOpcional")
	@Column(name="TELEFONE_OPCIONAL", length = 16 )
	private String telefoneOpcional;
	
	@Label("lblFax")
	@Column(name="FAX", length = 16 )
	private String fax;
	
	@Label( "lblSite" )
	@Column( name = "SITE", length = 150 )
	private String site;

	@Label( "lblIdiomaContato" )
	@Column( name = "IDIOMA" )
	@Enumerated( EnumType.STRING )
	private IdiomaContato idioma;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@NotNull(message="notnull")
	@JoinColumn(name="ID_PESSOA")
	@Label("lblPessoaContato")
    private PessoaJuridica pessoa;
	
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

	public PessoaJuridica getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaJuridica pessoa) {
		this.pessoa = pessoa;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getTelefoneOpcional() {
		return telefoneOpcional;
	}

	public void setTelefoneOpcional(String telefoneOpcional) {
		this.telefoneOpcional = telefoneOpcional;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		if ( nomeResponsavel != null ) 
			nomeResponsavel = nomeResponsavel.toUpperCase();
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public IdiomaContato getIdioma() {
		return idioma;
	}

	public void setIdioma(IdiomaContato idioma) {
		this.idioma = idioma;
	}

}
