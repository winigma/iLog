package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Feriado;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFeriado;

public interface FeriadoRepository extends CrudRepository<Feriado>{
	public List<Feriado> listaFeriados(BasicFiltroFeriado filtro)throws BusinessException;
	public List<Feriado> cadastrarVarios(List<Feriado> feriados) throws BusinessException;
}
