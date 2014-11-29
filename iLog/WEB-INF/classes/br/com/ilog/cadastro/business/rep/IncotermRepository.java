package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Incoterm;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroIncoterm;

public interface IncotermRepository extends CrudRepository<Incoterm> {

	public List<Incoterm> listar(BasicFiltroIncoterm filtro) throws BusinessException;

	public Incoterm getBySigla(String sigla) throws BusinessException;

}
