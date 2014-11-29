package br.com.ilog.cadastro.business.rep;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Canal;
import br.com.ilog.cadastro.business.entity.ParametroCanal;

public interface CanalRepository extends CrudRepository<ParametroCanal> {

	public ParametroCanal getCanal(Canal canal) throws BusinessException;

}
