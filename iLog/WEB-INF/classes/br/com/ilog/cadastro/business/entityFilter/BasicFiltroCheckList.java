package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.cadastro.business.entity.CheckList;
import br.com.ilog.cadastro.business.entity.ItemChecklist;


/**
 * 
 * @author Wisley Souza
 * @param filtro checklist, implementa todos os itens relacionado a um check list, entre eles Processo, Itens e check list propriamente dito
 *
 */
public class BasicFiltroCheckList implements Serializable {
	
	/** */
	private static final long serialVersionUID = 5911299495127811009L;
	
	private String parametro;
	private Boolean ativo;
	private CheckList checkList;
	private ItemChecklist itemCheckList;
	
	public BasicFiltroCheckList() {
		super();
	}
	
	public BasicFiltroCheckList( Boolean ativo ) {
		this.ativo = ativo;
	}
	
	public String getParametro() {
		return parametro;
	}
	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
	public CheckList getCheckList() {
		return checkList;
	}
	public void setCheckList(CheckList checkList) {
		this.checkList = checkList;
	}
	public ItemChecklist getItemCheckList() {
		return itemCheckList;
	}
	public void setItemCheckList(ItemChecklist itemCheckList) {
		this.itemCheckList = itemCheckList;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
}
