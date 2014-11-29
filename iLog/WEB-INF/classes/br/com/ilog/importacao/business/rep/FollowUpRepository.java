package br.com.ilog.importacao.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.importacao.business.entity.AnexoFollowUpImp;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroFollowUp;
import br.com.ilog.relatorio.business.dto.CargaRelatorioWeeklyBasis;

public interface FollowUpRepository extends CrudRepository<FollowUpImport> {
	
	/**
	 * Recupera o followup conforme a carga.
	 * @param carga
	 * @return
	 * @throws BusinessException
	 */
	public FollowUpImport getFollowUpByCarga( Carga carga ) throws BusinessException;

	public FollowUpImport carregarFollowUpEstimados(FollowUpImport followUp) throws BusinessException;

	public List<AnexoFollowUpImp> getAnexosFollowUp(FollowUpImport followUp) throws BusinessException;

	public List<FollowUpImport> listQualityGrowth(BasicFiltroFollowUp filtro) throws BusinessException;

	public List<CargaRelatorioWeeklyBasis> listCargasWeeklyBasis(BasicFiltroFollowUp filtro) throws BusinessException;

	public List<CargaRelatorioWeeklyBasis> listFollowUpPorContinente(BasicFiltroFollowUp filtro) throws BusinessException;
	
}
