package br.com.ilog.cadastro.business.rep;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Continente;
import br.com.ilog.cadastro.business.entity.ParametroContinente;

public interface ContinenteRepository extends CrudRepository<ParametroContinente> {
	
	public ParametroContinente getParametroContinente( Continente continente ) throws BusinessException;

}
