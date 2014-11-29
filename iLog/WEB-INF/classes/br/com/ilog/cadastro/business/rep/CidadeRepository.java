package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;
import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroCidade;
import br.com.ilog.importacao.business.entity.FollowUpImport;

/**
 * @author Celso Oscar Junior
 */

public interface CidadeRepository extends CrudRepository<Cidade>{
	public List<Cidade> listarCidades(BasicFiltroCidade filtro) throws BusinessException;

	public List<Cidade> getCidadesByPais(Pais pais) throws BusinessException;

	public List<Cidade> getCidadesByFollowUp(FollowUpImport followTrecho) throws BusinessException;
	//public List<Cidade> getCidadesByFollowUp(FollowUpExport followTrecho) throws BusinessException;
	public List<Cidade> getCidadesByFollowUpMoreCarga(FollowUpImport followTrecho) throws BusinessException;

	public List<Cidade> getCidadesByRota(Rota rota) throws BusinessException;
	
	public Cidade cityById(Serializable arg) throws BusinessException;

}
