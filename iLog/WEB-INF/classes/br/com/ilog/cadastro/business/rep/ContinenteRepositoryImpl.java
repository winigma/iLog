package br.com.ilog.cadastro.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Continente;
import br.com.ilog.cadastro.business.entity.ParametroContinente;

@Component
public class ContinenteRepositoryImpl extends CrudRepositoryJPA<ParametroContinente> implements ContinenteRepository {

	@PersistenceContext( name = "ilog" )
	EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager arg0) {
		entityManager = arg0;
	}

	@Override
	public List<ParametroContinente> listar() {
		return super.listar();
	}
	
	@Override
	public ParametroContinente getParametroContinente(Continente continente)
			throws BusinessException {
		
		if ( continente != null ) {
			HQLQuery<ParametroContinente> hql = new HQLQuery<ParametroContinente>(getEntityManager());
			hql.append("from ParametroContinente c");
			
			hql.appendEqual("c.continente", continente);
			
			return hql.getSingleResult();
			
		}
		return null;
	}

}
