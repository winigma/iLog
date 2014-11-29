package br.com.ilog.importacao.business.rep;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.com.ilog.importacao.business.entity.ItemPO;

@Component
public class ItemPORepositoryImpl extends CrudRepositoryJPA<ItemPO> implements
		ItemPORepository {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}
	
	
	
}