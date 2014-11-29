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
import br.com.ilog.cadastro.business.entity.Motivo;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMotivo;

@Component
public class MotivoRepositoryImpl extends CrudRepositoryJPA<Motivo> implements MotivoRepository {

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
	public Motivo getById(Serializable primaryKey) {
		Motivo m = super.getById(primaryKey);
		if ( m != null ) {
			Hibernate.initialize( m.getTipoOcorrencia() );
		}
		
		return m;
	}
	
	@Override
	public List<Motivo> listar(BasicFiltroMotivo filtro) {
		
		HQLQuery<Motivo> hql = new HQLQuery<Motivo>(getEntityManager());
		hql.append( "from Motivo m" );
		hql.append( "inner join fetch m.tipoOcorrencia ocorrencia" );
		
		if ( filtro != null ) {
			if (filtro.getDescricao() != null
					&& !filtro.getDescricao().isEmpty()) {
				hql.appendCondicao(" lower(m.descricao) like lower(:descricao) ");
				hql.setParameter("descricao", filtro.getDescricao() + "%");

			}
			hql.appendEqual("m.ativo", filtro.getAtivo());
			
			if ( filtro.getTipoOcorrencia() != null ) {
				hql.appendEqual( "ocorrencia", filtro.getTipoOcorrencia() );
			}
		}
		
		hql.append( "order by m.descricao" );
		return hql.getResultList();
	}
	
	@Override
	public List<Motivo> listarMotivo(BasicFiltroMotivo filtro)
			throws BusinessException {
		HQLQuery<Motivo> hql = new HQLQuery<Motivo>(getEntityManager());
		hql.append( "from Motivo m" );
		hql.append("inner join fetch m.tipoOcorrencia to");
		if(filtro != null){
			hql.appendEqual("to", filtro.getTipoOcorrencia());
		}
		return hql.getResultList();
	}

}
