package br.com.ilog.cadastro.business.facade;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTipoOcorrencia;

@Component
public class TipoOcorrenciaRepositoryImpl extends
		CrudRepositoryJPA<TipoOcorrencia> implements TipoOcorrenciaRepository {

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
	public TipoOcorrencia getById(Serializable primaryKey) {

		TipoOcorrencia to = super.getById(primaryKey);
		if ( to != null ) {
			Hibernate.initialize( to.getProcessos() );
		}
		return to;
	}
	
	@Override
	public List<TipoOcorrencia> listar( BasicFiltroTipoOcorrencia filtro ) throws BusinessException {
		
		HQLQuery<TipoOcorrencia> hql = new HQLQuery<TipoOcorrencia>(entityManager);
		hql.append("from TipoOcorrencia t");
		//hql.append("inner join fetch t.processos processo");
		
		if ( filtro != null ) {
			if (filtro.getDescricao() != null
					&& !filtro.getDescricao().isEmpty()) {
				hql.appendCondicao(" lower(t.descricao) like lower(:descricao) ");
				hql.setParameter("descricao", filtro.getDescricao() + "%");

			}
			hql.appendEqual("t.ativo", filtro.getAtivo() );
			
			if ( filtro.getProcesso() != null ) 
				hql.appendEqual("processo.id", filtro.getProcesso().getId() );
		}
		
		return hql.getResultList();
	}
	
	
	@Override
	public List<TipoOcorrencia> listarTipoOcorrenciaImportacao(
			BasicFiltroTipoOcorrencia filtro) throws BusinessException {
		HQLQuery<TipoOcorrencia> hql = new HQLQuery<TipoOcorrencia>(entityManager);
		hql.append("from TipoOcorrencia t");
		hql.append("inner join fetch t.processos processo");
		hql.appendEqual("processo.id", filtro.getProcesso().getId());
		return hql.getResultList();
	}

}
