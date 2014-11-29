package br.com.ilog.importacao.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroPlanejarCarga;

public interface CargaRepository extends CrudRepository<Carga> {

	public List<Carga> listCargaByFilter(BasicFiltroCarga filtro)
			throws BusinessException;

	public List<Carga> listCargasFollowUp(BasicFiltroCarga filtro)
			throws BusinessException;

	public List<Carga> listCargasPainel(BasicFiltroCarga filtro)
			throws BusinessException;

	public List<Carga> listarCargas(BasicFiltroCarga filtro)
			throws BusinessException;

	public List<Carga> listCargaRelatorio(BasicFiltroCarga filtro)
			throws BusinessException;

	public List<Carga> listCargasPainelLogistica(BasicFiltroCarga filtro)
			throws BusinessException;

	public Carga listCargaPlussInvoice(Carga carga) throws BusinessException;

	public Carga getByIdRelatorio(Long id) throws BusinessException;
	
	
	//cargas planejadas
	public List<Carga> listarEmbarquesPlenejados(BasicFiltroPlanejarCarga filtro)throws BusinessException;

	public List<Carga> listarCargasBroker(BasicFiltroCarga filtro) throws BusinessException;

	public Carga carregarCustos(Carga carga) throws BusinessException;

	public Carga alterarCusto(Carga carga) throws BusinessException;

	public Carga carregarProcBroker(Carga carga) throws BusinessException;

	public Carga preencheSequencial(Carga carga) throws BusinessException;
	
	
	
	
	//metodo experimental
	public List<Carga> listarLastUpdates(BasicFiltroCarga filtro) throws BusinessException;

	/**
	 * Listar Cargas com processo de DI associadas.
	 * @param filtro
	 * @return
	 * @throws BusinessException
	 */
	public List<Carga> listarProcessosDI(BasicFiltroCarga filtro) throws BusinessException;
	
	
}
