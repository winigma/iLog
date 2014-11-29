package br.com.ilog.cadastro.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroMoeda;

/**
 * @author Heber Santiago
 * 
 */
@Component
public class MoedaRepositoryImpl extends CrudRepositoryJPA<Moeda> implements
		MoedaRepository {

	@PersistenceContext(name = "ilog")
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
	public List<Moeda> listarMoedas(BasicFiltroMoeda filtroMoeda)
			throws BusinessException {
		HQLQuery<Moeda> hql = new HQLQuery<Moeda>(entityManager);
		hql.append("from Moeda m");

		if (filtroMoeda != null) {
			
			hql.appendEqual( "m.idSap", filtroMoeda.getIdSap(), true );
			
			if (filtroMoeda.getDescricao() != null) {
				hql.appendCondicao(" lower(m.descricao) like lower(:descricao) ");
				hql.setParameter("descricao", filtroMoeda.getDescricao() + "%");
			}
			if (filtroMoeda.getSigla() != null) {
				hql.appendCondicao(" lower(m.sigla) like lower(:sigla) ");
				hql.setParameter("sigla", filtroMoeda.getSigla() + "%");
			}

			hql.appendEqual("m.ativo", filtroMoeda.getAtivo());
		}

		hql.append("order by m.descricao");

		return hql.getResultList();
	}

	@Override
	public Moeda getBySigla(String sigla) throws BusinessException {
		try {

			HQLQuery<Moeda> hql = new HQLQuery<Moeda>(entityManager);
			hql.append("from Moeda m");
			if (sigla != null) {
				hql.appendCondicao(" lower(m.sigla) like lower(:sigla) ");
				hql.setParameter("sigla", sigla + "%");
			}
			return hql.getSingleResult();
		}catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Moeda getMoedaPadrao() throws BusinessException {
		
		try {
			HQLQuery<Moeda> hql = new HQLQuery<Moeda>(entityManager);
			hql.append("from Moeda m");
			hql.appendEqual("m.moedaPadrao", true);
			hql.appendEqual("m.sigla", "BRL");

			return hql.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
