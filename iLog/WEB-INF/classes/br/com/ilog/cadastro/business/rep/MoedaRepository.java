package br.com.ilog.cadastro.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMoeda;

public interface MoedaRepository extends CrudRepository<Moeda> {

	public List<Moeda> listarMoedas(BasicFiltroMoeda filtroMoeda) throws BusinessException;

	public Moeda getBySigla(String sigla) throws BusinessException;

	public Moeda getMoedaPadrao() throws BusinessException;

}
