package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.CheckList;
import br.com.ilog.cadastro.business.entity.ItemChecklist;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCheckList;

public interface CheckListRepository extends CrudRepository<CheckList>{
	
	public List<CheckList> listarTypesOfCheckList(BasicFiltroCheckList check)throws BusinessException;
	public List<ItemChecklist> listarItensCheckListByFilter(BasicFiltroCheckList filtro) throws BusinessException;

}
