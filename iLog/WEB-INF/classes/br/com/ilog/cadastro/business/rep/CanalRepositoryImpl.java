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
public class CanalRepositoryImpl extends CrudRepositoryJPA<ParametroCanal> implements CanalRepository {

	@PersistenceContext( name = "ilog" )
	EntityManager entityManager;
	
	@Override
	public List<ParametroCanal> listar() {
		return super.listar();
	}
	
	@Override
	public ParametroCanal getCanal(Canal canal) throws BusinessException {
		
		if ( canal != null ) {
			HQLQuery<ParametroCanal> hql = new HQLQuery<ParametroCanal>(getEntityManager());
			hql.append( "from ParametroCanal c" );
			
			hql.appendEqual("c.canal", canal);
			
			return hql.getSingleResult();
			
		}
		
		return null;
	}
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager arg0) {
		entityManager = arg0;
	}


}
