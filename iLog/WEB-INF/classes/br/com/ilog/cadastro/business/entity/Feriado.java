package br.com.ilog.cadastro.business.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 */

@Entity
@Table(name = "FERIADO")
public class Feriado implements Identificavel<Long> {

	private static final long serialVersionUID = -6336968563910800911L;
	@Id
	@SequenceGenerator( name = "gen", sequenceName = "feriado_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	@Column(name = "ID")
	private Long id;

	@NotNull(message="notnull")
	@NotEmpty
	@Label("lblNomeFeriado")
	@Column(name = "NOME")
	private String nome;

	@NotNull(message="notnull")
	@NotEmpty
	@Label("lblTipoFeriado")
	@Column(name = "TIPO")
	private String tipo;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA")
	@Label("lblDataFeriado")
	@NotNull
	private Date data;

	@NotNull(message="notnull")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PAIS")
	@Label("lblPaisFeriado")
	private Pais pais;

	public Feriado( Long id ) {
		this.id = id;
	}
	
	public Feriado() {
		super();
	}

	public Feriado(Feriado feriado) {
		this.nome = feriado.getNome();
		this.pais = feriado.getPais();
		this.tipo = feriado.getTipo();
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	

	public String getDatas() {
		String str ="";
		if (tipo.equals("M")) {
			SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
			str = out.format( data );

		} else if (tipo.equals("F")) {
			DateFormat formata = new SimpleDateFormat("dd/MM");
			str = formata.format( data );
			
		}
		return str;
	}

}
