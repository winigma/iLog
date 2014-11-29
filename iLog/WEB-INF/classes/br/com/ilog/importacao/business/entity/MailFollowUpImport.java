package br.com.ilog.importacao.business.entity;

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

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Wisley Souza
 *
 */
@Entity
@Table(name = "MAIL_FOLLOW_UP_IMPORT")
public class MailFollowUpImport implements Identificavel<Long> {

	private static final long serialVersionUID = -5726991328166160023L;
	@Id
	@SequenceGenerator( name = "gen", sequenceName = "mail_follow_up_imp_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;

	@Column(name = "MAIL", length = 250)
	private String mail;

	@Column(name = "CONTATO", length = 250)
	private String contato;

	@ManyToOne(fetch = FetchType.EAGER)
	@Label("lblRelatar")
	@Fetch(FetchMode.JOIN)
	@Cascade(value = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.SAVE_UPDATE })
	@JoinColumn(name = "ID_RELATAR", insertable = true, updatable = true)
	private RelatarFollowUpImport relatar;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getPK() {
		return this.id;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public RelatarFollowUpImport getRelatar() {
		return relatar;
	}

	public void setRelatar(RelatarFollowUpImport relatar) {
		this.relatar = relatar;
	}

}
