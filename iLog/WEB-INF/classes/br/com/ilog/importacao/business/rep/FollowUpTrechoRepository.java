package br.com.ilog.importacao.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.ParametroCanal;
import br.com.ilog.importacao.business.entity.Carga;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;
import br.com.ilog.importacao.business.entityFilter.BasicFiltroFollowUp;
import br.com.ilog.relatorio.business.dto.TempoMedioEntrega;

public interface FollowUpTrechoRepository extends CrudRepository<FollowUpImportTrecho> {

	public ParametroCanal getCanalCarga(Carga carga) throws BusinessException;

	public List<TempoMedioEntrega> listarTempoMedioEntregapoByOrigem(BasicFiltroFollowUp filtro)throws BusinessException;

	public List<FollowUpImportTrecho> listarByCarga(Carga carga) throws BusinessException;
	public List<FollowUpImportTrecho> listTrechoByCity(Cidade city)throws BusinessException;

	public List<FollowUpImportTrecho> listarByFollowUp(FollowUpImport followUpImport) throws BusinessException;
}
