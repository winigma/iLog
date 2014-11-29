package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.com.ilog.cadastro.business.entity.Trecho;

@Component
public class TrechoRepositoryImpl extends CrudRepositoryJPA<Trecho> implements TrechoRepository {

	@PersistenceContext( name = "ilog" )
	EntityManager entityManager;

	@Override
	public Trecho getById(Serializable primaryKey) {
		Trecho t = super.getById( primaryKey );
		
		if ( t != null ) {
			Hibernate.initialize( t.getTerminalOrigem() );
			Hibernate.initialize( t.getTerminalDestino() );
		}
		
		return t;
	}
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager arg0) {
		this.entityManager = arg0;
	}

}
