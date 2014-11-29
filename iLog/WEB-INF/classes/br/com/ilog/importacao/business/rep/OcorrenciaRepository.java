package br.com.ilog.importacao.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Ocorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroOcorrencia;
import br.com.ilog.importacao.business.entity.Carga;

public interface OcorrenciaRepository extends CrudRepository<Ocorrencia> {

	public List<Ocorrencia> listOcorrencias(BasicFiltroOcorrencia filtro)
			throws BusinessException;

	public boolean possuiOcorrencia(Carga basicFiltroOcorrencia) throws BusinessException;

	public List<Carga> possuiOcorrenciaNaoLida() throws BusinessException;

}
