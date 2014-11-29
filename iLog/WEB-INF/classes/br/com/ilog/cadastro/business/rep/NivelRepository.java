package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Nivel;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroNivel;

public interface NivelRepository extends CrudRepository<Nivel> {
	
	public List<Nivel> listarNivelByFiltro(BasicFiltroNivel filtro) throws BusinessException;

}
