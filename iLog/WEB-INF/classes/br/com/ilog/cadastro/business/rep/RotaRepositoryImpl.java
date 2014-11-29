package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroRota;

@Component
public class RotaRepositoryImpl extends CrudRepositoryJPA<Rota> implements RotaRepository {

	@PersistenceContext( name = "ilog" )
	EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager arg0) {
		this.entityManager = arg0;
	}

	@Override
	public List<Rota> listar(BasicFiltroRota filtro) throws BusinessException {
		
		HQLQuery<Rota> hql = new HQLQuery<Rota>(getEntityManager());
		hql.append("from Rota rota");
		hql.append("inner join fetch rota.tipoTransporte modal");
		hql.append("inner join fetch rota.cidadeOrigem co");
		hql.append("inner join fetch rota.cidadeDestino cd");
		hql.append("inner join fetch rota.paisOrigem po");
		hql.append("inner join fetch rota.paisDestino pd");
		hql.append("inner join fetch rota.agenteCarga agente");

		if (filtro != null) {
			if (filtro.getCidadeOrigem() != null) {
				hql.appendEqual("co.id", filtro.getCidadeOrigem().getId());
			}
			if (filtro.getPaisOrigem() != null) {
				hql.appendEqual("po.id", filtro.getPaisOrigem().getId());
			}
			if (filtro.getCidadeDestino() != null) {
				hql.appendEqual("cd.id", filtro.getCidadeDestino().getId());
			}
			if (filtro.getPaisDestino() != null) {
				hql.appendEqual("pd.id", filtro.getPaisDestino().getId());
			}
			if (filtro.getAgenteCargas() != null) {
				hql.appendEqual("agente.id", filtro.getAgenteCargas().getId());
			}

			if (filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) {
				hql.appendCondicao(" lower(rota.codigo) like lower(:code) ");
				hql.setParameter("code", filtro.getCodigo() + "%");
			}

			hql.appendEqual("modal", filtro.getTipoTransporte());
			hql.appendEqual("rota.critico", filtro.getCritico());
			hql.appendEqual("rota.ativo", filtro.getAtivo());
		}

		hql.append("order by co.nome, rota.codigo");
		return hql.getResultList();
	}

	@Override
	public Rota getById(Serializable primaryKey) {
		Rota r = super.getById(primaryKey);
		
		if ( r != null ) {
			Hibernate.initialize( r.getPaisOrigem() );
			Hibernate.initialize( r.getPaisDestino() );
			Hibernate.initialize( r.getCidadeOrigem() );
			Hibernate.initialize( r.getCidadeDestino() );
			Hibernate.initialize( r.getAgenteCarga() );
			Hibernate.initialize( r.getTrechos() );
			Hibernate.initialize( r.getTipoTransporte() );
		}
		return r;
	}
	
	@Override
	public List<Rota> listarComTrechos(BasicFiltroRota filtro) throws BusinessException {
		
		HQLQuery<Rota> hql = new HQLQuery<Rota>(getEntityManager());
		hql.append("select distinct rota from Rota rota");
		hql.append("inner join fetch rota.trechos trechos");
		hql.append("inner join fetch rota.tipoTransporte modal");
		hql.append("inner join fetch rota.cidadeOrigem co");
		hql.append("inner join fetch rota.cidadeDestino cd");
		hql.append("inner join fetch rota.paisOrigem po");
		hql.append("inner join fetch rota.paisDestino pd");
		hql.append("inner join fetch rota.agenteCarga agente");

		if (filtro != null) {
			if (filtro.getCidadeOrigem() != null) {
				hql.appendEqual("co.id", filtro.getCidadeOrigem().getId());
			}
			if (filtro.getPaisOrigem() != null) {
				hql.appendEqual("po.id", filtro.getPaisOrigem().getId());
			}
			if (filtro.getCidadeDestino() != null) {
				hql.appendEqual("cd.id", filtro.getCidadeDestino().getId());
			}
			if (filtro.getPaisDestino() != null) {
				hql.appendEqual("pd.id", filtro.getPaisDestino().getId());
			}
			if (filtro.getAgenteCargas() != null) {
				hql.appendEqual("agente.id", filtro.getAgenteCargas().getId());
			}

			if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
				hql.appendCondicao(" rota.codigo like :code ");
				hql.setParameter("code", filtro.getCodigo() + "%");
			}

			hql.appendEqual("modal", filtro.getTipoTransporte());
			hql.appendEqual("rota.critico", filtro.getCritico());
			hql.appendEqual("rota.ativo", filtro.getAtivo());
		}

		hql.append("order by co.nome, rota.codigo");
		return hql.getResultList();
	}
	
	@Override
	public Rota getByIdTrechos(Serializable id) throws BusinessException {

		Rota rota = super.getById(id);
		Hibernate.initialize(rota.getTrechos());

		return rota;
	}
}
