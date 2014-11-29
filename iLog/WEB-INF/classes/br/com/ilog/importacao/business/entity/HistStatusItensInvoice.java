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
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.ItemChecklist;

@Entity
@Table( name = "HIST_STATUS_ITENS_INV" )
public class HistStatusItensInvoice implements Identificavel<Long> {

	private static final long serialVersionUID = -8402379254809599000L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "hist_st_itens_inv_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@Label("lblHistorico")
	@JoinColumn(name = "ID_HISTORICO", insertable=true, updatable=true)
	@Fetch(FetchMode.JOIN)
	@Cascade( value = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE } )
	private HistoricoStatusInvoice historico;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ITEM_CHECKLIST", nullable = false )
	@Label("lblItemChecklist")
	@NotNull(message="notnull")
	private ItemChecklist itemChecklist;
	
	@NotNull(message="notnull")
	@Column( name = "RESPOSTA", length = 1 )
	private Boolean resposta;
	
	@Column( name = "REPROVADO", length = 1 )
	private Boolean reprovado;
	
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

	public HistoricoStatusInvoice getHistorico() {
		return historico;
	}

	public void setHistorico(HistoricoStatusInvoice historico) {
		this.historico = historico;
	}

	public ItemChecklist getItemChecklist() {
		return itemChecklist;
	}

	public void setItemChecklist(ItemChecklist itemChecklist) {
		this.itemChecklist = itemChecklist;
	}

	public Boolean getResposta() {
		return resposta;
	}

	public void setResposta(Boolean resposta) {
		this.resposta = resposta;
	}

	public Boolean getReprovado() {
		return reprovado;
	}

	public void setReprovado(Boolean aprovado) {
		this.reprovado = aprovado;
	}

}
