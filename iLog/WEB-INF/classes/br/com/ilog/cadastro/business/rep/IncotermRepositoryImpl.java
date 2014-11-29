package br.com.ilog.cadastro.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Incoterm;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroIncoterm;

@Component
public class IncotermRepositoryImpl extends CrudRepositoryJPA<Incoterm> implements IncotermRepository {

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
	public List<Incoterm> listar(BasicFiltroIncoterm filtro)
			throws BusinessException {
		
		HQLQuery<Incoterm> hql = new HQLQuery<Incoterm>( getEntityManager() );
		hql.append( "from Incoterm i" );
				
		if ( filtro != null ) {
			
			if(filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()){
				hql.appendCondicao(" lower(i.descricao) like lower(:descricao) ");
				hql.setParameter("descricao", filtro.getDescricao() + "%");

			}
			if(filtro.getSigla() != null && !filtro.getSigla().isEmpty()){
				hql.appendCondicao(" lower(i.sigla) like lower(:sigla) ");
				hql.setParameter("sigla", filtro.getSigla() + "%");

			}
			
			hql.appendEqual( "i.ativo", filtro.getAtivo() );
		}
		hql.append( "order by i.descricao" );
		
		return hql.getResultList();
	}

	@Override
	public Incoterm getBySigla(String sigla) throws BusinessException {
		
		if ( sigla != null ) {
			HQLQuery<Incoterm> hql = new HQLQuery<Incoterm>( getEntityManager() );
			hql.append( "from Incoterm i" );
			hql.appendLike( "i.sigla", sigla );
			
			return hql.getSingleResult();
		}
		return null;
	}

}
