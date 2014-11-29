package br.com.ilog.importacao.business.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Wisley Souza
 *
 */
@Entity
@Table(name = "RELATAR_FOLLOW_UP_IMPORT")
public class RelatarFollowUpImport implements Identificavel<Long> {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator( name = "gen", sequenceName = "rel_follow_up_imp_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;

	@Column(name = "COLETADO")
	@Label("lblColetado")
	private Boolean coletado;

	@Column(name = "OCORRENCIA")
	@Label("lblOcorrencia")
	private Boolean ocorrencia;

	@Column(name = "CANAL")
	@Label("lblCanal")
	private Boolean canal;

	@Column(name = "LOM")
	@Label("lblLom")
	private Boolean lom;

	@OneToMany(mappedBy = "relatar", fetch = FetchType.EAGER)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<MailFollowUpImport> mails;

	@JoinColumn(name = "ID_FOLLOWUP")
	@OneToOne(fetch = FetchType.EAGER)
	private FollowUpImport followUp;

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

	public Boolean getColetado() {
		return coletado;
	}

	public void setColetado(Boolean coletado) {
		this.coletado = coletado;
	}

	public Boolean getCanal() {
		return canal;
	}

	public void setCanal(Boolean canal) {
		this.canal = canal;
	}

	public Boolean getLom() {
		return lom;
	}

	public void setLom(Boolean lom) {
		this.lom = lom;
	}

	public List<MailFollowUpImport> getMails() {
		return mails;
	}

	public void setMails(List<MailFollowUpImport> mails) {
		this.mails = mails;
	}

	public FollowUpImport getFollowUp() {
		return followUp;
	}

	public void setFollowUp(FollowUpImport followUp) {
		this.followUp = followUp;
	}

	public Boolean getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(Boolean ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
}
