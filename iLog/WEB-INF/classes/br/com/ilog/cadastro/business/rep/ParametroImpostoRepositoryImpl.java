package br.com.ilog.cadastro.business.rep;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.ParametroImposto;

@Component
public class ParametroImpostoRepositoryImpl extends CrudRepositoryJPA<ParametroImposto> implements ParametroImpostoRepository {

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
	public ParametroImposto getParametro(String descricao)
			throws BusinessException {
		
		if ( descricao != null ) {
			HQLQuery<ParametroImposto> hql = new HQLQuery<ParametroImposto>(getEntityManager());
			hql.append("from ParametroImposto pi");
			hql.appendCondicao("lower(pi.descricao) like lower(:descricao)");
			hql.setParameter("descricao", descricao);
			
			try {
				return hql.getSingleResult();
			} catch (NoResultException e) {
				return null;
			}
		}
		return null;
	}

}
