package br.com.ilog.exportacao.business.facade;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.com.ilog.exportacao.business.entity.CargaExp;
import br.com.ilog.exportacao.business.entityFilter.BasicFiltroCargaExp;

public interface ExportacaoFacade {
	
	
	public List<CargaExp> listarCargasExportacaoByFiltro(BasicFiltroCargaExp filtro) throws BusinessException;

}
