package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroEstado;

@Component
public class EstadoRepositoryImpl extends CrudRepositoryJPA<Estado> implements EstadoRepository, Serializable {

	/** */
	private static final long serialVersionUID = 1676200668827986055L;
	
	@PersistenceContext(unitName = "ilog")
	private EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager arg0) {
		this.entityManager = arg0;
	}

	@Override
	public Estado getById(Serializable primaryKey) {
		
		Estado estado = super.getById( primaryKey );
		
		if ( estado != null ) {
			Hibernate.initialize( estado.getPais() );
		}
		
		return estado;
	}
	
	@Override
	public Estado alterar(Estado entity) throws BusinessException {
		
		entity = super.alterar(entity);
		getEntityManager().flush();
		
		return entity;
		
	}
	
	@Override
	public Estado cadastrar(Estado entity) throws BusinessException {
		entity = super.cadastrar(entity);
		return entity;
		
	}
	
	@Override
	public List<Estado> listarEstados(BasicFiltroEstado filtro)
			throws BeansException {
		HQLQuery<Estado> hql = new HQLQuery<Estado>(entityManager);
		hql.append("from Estado c");
		hql.append("inner join fetch c.pais pais");
		
		if (filtro != null) {
			
			if (filtro.getNomeEstado() != null
					&& !filtro.getNomeEstado().isEmpty()) {
				hql.appendCondicao("lower(c.nome) like  lower(:nome)");
				hql.setParameter("nome", filtro.getNomeEstado() + "%");
			}
			if (filtro.getSigla()!= null
					&& !filtro.getSigla().isEmpty()) {
				hql.appendCondicao("lower(c.sigla) like  lower(:sigla)");
				hql.setParameter("sigla", filtro.getSigla() + "%");
			}
			
			

			if ( filtro.getPais() != null ) {
				hql.appendEqual("pais.id", filtro.getPais().getId());
			}
			
			/*hql.appendCondicao(" c.nome like :nome ");
			hql.setParameter("nome", filtro.getNomeEstado() + "%");*/
		}
		hql.append("order by c.nome");
		return hql.getResultList();

	}

}
