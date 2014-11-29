package br.com.ilog.cadastro.business.facade;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoOcorrencia;

public interface TipoOcorrenciaRepository extends CrudRepository<TipoOcorrencia> {

	public List<TipoOcorrencia> listar(BasicFiltroTipoOcorrencia filtroTipoOcorrencia) throws BusinessException;
	public List<TipoOcorrencia> listarTipoOcorrenciaImportacao(BasicFiltroTipoOcorrencia filtro)throws BusinessException;


}
