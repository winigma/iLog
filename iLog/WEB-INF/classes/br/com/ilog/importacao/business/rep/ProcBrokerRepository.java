package br.com.ilog.importacao.business.rep;

import java.io.Serializable;
import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.ProcBroker;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroProcBroker;

public interface ProcBrokerRepository extends CrudRepository<ProcBroker> {

	public List<ProcBroker> listar( BasicFiltroProcBroker filtro ) throws BusinessException;

	public void salvarBroker(List<ProcBroker> brokersImport) throws BusinessException;

	public ProcBroker getById(Serializable primaryKey, boolean iniciarListas) throws BusinessException;

	public List<ProcBroker> listarSemCarga(Carga carga) throws BusinessException;
	
}
