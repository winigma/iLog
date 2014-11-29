package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Frete;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFrete;

public interface FreteRepository extends CrudRepository<Frete> {

	public List<Frete> listar( BasicFiltroFrete filtro ) throws BusinessException;

	public Frete getByRota( Rota rota ) throws BusinessException;
	
}
