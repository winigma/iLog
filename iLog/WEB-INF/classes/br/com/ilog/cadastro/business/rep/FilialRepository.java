package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFilial;

public interface FilialRepository extends CrudRepository<Filial>{
	
	public List<Filial> listarFiliais(BasicFiltroFilial filtro)throws BusinessException;

	/**
	 * @param codigo
	 * @return
	 * @throws BusinessException
	 */
	public Filial getFilialByCodigo(String codigo) throws BusinessException;

}
