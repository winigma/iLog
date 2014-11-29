package br.com.ilog.importacao.business.rep;

import java.util.List;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepository;
import br.com.ilog.importacao.business.entity.AnexoFollowUpImp;

/**
 * @author Heber Santiago
 * 
 * @since 05/12/2011
 *
 */
public interface AnexoFollowUpImpRepository extends CrudRepository<AnexoFollowUpImp> {

	void excluir(List<AnexoFollowUpImp> removeListAnexos2)
			throws BusinessException;

	void adicionarAnexosFollowUp(List<AnexoFollowUpImp> anexosFollowUp2)
			throws BusinessException;

}
