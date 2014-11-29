package br.com.ilog.cadastro.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.UnidadeMedida;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroUnidMedida;

@Component
public class UnidadeMedidaRepositoryImpl extends
		CrudRepositoryJPA<UnidadeMedida> implements UnidadeMedidaRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<UnidadeMedida> listarUnidMedidaBysFiltro(
			BasicFiltroUnidMedida filtro) throws BusinessException {
		HQLQuery<UnidadeMedida> hql = new HQLQuery<UnidadeMedida>(entityManager);
		hql.append("from UnidadeMedida um");

		if (filtro != null) {
			if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
				hql.appendCondicao(" lower(um.codigo) like lower(:code) ");
				hql.setParameter("code", filtro.getCodigo() + "%");
			}
			if (filtro.getDescricao() != null
					&& !filtro.getDescricao().equals(" ")) {
				hql.appendCondicao(" lower(um.descricao) like lower(:desc) ");
				hql.setParameter("desc", filtro.getDescricao() + "%");
			}
			hql.appendEqual("um.ativo", filtro.getAtivo());
		}

		return hql.getResultList();

	}

	@Override
	public UnidadeMedida getByCodigo(String codigo) throws BusinessException {
		try {

			HQLQuery<UnidadeMedida> hql = new HQLQuery<UnidadeMedida>(
					entityManager);
			hql.append("from UnidadeMedida um");
			if (codigo != null && !codigo.equals("")) {
				hql.appendCondicao(" lower(um.codigo) like lower(:code) ");
				hql.setParameter("code", codigo + "%");
			}

			return hql.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
