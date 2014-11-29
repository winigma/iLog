package br.com.ilog.cadastro.business.rep;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citsbusiness.rep.CrudRepositoryJPA;
import br.cits.commons.citsbusiness.util.HQLQuery;
import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.entityFilter.BasicFiltroFilial;

@Component
public class FilialRepositoryImpl extends CrudRepositoryJPA<Filial> implements
		FilialRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public Filial getFilialByCodigo(String codigo) throws BusinessException {
		try {
			HQLQuery<Filial> hql = new HQLQuery<Filial>(getEntityManager());
			hql.append("from Filial f");
			hql.appendEqual("codigo", codigo);
			return hql.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public List<Filial> listarFiliais(BasicFiltroFilial filtro)
			throws BusinessException {
		HQLQuery<Filial> hql = new HQLQuery<Filial>(getEntityManager());
		hql.append("from Filial f");
		if (filtro != null) {
			if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
				hql.appendCondicao(" lower(f.codigo) like :code ");
				hql.setParameter("code", filtro.getCodigo().toLowerCase() + "%");
			}
			if (filtro.getIdSap() != null && !filtro.getIdSap().equals("")) {
				hql.appendCondicao(" lower(f.idSap) like :idSap ");
				hql.setParameter("idSap", filtro.getIdSap().toLowerCase() + "%");
			}
			if (filtro.getDescricao() != null
					&& !filtro.getDescricao().equals(" ")) {
				hql.appendCondicao(" lower(f.descricao) like :desc ");
				hql.setParameter("desc", filtro.getDescricao().toLowerCase() + "%");
			}
			hql.appendEqual("f.ativo", filtro.getAtivo());
		}

		return hql.getResultList();
	}

}
