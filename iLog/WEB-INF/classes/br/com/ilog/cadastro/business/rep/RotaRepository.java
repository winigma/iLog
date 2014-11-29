package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;
import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroRota;

public interface RotaRepository extends CrudRepository<Rota> {

	public List<Rota> listar(BasicFiltroRota filtro) throws BusinessException;
	
	public Rota getByIdTrechos( Serializable id ) throws BusinessException;

	public List<Rota> listarComTrechos(BasicFiltroRota filtro)
			throws BusinessException;

}
