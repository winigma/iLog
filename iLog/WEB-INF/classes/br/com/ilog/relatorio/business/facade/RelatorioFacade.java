package br.com.ilog.relatorio.business.facade;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.com.ilog.exportacao.business.entity.CargaExp;
import br.com.ilog.exportacao.business.entityFilter.BasicFiltroCargaExp;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroFollowUp;
import br.com.ilog.relatorio.business.dto.CargaRelatorioWeeklyBasis;
import br.com.ilog.relatorio.business.dto.TempoMedioEntrega;

public interface RelatorioFacade {

	public List<TempoMedioEntrega> tempoMedioEntrega(BasicFiltroFollowUp filtro);

	public List<Carga> importacao(BasicFiltroCarga filtro);

	public List<FollowUpImport> qualityGrowth(BasicFiltroFollowUp filtro);

	public List<CargaRelatorioWeeklyBasis> weeklyBasis(
			BasicFiltroFollowUp filtro) throws BusinessException;

	public List<CargaRelatorioWeeklyBasis> listaFollowUpPorContinente(
			BasicFiltroFollowUp filtro) throws BusinessException;

	public List<FollowUpImport> carregarListasFollowUps(
			List<FollowUpImport> listaRelatorio) throws BusinessException;

	public List<CargaExp> relatorioExportacao(BasicFiltroCargaExp filtro)throws BusinessException;

}
