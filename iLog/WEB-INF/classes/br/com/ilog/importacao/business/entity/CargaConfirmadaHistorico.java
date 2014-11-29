package br.com.ilog.importacao.business.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.seguranca.business.entity.Usuario;

@Entity
@Table(name = "CARGA_CONFIRMADA_HISTORICO")
public class CargaConfirmadaHistorico implements Identificavel<Long> {

	private static final long serialVersionUID = 7109479679933807972L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "carga_conf_hist_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;

	@NotNull(message = "notnull")
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_USER")
	private Usuario user;

	@NotNull(message = "notnull")
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CARGA")
	private Carga carga;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA")
	@NotNull(message = "notnull")
	private Date data;

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

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

}
