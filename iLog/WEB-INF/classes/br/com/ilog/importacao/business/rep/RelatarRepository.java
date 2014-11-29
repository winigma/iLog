package br.com.ilog.importacao.business.rep;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.RelatarFollowUpImport;

public interface RelatarRepository extends
		CrudRepository<RelatarFollowUpImport> {

	public RelatarFollowUpImport getRelatorByFollowUp(FollowUpImport obj)
			throws BusinessException;

}
