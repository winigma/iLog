package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.UnidadeMedida;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroUnidMedida;

public interface UnidadeMedidaRepository extends CrudRepository<UnidadeMedida>{
	
	public List<UnidadeMedida> listarUnidMedidaBysFiltro(BasicFiltroUnidMedida filtro) throws BusinessException;

	public UnidadeMedida getByCodigo(String codigo) throws BusinessException;

}
