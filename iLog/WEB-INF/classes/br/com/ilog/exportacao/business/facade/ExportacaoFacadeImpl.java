package br.com.ilog.exportacao.business.facade;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.com.ilog.exportacao.business.entity.CargaExp;
import br.com.ilog.exportacao.business.entityFilter.BasicFiltroCargaExp;
import br.com.ilog.exportacao.business.rep.CargaRepository;

@Component("controllerExportacao")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = BusinessException.class)
public class ExportacaoFacadeImpl implements ExportacaoFacade {

	@Resource
	CargaRepository cargaRepository;

	@Override
	public List<CargaExp> listarCargasExportacaoByFiltro(
			BasicFiltroCargaExp filtro) throws BusinessException {
		return cargaRepository.listarCargaByFilter(filtro);
	}
}
