package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroModal;

public interface ModalRepository extends CrudRepository<Modal> {

	public List<Modal> listar(BasicFiltroModal filtroModal) throws BusinessException;

}
