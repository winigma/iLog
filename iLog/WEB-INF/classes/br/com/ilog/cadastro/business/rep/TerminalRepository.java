package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Terminal;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTerminal;

public interface TerminalRepository extends CrudRepository<Terminal> {

	public List<Terminal> listarTerminais(BasicFiltroTerminal filtro) throws BusinessException;
	
}
