package br.com.ilog.seguranca.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.seguranca.business.entity.Funcionalidade;

public interface FuncionalidadeRepository extends CrudRepository<Funcionalidade> {

	public void atualizarFuncionalidades() throws BusinessException;

	public List<Funcionalidade> listarFuncionalidades() throws BusinessException;
	
}
