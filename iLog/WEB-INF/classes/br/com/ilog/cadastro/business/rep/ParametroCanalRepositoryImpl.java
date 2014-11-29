package br.com.ilog.cadastro.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Canal;
import br.com.ilog.cadastro.business.entity.ParametroCanal;

@Component
public class ParametroCanalRepositoryImpl extends CrudRepositoryJPA<ParametroCanal> implements ParametroCanalRepository {

	@PersistenceContext(unitName = "ilog")
	private EntityManager entityManager;

	@Override
	public List<ParametroCanal> listarParametroCanais()
			throws BusinessException {
		HQLQuery<ParametroCanal> hql = new HQLQuery<ParametroCanal>(entityManager);
		hql.append("from ParametroCanal p");
		return hql.getResultList();
	}

	@Override
	public ParametroCanal alterar(ParametroCanal entity) throws BusinessException {		
		return super.alterar(entity);
	}
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public ParametroCanal getParametroCanais(Canal canal)
			throws BusinessException {
		
		HQLQuery<ParametroCanal> hql = new HQLQuery<ParametroCanal>(entityManager);
		hql.append("from ParametroCanal p");
		hql.appendEqual("p.canal", canal);
		
		return (ParametroCanal) hql.getSingleResult();
	}

}
