package br.com.ilog.importacao.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.importacao.business.entity.ItemPO;
import br.com.ilog.importacao.business.entity.PO;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroPO;

public interface PORepository extends
		CrudRepository<PO> {

	public PO getPO(String numPO)
			throws BusinessException;

	public List<PO> listarPO(BasicFiltroPO filtroPO) throws BusinessException;

	public ItemPO getItemPO(ItemPO itemPO) throws BusinessException;

}
