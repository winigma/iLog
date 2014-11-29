package br.com.ilog.cadastro.business.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiagoaa
 */
@Entity
@Table(name = "CHECKLIST")
public class CheckList implements Identificavel<Long> {
	
	/** */
	private static final long serialVersionUID = 9020865983000746013L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty
	@NotNull(message="notnull")
	@Label("lblNome")
	@Column(name = "NOME")
	private String nome;
	
	@Label("lblAtivo")
	@Column(name = "ATIVO", length = 1)
	private Boolean ativo;

	@OneToMany(mappedBy = "checklist", fetch = FetchType.LAZY)
	@Cascade(value={CascadeType.ALL,CascadeType.DELETE_ORPHAN})
	@OnDelete(action=OnDeleteAction.CASCADE)
	private List<ItemChecklist> item;

	public CheckList() {
		super();
	}
	/**
	 * @coment construto que captura o id
	 * @param id
	 */
	public CheckList( Long id ) {
		this.id = id;
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

	@Override
	public Long getPK() {
		return this.id;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public List<ItemChecklist> getItem() {
		return item;
	}

	public void setItem(List<ItemChecklist> item) {
		this.item = item;
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
		if (!(obj instanceof CheckList))
			return false;
		CheckList other = (CheckList) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
