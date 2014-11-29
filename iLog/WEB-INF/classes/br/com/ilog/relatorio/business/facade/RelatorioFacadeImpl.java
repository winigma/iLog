package br.com.ilog.relatorio.business.facade;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroOcorrencia;
import br.com.ilog.exportacao.business.entity.CargaExp;
import br.com.ilog.exportacao.business.entityFilter.BasicFiltroCargaExp;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroCarga;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroFollowUp;
import br.com.ilog.importacao.business.rep.CargaRepository;
import br.com.ilog.importacao.business.rep.FollowUpRepository;
import br.com.ilog.importacao.business.rep.FollowUpTrechoRepository;
import br.com.ilog.importacao.business.rep.InvoiceRepository;
import br.com.ilog.importacao.business.rep.OcorrenciaRepository;
import br.com.ilog.relatorio.business.dto.CargaRelatorioWeeklyBasis;
import br.com.ilog.relatorio.business.dto.TempoMedioEntrega;

@Component("controllerRelatorio")
public class RelatorioFacadeImpl implements RelatorioFacade {

	@Resource
	FollowUpTrechoRepository followUpTrechoRepository;

	@Resource
	FollowUpRepository followUpRepository;

	@Resource
	OcorrenciaRepository ocorrenciaRepository;

	@Resource
	InvoiceRepository invoiceRepository;

	@Resource
	CargaRepository cargaRepository;

	@Resource
	br.com.ilog.exportacao.business.rep.CargaRepository cargaExportacaoRepository;

	@Override
	public List<TempoMedioEntrega> tempoMedioEntrega(BasicFiltroFollowUp filtro) {
		try {
			return followUpTrechoRepository
					.listarTempoMedioEntregapoByOrigem(filtro);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Carga> importacao(BasicFiltroCarga filtro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FollowUpImport> qualityGrowth(BasicFiltroFollowUp filtro) {
		try {
			return followUpRepository.listQualityGrowth(filtro);
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<CargaRelatorioWeeklyBasis> weeklyBasis(
			BasicFiltroFollowUp filtro) throws BusinessException {
		return followUpRepository.listCargasWeeklyBasis(filtro);
	}

	@Override
	public List<CargaRelatorioWeeklyBasis> listaFollowUpPorContinente(
			BasicFiltroFollowUp filtro) throws BusinessException {
		return followUpRepository.listFollowUpPorContinente(filtro);
	}

	@Override
	public List<FollowUpImport> carregarListasFollowUps(
			List<FollowUpImport> listaRelatorio) throws BusinessException {

		List<FollowUpImport> result = new ArrayList<FollowUpImport>();

		for (FollowUpImport followUpImport : listaRelatorio) {

			followUpImport = followUpRepository.getById(followUpImport.getId());
			followUpImport.setTrechosFollowUp(followUpTrechoRepository
					.listarByFollowUp(followUpImport));
			for (FollowUpImportTrecho trecho : followUpImport
					.getTrechosFollowUp()) {
				trecho = followUpTrechoRepository.getById(trecho.getId());
			}

			// followUpImport.setCarga( cargaRepository.getByIdRelatorio(
			// followUpImport.getCarga().getId() ) );

			if (followUpImport.getCarga().getListaDeOcorrencias() != null) {
				followUpImport.getCarga().setListaDeOcorrencias(
						ocorrenciaRepository
								.listOcorrencias(new BasicFiltroOcorrencia(
										followUpImport.getCarga())));
			}
			if (followUpImport.getCarga().getListaDeInvoices() != null) {
				followUpImport.getCarga().setListaDeInvoices(
						invoiceRepository.listarInvoicesPorCarga(followUpImport
								.getCarga()));
			}
			result.add(followUpImport);
		}

		return result;
	}

	@Override
	public List<CargaExp> relatorioExportacao(BasicFiltroCargaExp filtro)
			throws BusinessException {
		return cargaExportacaoRepository.listarCargaByFilter(filtro);
	}

}
