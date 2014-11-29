package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Projeto;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroProjeto;
import br.com.ilog.importacao.business.entity.Invoice;

public interface ProjetoRepository extends CrudRepository<Projeto> {

	public List<Projeto> listar(BasicFiltroProjeto filtro) throws BusinessException;

	public List<Projeto> listarAtivos() throws BusinessException;
	
	public List<Projeto> listarProjetosPorInvoices(List<Invoice> listaInvoices) throws BusinessException;
	
}
