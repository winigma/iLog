package br.com.ilog.cadastro.business.rep;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.ParametroImposto;

public interface ParametroImpostoRepository extends CrudRepository<ParametroImposto> {
	
	public ParametroImposto getParametro( String descricao ) throws BusinessException;
}
