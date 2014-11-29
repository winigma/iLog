package br.com.ilog.importacao.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.com.ilog.importacao.business.entity.AnexoFollowUpImp;

@Component
public class AnexoFollowUpImpRepositoryImpl extends
		CrudRepositoryJPA<AnexoFollowUpImp> implements AnexoFollowUpImpRepository {

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Exclusao da lista de anexos do FollowUp
	 * @param removeListAnexos2
	 * @throws BusinessException
	 */
	@Override
	public void excluir(List<AnexoFollowUpImp> removeListAnexos2) throws BusinessException {
		for (AnexoFollowUpImp anexo : removeListAnexos2) {
			if ( anexo.getId() != null ) {
				excluirById( anexo.getId() );
			}
		}
	}

	/**
	 * Cadastrar anexos de followUp de importacao
	 * @param anexosFollowUp2
	 * @throws BusinessException
	 */
	@Override
	public void adicionarAnexosFollowUp(List<AnexoFollowUpImp> anexosFollowUp2) throws BusinessException {
		for (AnexoFollowUpImp anexo : anexosFollowUp2) {
			if(anexo.getId() == null)
			cadastrar( anexo );
		}
	}

}
