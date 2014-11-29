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
import br.com.ilog.cadastro.business.entity.Terminal;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroTerminal;

@Component
public class TerminalRepositoryImpl extends CrudRepositoryJPA<Terminal> implements TerminalRepository {

	@PersistenceContext(unitName = "ilog")
	private EntityManager entityManager;
	
	@Override
	public Terminal getById(Serializable arg0) {
		
		Terminal t = super.getById( arg0 );
		
		if ( t != null ) {
			Hibernate.initialize( t.getCidade() );
			Hibernate.initialize( t.getCidade().getPais() );
			Hibernate.initialize( t.getCidade().getEstado() );
		}
		
		return t;
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager arg0) {
		entityManager = arg0;
	}

	@Override
	public List<Terminal> listarTerminais(BasicFiltroTerminal filtro)
			throws BusinessException {
		
		HQLQuery<Terminal> hql = new HQLQuery<Terminal>(entityManager);
		hql.append("from Terminal t");
		hql.append("inner join fetch t.cidade c");
		
		if (filtro != null) {
			
			if (filtro.getPais() != null) {
				hql.appendEqual("c.estado.pais.id", filtro.getPais().getId() );
			}
			if (filtro.getEstado() != null) {
				hql.appendEqual("c.estado.id", filtro.getEstado().getId() );
			}
			if (filtro.getCidade() != null) {
				hql.appendEqual("c.id", filtro.getCidade().getId() );
			}
			if (filtro.getNomeTerminal() != null) {			
				hql.appendCondicao("lower(t.nome) like lower(:nome)");
				hql.setParameter("nome", filtro.getNomeTerminal() + "%");
			}
			if (filtro.getSigla() != null && !filtro.getSigla().isEmpty()) {
				hql.appendCondicao("lower(t.sigla) like lower(:sigla)");
				hql.setParameter("sigla", filtro.getSigla() + "%");
			}
		}
		
		hql.append("order by t.nome");
		return hql.getResultList();
	}

}
