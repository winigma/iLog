package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.TipoPessoa;


@Component
public class TipoPessoaRepositoryimpl extends CrudRepositoryJPA<TipoPessoa> implements TipoPessoaRepository {

	
	@PersistenceContext
	EntityManager entityManager;
	
	
	
	@Override
	public TipoPessoa getById(Serializable primaryKey) {
		HQLQuery<TipoPessoa> hql =  new HQLQuery<TipoPessoa>(entityManager);
		hql.append("from TipoPessoa tp");
		hql.appendEqual("tp.id", primaryKey);
		return hql.getSingleResult();
	}
	
	
	
	
	@Override
	public TipoPessoa cadastrar(TipoPessoa tipo) throws BusinessException {
		if (tipo.getId() == null) {
			getEntityManager().persist(tipo);
		}
		getEntityManager().flush();
		return tipo;
	}
	
	
	@Override
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		
	}

}
