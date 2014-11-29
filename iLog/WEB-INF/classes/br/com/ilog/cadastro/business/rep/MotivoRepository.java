package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Motivo;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMotivo;

public interface MotivoRepository extends CrudRepository<Motivo> {

	List<Motivo> listar(BasicFiltroMotivo filtro);
	public List<Motivo> listarMotivo( BasicFiltroMotivo filtro ) throws BusinessException;


}
