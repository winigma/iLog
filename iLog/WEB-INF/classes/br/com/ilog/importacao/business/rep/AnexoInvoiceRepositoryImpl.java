package br.com.ilog.importacao.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.com.ilog.importacao.business.entity.AnexoInvoice;

@Component
public class AnexoInvoiceRepositoryImpl extends CrudRepositoryJPA<AnexoInvoice>
		implements AnexoInvoiceRepository {

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

	@Override
	public void adicionarAnexosInvoice(List<AnexoInvoice> anexos)
			throws BusinessException {
		
		if ( anexos != null )
			for (AnexoInvoice anex : anexos) {
				if (anex.getId() == null)
					cadastrar(anex);
			}
	}

	@Override
	public void excluirAnexoInvoice(List<AnexoInvoice> removeListAnexos)
			throws BusinessException {
		if (removeListAnexos != null ) {
			for (AnexoInvoice anex : removeListAnexos) {
				if ( anex.getId() != null ) {
					excluirById( anex.getId() );
				}
			}
		}
	}

}
