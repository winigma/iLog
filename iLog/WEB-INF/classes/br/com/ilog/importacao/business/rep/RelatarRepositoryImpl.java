package br.com.ilog.importacao.business.rep;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.importacao.business.entity.FollowUpImport;
import br.com.ilog.importacao.business.entity.RelatarFollowUpImport;

@Component
public class RelatarRepositoryImpl extends
		CrudRepositoryJPA<RelatarFollowUpImport> implements RelatarRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return this.entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public RelatarFollowUpImport getById(Serializable primaryKey) {
		RelatarFollowUpImport re = super.getById(primaryKey);
		return re;
	}

	@Override
	public RelatarFollowUpImport getRelatorByFollowUp(FollowUpImport obj)
			throws BusinessException {
		HQLQuery<RelatarFollowUpImport> hql = new HQLQuery<RelatarFollowUpImport>(
				entityManager);
		hql.append("from RelatarFollowUpImport r");

		try {
			if (obj != null) {
				hql.appendEqual("r.followUp", obj);

				if (!hql.getResultList().isEmpty()) {
					RelatarFollowUpImport rel = (RelatarFollowUpImport) hql
							.getSingleResult();
					return rel;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
