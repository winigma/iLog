package br.com.ilog.cadastro.business.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * 
 * @author Heber Santiago
 * 
 */
@Entity
@Table(name = "ITEM_CHECKLIST")
public class ItemChecklist implements Identificavel<Long> {

	private static final long serialVersionUID = -2723276552274891883L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "item_checklist_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;

	@NotEmpty
	@NotNull(message="notnull")
	@Label("lblItem")
	@Column(name = "ITEM")
	private String item;

	@NotNull(message="notnull")
	@Label("lblAtivo")
	@Column(name = "ATIVO", length = 1)
	private Boolean ativo;

	@Temporal(TemporalType.DATE)
	@Column(name = "VIGENCIA_INICIAL")
	private Date vigencia_inicial;

	@Temporal(TemporalType.DATE)
	@Column(name = "VIGENCIA_FINAL")
	private Date vigencia_final;

	@ManyToOne(fetch = FetchType.EAGER)
	@Label("lblCheckList")
	@JoinColumn(name = "ID_CHECKLIST", insertable=true, updatable=true)
	@Fetch(FetchMode.JOIN)
	@Cascade( value = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE } )
	private CheckList checklist;
	
	@OneToMany(mappedBy = "itemCheckList", fetch = FetchType.LAZY)
	@Cascade(value={CascadeType.ALL})
	private List<DataVigencia> datasVigencia;
	
	
	/**
	 * @comentario utilizado pra pegar o item selecionado na table
	 */
	
	@Transient
	private Boolean select;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Date getVigencia_inicial() {
		return vigencia_inicial;
	}

	public void setVigencia_inicial(Date vigencia_inicial) {
		this.vigencia_inicial = vigencia_inicial;
	}

	public Date getVigencia_final() {
		return vigencia_final;
	}

	public void setVigencia_final(Date vigencia_final) {
		this.vigencia_final = vigencia_final;
	}

	public CheckList getChecklist() {
		return checklist;
	}

	public void setChecklist(CheckList checklist) {
		this.checklist = checklist;
	}

	@Override
	public Long getPK() {
		return this.id;
	}

	public Boolean getSelect() {
		return select;
	}

	public void setSelect(Boolean select) {
		this.select = select;
	}

	public List<DataVigencia> getDatasVigencia() {
		return datasVigencia;
	}

	public void setDatasVigencia(List<DataVigencia> datasVigencia) {
		this.datasVigencia = datasVigencia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ItemChecklist))
			return false;
		ItemChecklist other = (ItemChecklist) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
