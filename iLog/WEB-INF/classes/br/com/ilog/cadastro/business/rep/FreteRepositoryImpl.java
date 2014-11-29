package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Frete;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFrete;

/**
 * @brief Classe de implementacao das consultas em Frete.
 * @since 14/02/2012
 * @author Heber Santiago
 */
@Component
public class FreteRepositoryImpl extends CrudRepositoryJPA<Frete> implements
		FreteRepository {

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public List<Frete> listar(BasicFiltroFrete filtro) throws BusinessException {
		
		HQLQuery<Frete> hql = new HQLQuery<Frete>( getEntityManager() );
		hql.append( "from Frete frete" );
		hql.append( "inner join fetch frete.moeda moeda" );
		hql.append( "inner join fetch frete.rota rota" );
		hql.append( "inner join fetch rota.cidadeOrigem co" );
		hql.append( "inner join fetch rota.cidadeDestino cd" );
		hql.append( "inner join fetch rota.paisOrigem po" );
		hql.append( "inner join fetch rota.paisDestino pd" );
		hql.append( "inner join fetch rota.agenteCarga agente" );
		hql.append( "inner join fetch rota.tipoTransporte modal" );
		
		hql.appendEqual("agente", filtro.getAgenteCarga());
		hql.appendEqual("co", filtro.getCidadeOrigem());
		hql.appendEqual("cd", filtro.getCidadeDestino());
		hql.appendEqual("po", filtro.getPaisOrigem());
		hql.appendEqual("pd", filtro.getPaisDestino());
		hql.appendEqual("modal", filtro.getModal());
		hql.appendEqual("rota.critico", filtro.getExpresso());
		
		hql.appendEqual("moeda", filtro.getMoeda());
		hql.appendEqual("frete.ativo", filtro.getAtivo());
		
		return hql.getResultList();
	}
	
	@Override
	public Frete getById(Serializable id) {
		Frete frete = super.getById(id);
		Hibernate.initialize( frete.getMoeda() );
		Hibernate.initialize( frete.getTaxasFrete() );
		Hibernate.initialize( frete.getRota() );
		Hibernate.initialize( frete.getRota().getCidadeDestino() );
		Hibernate.initialize( frete.getRota().getCidadeOrigem() );
		Hibernate.initialize( frete.getRota().getPaisDestino() );
		Hibernate.initialize( frete.getRota().getPaisOrigem() );
		Hibernate.initialize( frete.getRota().getAgenteCarga() );
		Hibernate.initialize( frete.getRota().getAgenteCarga().getContatos() );
		Hibernate.initialize( frete.getRota().getAgenteCarga().getEnderecos() );
		
		return frete;
	}

	@Override
	public Frete getByRota(Rota rota) throws BusinessException {
		
		if ( rota == null ) {
			throw new NullPointerException();
		}
		HQLQuery<Frete> hql = new HQLQuery<Frete>( getEntityManager() );
		
		hql.append( "from Frete frete" );
		hql.append( "inner join fetch frete.rota rota" );
		hql.appendEqual( "rota", rota);
		hql.appendEqual( "frete.ativo", true);
		
		try {
			Frete frete = hql.getSingleResult();
			Hibernate.initialize( frete.getMoeda() );
			Hibernate.initialize( frete.getTaxasFrete() );

			return frete;
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
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
