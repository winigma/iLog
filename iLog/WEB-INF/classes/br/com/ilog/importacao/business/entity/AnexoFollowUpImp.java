package br.com.ilog.importacao.business.entity;

import javax.persistence.Basic;
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

import org.hibernate.annotations.Type;

import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.geral.presentation.util.File;

/**
 * 
 * @author Heber Santiago
 *
 */
@Entity
@Table( name = "ANEXO_FOLLOW_UP_IMP" )
public class AnexoFollowUpImp implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 2409670189908109616L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "anexo_follow_up_imp_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@Type( type = "org.hibernate.type.BinaryType" )
	@Basic( fetch = FetchType.LAZY )
	@Column( name = "ANEXO" )
	private byte[] anexo;
	
	@Column(name="NOME_ARQUIVO", length=255, nullable=false)
	private String nomeArquivo;
	
	@Column(name="MIME_TYPE", length=255, nullable=false)
	private String mimeType;
	
	@JoinColumn( name = "ID_FOLLOW_UP", nullable = false )
	@ManyToOne( fetch = FetchType.LAZY )
	private FollowUpImport followUp;
	
	
	public AnexoFollowUpImp() {
		super();
	}
	
	public AnexoFollowUpImp( File file, FollowUpImport followUp ) {
		this.anexo = file.getData();
		this.nomeArquivo = file.getName();
		this.mimeType = file.getMime();
		this.followUp = followUp;
	}
	
	@Override
	public Long getPK() {
		return id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getAnexo() {
		return anexo;
	}

	public void setAnexo(byte[] anexo) {
		this.anexo = anexo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public FollowUpImport getFollowUp() {
		return followUp;
	}

	public void setFollowUp(FollowUpImport followUp) {
		this.followUp = followUp;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AnexoFollowUpImp))
			return false;
		AnexoFollowUpImp other = (AnexoFollowUpImp) obj;
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
