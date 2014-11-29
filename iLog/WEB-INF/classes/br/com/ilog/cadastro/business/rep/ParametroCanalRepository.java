package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Canal;
import br.com.ilog.cadastro.business.entity.ParametroCanal;

public interface ParametroCanalRepository extends CrudRepository<ParametroCanal>{
	public List<ParametroCanal> listarParametroCanais() throws BusinessException;

	public ParametroCanal getParametroCanais(Canal canal) throws BusinessException;
	
}
