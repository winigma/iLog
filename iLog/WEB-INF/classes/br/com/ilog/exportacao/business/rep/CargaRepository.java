package br.com.ilog.exportacao.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.exportacao.business.entity.CargaExp;
import br.com.ilog.exportacao.business.entityFilter.BasicFiltroCargaExp;

public interface CargaRepository extends CrudRepository<CargaExp> {
	
	public List<CargaExp> listarCargaByFilter(BasicFiltroCargaExp filtro)throws BusinessException;

}
