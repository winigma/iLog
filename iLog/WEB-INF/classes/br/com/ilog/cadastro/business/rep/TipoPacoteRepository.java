package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.TipoPacote;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoPacote;

public interface TipoPacoteRepository extends CrudRepository<TipoPacote> {

	public List<TipoPacote> listar(BasicFiltroTipoPacote filtro) throws BusinessException;

	public TipoPacote getByCodigo(String string) throws BusinessException;

	public List<TipoPacote> getByCategoria(String string) throws BusinessException;
	
}
