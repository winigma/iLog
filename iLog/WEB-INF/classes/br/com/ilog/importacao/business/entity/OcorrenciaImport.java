package br.com.ilog.importacao.business.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Motivo;
import br.com.ilog.seguranca.business.entity.Usuario;

@Entity
@Table(name = "OCORRENCIA_IMPORT")
public class OcorrenciaImport implements Identificavel<Long> {

	public OcorrenciaImport() {
		super();
	}

	public OcorrenciaImport(Long id) {
		this.id = id;
	}

	private static final long serialVersionUID = -8430299581396916077L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "notnull")
	@Label("lblDescricao")
	@Column(name = "DESCRICAO_OCORRENCIA", length = 150)
	private String descricao;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_OCORRENCIA")
	@NotNull(message = "notnull")
	@Label("lblDataOcorrencia")
	private Date dtOcorrencia;

	@JoinColumn(name = "ID_MOTIVO", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblMotivo")
	@NotNull(message = "notnull")
	private Motivo motivo;

	@JoinColumn(name = "ID_CARGA", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblCarga")
	@NotNull(message = "notnull")
	private Carga carga;

	@JoinColumn(name = "ID_AUTOR", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblAutor")
	@NotNull(message = "notnull")
	private Usuario autor;

	@JoinColumn(name = "ID_CIDADE", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblCidade")
	@NotNull(message = "notnull")
	private Cidade cidade;

	@Column(name = "LIDO")
	private boolean lido;

	@Column(name = "CANAL")
	private Boolean canal;

	@Column(name="LOM")
	private Boolean isLom;
	
	@Column(name="ATIVO")
	private Boolean ativo;
	
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

	public String getDescricaoShort() {
		if (descricao.length() > 25) {
			String descShort = descricao.substring(0, 22) + "...";
			return descShort;
		}
		return descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDtOcorrencia() {
		return dtOcorrencia;
	}

	public void setDtOcorrencia(Date dtOcorrencia) {
		this.dtOcorrencia = dtOcorrencia;
	}

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public Usuario getAutor() {
		return autor;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OcorrenciaImport))
			return false;
		OcorrenciaImport other = (OcorrenciaImport) obj;
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

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public boolean isLido() {
		return lido;
	}

	public void setLido(boolean lido) {
		this.lido = lido;
	}

	

	public Boolean getIsLom() {
		return isLom;
	}

	public void setIsLom(Boolean isLom) {
		this.isLom = isLom;
	}

	public Boolean getCanal() {
		return canal;
	}

	public void setCanal(Boolean canal) {
		this.canal = canal;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	

	
}
