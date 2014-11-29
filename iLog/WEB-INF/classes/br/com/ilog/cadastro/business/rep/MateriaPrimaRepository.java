package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.MateriaPrima;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMateriaPrima;

public interface MateriaPrimaRepository extends CrudRepository<MateriaPrima> {

	public List<MateriaPrima> listar(BasicFiltroMateriaPrima filtro) throws BusinessException;

}
