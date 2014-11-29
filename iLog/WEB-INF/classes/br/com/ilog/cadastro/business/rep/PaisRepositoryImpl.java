package br.com.ilog.cadastro.business.rep;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroPais;

/**
 * @author Wisley P. SOuza
 */

@Component
public class PaisRepositoryImpl extends CrudRepositoryJPA<Pais> implements
		PaisRepository, Serializable {

	/** */
	private static final long serialVersionUID = -2458409149862454836L;

	@PersistenceContext(unitName="ilog")
	private EntityManager entityManager;

	@Override
	public Pais getById(Serializable arg) {
		HQLQuery<Pais> hql = new HQLQuery<Pais>(entityManager);
		hql.append("from Pais p");
		hql.appendEqual("p.id", arg);
		return hql.getSingleResult();
	}

	// Retornar o pais padrão
	@Override
	public Pais getByPadrao() {
		try {
			HQLQuery<Pais> hql = new HQLQuery<Pais>(entityManager);
			hql.append("from Pais p");
			hql.appendEqual("p.padrao", true);
			return hql.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// método listagem de países
	public List<Pais> listarPaises(BasicFiltroPais filtroPais) {
		HQLQuery<Pais> hql = new HQLQuery<Pais>(entityManager);
		hql.append("from Pais p");
		
		if ( filtroPais != null ) {
			hql.appendEqual("p.regiao", filtroPais.getRegiao() );
			if (filtroPais.getNomePais() != null
					&& !filtroPais.getNomePais().isEmpty()) {
				hql.appendCondicao("lower(p.nome) like  lower(:nome)");
				hql.setParameter("nome", filtroPais.getNomePais() + "%");
			}
			if(filtroPais.getSigla() != null && !filtroPais.getSigla().isEmpty()){
				hql.appendCondicao("lower(p.sigla) like lower(:sigla)");
				hql.setParameter("sigla", filtroPais.getSigla() + "%");

			}
		}
		hql.append("order by p.nome");
		return hql.getResultList();
	}

	// método alterar país
	@Override
	public Pais alterar(Pais entity) throws BusinessException {
		Pais p = super.alterar(entity);
		getEntityManager().flush();
		return p;
	}

	// método inserir país
	@Override
	
	public Pais cadastrar(Pais pais) throws BusinessException {
		// entityManager.getTransaction().begin();
		// entityManager.persist(pais);
		// entityManager.getTransaction().commit();
		Pais p = super.cadastrar(pais);
		return p;
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Pais> listarPaises() throws BusinessException {

		HQLQuery<Pais> hql = new HQLQuery<Pais>(getEntityManager());
		hql.append("from Pais p");
		// hql.append("");
		hql.append("order by p.nome");
		return hql.getResultList();
	}

}
